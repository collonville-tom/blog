package com.example.manager;

import com.example.tools.SimplePartitionerImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.integration.config.annotation.EnableBatchIntegration;
import org.springframework.batch.integration.partition.RemotePartitioningManagerStepBuilderFactory;
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

    private static final int GRID_SIZE = 3;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private RemotePartitioningManagerStepBuilderFactory managerStepBuilderFactory;

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
        log.debug("build outboundPartFlow");
        return IntegrationFlows.from(exchangePart())
                .handle(Amqp.outboundAdapter(amqpTemplate).routingKey("partitionTask"))
                .get();
    }

    @Bean
    public IntegrationFlow inboundPartFlow(ConnectionFactory factory) {
        log.debug("build inboundPartFlow");
        return IntegrationFlows.from(Amqp.inboundAdapter(factory, "partitionResults"))
                .channel(queuePart())
                .get();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata defaultPoller() {
        PollerMetadata pollerMetadata = new PollerMetadata();
        pollerMetadata.setTrigger(new PeriodicTrigger(1));
        return pollerMetadata;
    }

    @Bean
    public Job remotePartitioningJob() {
        log.debug("Execute job");
        return this.jobBuilderFactory.get("remotePartitioningJob")
                .incrementer(new RunIdIncrementer())
                .start(pRemoteStep())
                .build();
    }
    @Bean
    public Step pRemoteStep() {
        log.debug("Init manager Step with Part");
        return this.managerStepBuilderFactory.get("pRemoteStep")
                .partitioner("workerStepPart", new SimplePartitionerImpl()).gridSize(GRID_SIZE)
                .outputChannel(exchangePart())
                .inputChannel(queuePart())
                .build();
    }


}
