package com.kickalert.batch.job;

import com.kickalert.batch.tasklet.FetchAllCountryTasklet;
import com.kickalert.batch.tasklet.FetchAllLeagueTasklet;
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
public class FetchAllLeagueJobCall {
    private final FetchAllLeagueTasklet fetchAllLeagueTasklet;

    @Bean
    public Job fetchAllLeagueJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("fetchAllLeagueJob",jobRepository)
                .start(fetchAllLeagueStep(jobRepository,transactionManager))
                .build();
    }

    public Step fetchAllLeagueStep(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new StepBuilder("fetchAllLeagueStep",jobRepository)
                .tasklet(fetchAllLeagueTasklet,transactionManager)
                .build();
    }
}
