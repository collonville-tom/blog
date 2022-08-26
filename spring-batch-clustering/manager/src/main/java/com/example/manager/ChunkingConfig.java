package com.example.manager;

import com.example.tools.ChunkingCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.integration.chunk.RemoteChunkingManagerStepBuilderFactory;
import org.springframework.batch.integration.config.annotation.EnableBatchIntegration;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

import java.util.Arrays;

@Configuration
@EnableBatchProcessing
@EnableBatchIntegration
@Slf4j
public class ChunkingConfig {

    @Autowired
    private ChunkingCache chunkingCache;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private RemoteChunkingManagerStepBuilderFactory remoteChunkingManagerStepBuilderFactory;

    @Bean
    public DirectChannel exchangeChunk() {
        return new DirectChannel();
    }

    @Bean
    public QueueChannel queueChunk() {
        return new QueueChannel();
    }

    @Bean
    public IntegrationFlow outboundChunkFlow(AmqpTemplate amqpTemplate) {
        log.debug("build Chunk outboundFlow");
        return IntegrationFlows
                .from(exchangeChunk())
                .handle(Amqp.outboundAdapter(amqpTemplate).routingKey("chunkTask"))
                .get();
    }

    @Bean
    public IntegrationFlow inboundChunkFlow(ConnectionFactory factory) {
        log.debug("build Chunk inboundFlow");
        return IntegrationFlows
                .from(Amqp.inboundAdapter(factory, "chunkResults"))
                .channel(queueChunk())
                .get();
    }



    @Bean
    public TaskletStep cRemoteStep() {
        return this.remoteChunkingManagerStepBuilderFactory.get("cRemoteStep")
                .<Integer, Integer>chunk(3)
                .reader(chunkingCache)
                .outputChannel(exchangeChunk())
                .inputChannel(queueChunk())
                .build();
    }

    @Bean
    public Job remoteChunkingJob() {
        return this.jobBuilderFactory.get("remoteChunkingJob")
                .incrementer(new RunIdIncrementer())
                .start(cRemoteStep())
                .build();
    }

    @Bean
    public ListItemReader<Integer> itemReader() {
        return new ListItemReader<>(Arrays.asList(1, 2, 3, 4, 5, 6));
    }


}
