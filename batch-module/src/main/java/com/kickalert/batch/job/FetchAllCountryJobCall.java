package com.kickalert.batch.job;

import com.kickalert.batch.tasklet.FetchAllCountryTasklet;
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
public class FetchAllCountryJobCall {
    private final FetchAllCountryTasklet fetchAllCountryTasklet;

    @Bean
    public Job fetchAllCountryJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("fetchAllCountryJob",jobRepository)
                .start(fetchAllCountryStep(jobRepository,transactionManager))
                .build();
    }

    public Step fetchAllCountryStep(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new StepBuilder("fetchAllCountryStep",jobRepository)
                .tasklet(fetchAllCountryTasklet,transactionManager)
                .build();
    }
}
