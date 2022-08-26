package com.example.manager;

import com.example.tools.ChunkingCache;
import com.example.tools.Pair;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.lang.Exception;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/tasks")
@Slf4j
public class TaskController {
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job remotePartitioningJob;

    @Autowired
    private Job remoteChunkingJob;

    @Autowired
    private ChunkingCache chunkingCache;


    @PostMapping("/partitioning/{chaine}")
    public void remoting(@PathVariable String chaine) throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException,
            JobInstanceAlreadyCompleteException {

        launch(remotePartitioningJob, chaine);
    }

    @PostMapping("/chunking/{chaine}/{nbr}")
    public void chunking(@PathVariable String chaine, @PathVariable Integer nbr) throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException,
            JobInstanceAlreadyCompleteException {
        for (int i = 0; i < nbr; i++) {
            chunkingCache.add(new Pair<>(i, chaine));
        }
        launch(remoteChunkingJob, chaine);
    }

    private void launch(Job job, String chaine) throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        try{
            jobLauncher.run(job, new JobParametersBuilder().addString("taskKey", chaine).toJobParameters());
        }catch (Exception e)
        {
            log.info(e.getMessage());
        }
    }


}

