package com.kickalert.batch.job;

import com.kickalert.batch.tasklet.FetchAllCountryTasklet;
import com.kickalert.batch.tasklet.FetchAllTeamTasklet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;


@Slf4j
@Configuration
@RequiredArgsConstructor
public class FetchAllTeamJobCall {
    private final FetchAllTeamTasklet fetchAllTeamTasklet;

    @Bean
    public Job fetchAllTeamJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        Job job = new JobBuilder("fetchAllTeamJob",jobRepository)
                .start(fetchAllTeamStep(jobRepository,transactionManager))
                .build();

        return job;
    }

    public Step fetchAllTeamStep(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        Step step = new StepBuilder("fetchAllTeamStep",jobRepository)
                .tasklet(fetchAllTeamTasklet,transactionManager)
                .build();
        return step;
    }

}
