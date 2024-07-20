package com.kickalert.batch.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BatchScheduler {
    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

//    @Scheduled(cron = "0/20 * * * * *") // 20초마다 실행
//    public void runTestJob() {
//        String time = LocalDateTime.now().toString();
//        try {
//            Job job = jobRegistry.getJob("testJob"); // job 이름
//            JobParametersBuilder jobParam = new JobParametersBuilder().addString("time", time);
//            jobLauncher.run(job, jobParam.toJobParameters());
//        } catch (NoSuchJobException | JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException |
//                 JobParametersInvalidException | JobRestartException e) {
//            throw new RuntimeException(e);
//        }
//    }

//    @Scheduled(cron = "0/20 * * * * *") // 10초마다 실행
//    public void runGetAllPlayerInfoJob() {
//        String time = LocalDateTime.now().toString();
//        try {
//            Job job = jobRegistry.getJob("fetchAllPlayerInfoJob"); // job 이름
//            JobParametersBuilder jobParam = new JobParametersBuilder().addString("time", time);
//            jobLauncher.run(job, jobParam.toJobParameters());
//        } catch (NoSuchJobException | JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException |
//                 JobParametersInvalidException | JobRestartException e) {
//            throw new RuntimeException(e);
//        }
//    }

//    @Scheduled(cron = "0/30 * * * * *") // 10초마다 실행
//    public void runFetchAllCountryJob() {
//        String time = LocalDateTime.now().toString();
//        try {
//            Job job = jobRegistry.getJob("fetchAllCountryJob"); // job 이름
//            JobParametersBuilder jobParam = new JobParametersBuilder().addString("time", time);
//            jobLauncher.run(job, jobParam.toJobParameters());
//        } catch (NoSuchJobException | JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException |
//                 JobParametersInvalidException | JobRestartException e) {
//            throw new RuntimeException(e);
//        }
//    }

//    @Scheduled(cron = "0/30 * * * * *") // 10초마다 실행
//    public void runFetchAllLeagueJob() {
//        String time = LocalDateTime.now().toString();
//        try {
//            Job job = jobRegistry.getJob("fetchAllLeagueJob"); // job 이름
//            JobParametersBuilder jobParam = new JobParametersBuilder().addString("time", time);
//            jobLauncher.run(job, jobParam.toJobParameters());
//        } catch (NoSuchJobException | JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException |
//                 JobParametersInvalidException | JobRestartException e) {
//            throw new RuntimeException(e);
//        }
//    }
//    @Scheduled(cron = "0/30 * * * * *") // 10초마다 실행
//    public void runFetchAllTeamJob() {
//        String time = LocalDateTime.now().toString();
//        try {
//            Job job = jobRegistry.getJob("fetchAllTeamJob"); // job 이름
//            JobParametersBuilder jobParam = new JobParametersBuilder().addString("time", time);
//            jobLauncher.run(job, jobParam.toJobParameters());
//        } catch (NoSuchJobException | JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException |
//                 JobParametersInvalidException | JobRestartException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Scheduled(cron = "0/30 * * * * *") // 10초마다 실행
//    public void runFetchJob() {
//        String time = LocalDateTime.now().toString();
//        try {
//            Job job = jobRegistry.getJob("fetchFixtureDateJob"); // job 이름
//            JobParametersBuilder jobParam = new JobParametersBuilder().addString("time", time);
//            jobLauncher.run(job, jobParam.toJobParameters());
//        } catch (NoSuchJobException | JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException |
//                 JobParametersInvalidException | JobRestartException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
