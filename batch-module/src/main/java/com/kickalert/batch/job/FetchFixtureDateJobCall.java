package com.kickalert.batch.job;

import com.kickalert.batch.tasklet.FetchFixtureDateTasklet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;


@Slf4j
@Configuration
@RequiredArgsConstructor
public class FetchFixtureDateJobCall {

    private final FetchFixtureDateTasklet fetchFixtureDateTasklet;

    @Bean
    public Job fetchFixtureRangeDateJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        Job job = new JobBuilder("fetchFixtureDateJob",jobRepository)
                .start(fetchFixtureDateStep(jobRepository,transactionManager))
                .build();

        return job;
    }

    public Step fetchFixtureDateStep(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        Step step = new StepBuilder("fetchFixtureDateStep",jobRepository)
                .tasklet(fetchFixtureDateTasklet,transactionManager)
                .build();
        return step;
    }
}
