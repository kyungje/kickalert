package com.kickalert.batch.job;

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
public class TestJobCall {
    @Bean
    public Job testJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        Job job = new JobBuilder("testJob",jobRepository)
                .start(testStep(jobRepository,transactionManager))
                .build();

        return job;
    }

    public Step testStep(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        Step step = new StepBuilder("testStep",jobRepository)
                .tasklet(testTasklet(),transactionManager)
                .build();
        return step;
    }

    public Tasklet testTasklet(){
        return ((contribution, chunkContext) -> {
            System.out.println("***** hello batch! *****");
            // 원하는 비지니스 로직 작성
            return RepeatStatus.FINISHED;
        });
    }
}
