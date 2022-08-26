package com.example.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.partition.support.SimplePartitioner;
import org.springframework.batch.item.ExecutionContext;

import java.util.Map;

@Slf4j
public class SimplePartitionerImpl extends SimplePartitioner {

    private static final String PART_KEY = "partition";

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        log.debug("Partitioning");
        Map<String, ExecutionContext> partitions = super.partition(gridSize);
        int i = 0;
        for (ExecutionContext context : partitions.values()) {
            String val= PART_KEY + (i++);
            log.debug("Context :" + context);
            log.debug("Partition :" + PART_KEY + ", " + val);
            context.put(PART_KEY, val);
        }
        log.debug("Partitioning ok");
        return partitions;
    }
}
