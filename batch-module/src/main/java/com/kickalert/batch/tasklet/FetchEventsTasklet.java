package com.kickalert.batch.tasklet;

import com.kickalert.batch.dto.api.AlarmHistoryDto;
import com.kickalert.batch.dto.api.BodyDto;
import com.kickalert.batch.repository.AlarmHistoryRepository;
import com.kickalert.batch.tasklet.common.FootballRestClient;
import com.kickalert.core.util.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class FetchEventsTasklet implements Tasklet {
    private final AlarmHistoryRepository alarmHistoryRepository;

    @Value("${football.api-token}")
    private String your_api_token;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {

        String fixtureLeagues = "8, 82, 301, 564, 384";

        //페이지별 데이터 저장
        int page = 1; //시작 페이지
        BodyDto bodyDto = getBodyDto(fixtureLeagues, page);

        if (!CommonUtils.isEmpty(bodyDto) && !CommonUtils.isEmpty(bodyDto.getData())) {
            processApiBodyInfo(bodyDto);

            while ((Boolean) bodyDto.getPagination().get("has_more")) {
                bodyDto =  getBodyDto(fixtureLeagues, ++page);

                if (!CommonUtils.isEmpty(bodyDto) && !CommonUtils.isEmpty(bodyDto.getData())) {
                    processApiBodyInfo(bodyDto);
                }
            }
        }

        return RepeatStatus.FINISHED;
    }

    private void processApiBodyInfo(BodyDto bodyDto) {
        bodyDto.getData().forEach(data -> {
            log.info("data : {}", data);
            if (!CommonUtils.isEmpty(data.get("id")) && data.get("id") instanceof Integer apiFixtureId) {

                List<AlarmHistoryDto.AlarmInfo> alarmsWithinOneHourList = alarmHistoryRepository.findAlarmsWithinOneHour(apiFixtureId);

                for(AlarmHistoryDto.AlarmInfo alarmInfo : alarmsWithinOneHourList){
                    log.info("alarmHistory : {}", alarmInfo);

                    //알람 신청 이력이 있으면 알람처리
                    if(checkAlarmSetting(alarmInfo, data)){
                        //선발 정보 알람처리(경기시작알람)
                        sendAlarmMessage(alarmInfo);
                    }
                    //알람처리 후 알람 이력 업데이트
                }
            } else {
                return;
            }
        });
    }

    /*
    * alarmtype : 1 - 선발, 2 - 선발 + 후보 , 3 - 출전
    * type_id : 1 - 전반 시작, 2 - 후반 시작, 3 - 추가시간, 11(lineups) - 선발, 12(lineups) - 후보, 18 - 교체
    */
    private boolean checkAlarmSetting(AlarmHistoryDto.AlarmInfo alarmInfo, Map<String, Object> apiData) {
        String alarmType = alarmInfo.getAlarmType();
        String playerSelection = "NON"; //선발 - STA, 후보 - SUB, 없음 - NON

        if(!CommonUtils.isEmpty(apiData.get("lineups")) && apiData.get("lineups") instanceof List) {
            List<Map<String, Object>> lineupList = (List<Map<String, Object>>) apiData.get("lineups");

            for(Map<String, Object> lineup: lineupList){
                if(!CommonUtils.isEmpty(lineup.get("type_id")) && lineup.get("type_id") instanceof Integer){
                    Integer lineupType = (Integer) lineup.get("type_id");

                    if(!CommonUtils.isEmpty(lineup.get("player_id")) && lineup.get("player_id") instanceof Integer){
                        Integer playerId = (Integer) lineup.get("player_id");

                        if(playerId.equals(alarmInfo.getPlayerApiId())){
                            if(lineupType == 11){
                                playerSelection = "STA";
                            } else if(lineupType == 12){
                                playerSelection = "SUB";
                            }
                        }
                    }

                }
            }
        }
        //선발, 후보 모두 없으면 알람 안 울림
        if("NON".equals(playerSelection)){
            return false;
        }

        if(!CommonUtils.isEmpty(apiData.get("events")) && apiData.get("events") instanceof List) {
            List<Map<String, Object>> eventList = (List<Map<String, Object>>) apiData.get("events");

            for(Map<String, Object> event: eventList){
                if(!CommonUtils.isEmpty(event.get("type_id")) && event.get("type_id") instanceof Integer){
                    Integer eventType = (Integer) event.get("type_id");
                    //알람이 선발이고 전반시작이고 실제 선발이면
                    if("1".equals(alarmType) && eventType == 1 && "STA".equals(playerSelection)){
                        return true;
                    // 알람이 선발+후보이고 전반시작이고 실제 선발이거나 후보이면(여기까지 오면 항상 STA 나 SUB)
                    } else if("2".equals(alarmType) && eventType == 1){
                        return true;
                    } else if("3".equals(alarmType) && eventType == 18){
                        return true;
                    }
                }
            }
        }

        //알람 설정 체크
        return false;
    }

    private void sendAlarmMessage(AlarmHistoryDto.AlarmInfo alarmInfo) {
        //알람 메시지 전송
    }


    private BodyDto getBodyDto(String fixtureLeagues, int page) {
        String qFixtureLeagues = "fixtureLeagues:" + fixtureLeagues;
        return FootballRestClient.getRestClient().get()
                .uri(uriBuilder ->
                        uriBuilder.path("/football/livescores/latest")
                                .queryParam("api_token", your_api_token)
                                .queryParam("page", page)
                                .queryParam("include", "events;lineups")
                                .queryParam("filters", qFixtureLeagues)
                                .build())
                .retrieve()
                .body(BodyDto.class);
    }
}