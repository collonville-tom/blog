package com.example.worker;

import com.example.tools.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.integration.chunk.RemoteChunkingWorkerBuilder;
import org.springframework.batch.integration.config.annotation.EnableBatchIntegration;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;


@Configuration
@EnableBatchProcessing
@EnableBatchIntegration
@Slf4j
public class ChunkingConfig {

    @Autowired
    private RemoteChunkingWorkerBuilder<Pair<Integer,String>, String> remoteChunkingWorkerBuilder;

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
        return IntegrationFlows
                .from(exchangeChunk())
                .handle(Amqp.outboundAdapter(amqpTemplate).routingKey("chunkResults"))
                .get();
    }

    @Bean
    public IntegrationFlow inboundChunkFlow(ConnectionFactory factory) {
        return IntegrationFlows
                .from(Amqp.inboundAdapter(factory, "chunkTask"))
                .channel(queueChunk())
                .get();
    }


    @Bean
    public ItemProcessor<Pair<Integer,String>, String> itemProcessor() {
        return item -> {
            log.info("processing item " + item);
            return item.getK()+"-"+item.getV();
        };
    }

    @Bean
    public ItemWriter<String> itemWriter() {
        return items -> {
            for (String item : items) {
                log.info("writing item " + item);
            }
        };
    }

    @Bean
    public IntegrationFlow workerIntegrationFlow() {
        return this.remoteChunkingWorkerBuilder
                .itemProcessor(itemProcessor())
                .itemWriter(itemWriter())
                .inputChannel(queueChunk())
                .outputChannel(exchangeChunk())
                .build();
    }
}
