package com.example.worker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.integration.config.annotation.EnableBatchIntegration;
import org.springframework.batch.integration.partition.RemotePartitioningWorkerStepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.scheduling.support.PeriodicTrigger;

@Configuration
@EnableBatchProcessing
@EnableBatchIntegration
@Slf4j
public class PartitioningConfig {


    @Autowired
    private RemotePartitioningWorkerStepBuilderFactory workerStepBuilderFactory;


    @Bean
    public DirectChannel exchangePart() {
        return new DirectChannel();
    }

    @Bean
    public QueueChannel queuePart() {
        return new QueueChannel();
    }

    @Bean
    public IntegrationFlow outboundPartFlow(AmqpTemplate amqpTemplate) {
        return IntegrationFlows
                .from(exchangePart())
                .handle(Amqp.outboundAdapter(amqpTemplate).routingKey("partitionResults"))
                .get();
    }

    @Bean
    public IntegrationFlow inboundPartFlow(ConnectionFactory factory) {
        return IntegrationFlows
                .from(Amqp.inboundAdapter(factory, "partitionTask"))
                .channel(queuePart())
                .get();
    }

    @Bean
    public Step workerStepPart() {
        log.debug("build workerStep");
        return this.workerStepBuilderFactory.get("workerStepPart")
                .inputChannel(queuePart()).outputChannel(exchangePart())
                .tasklet(tasklet())
                .build();
    }

    @Bean
    @StepScope
    public Tasklet tasklet() {
        return (contribution, chunkContext) -> {
            log.debug("processing " + contribution +", "+chunkContext);
            log.debug(chunkContext.getStepContext().getJobParameters().toString());
            return RepeatStatus.FINISHED;
        };
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata defaultPoller() {
        PollerMetadata pollerMetadata = new PollerMetadata();
        pollerMetadata.setTrigger(new PeriodicTrigger(1));
        return pollerMetadata;
    }

}
