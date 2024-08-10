package com.kickalert.batch.tasklet;

import com.kickalert.batch.dto.api.BodyDto;
import com.kickalert.batch.repository.*;
import com.kickalert.batch.tasklet.common.FootballRestClient;
import com.kickalert.core.domain.*;
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
public class FetchFixtureDateTasklet implements Tasklet {
    private final TeamRepository teamRepository;
    private final LeagueRepository leagueRepository;
    private final FixtureRepository fixtureRepository;

    @Value("${football.api-token}")
    private String your_api_token;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {

        String[] strDates = {"2024-01-27"};
        String fixtureLeagues = "8, 82, 301, 564, 384";

        //페이지별 데이터 저장
        for (String date : strDates) {
            int page = 1; //시작 페이지
            BodyDto bodyDto = getBodyDto(fixtureLeagues, date , page);

            if (!CommonUtils.isEmpty(bodyDto) && !CommonUtils.isEmpty(bodyDto.getData())) {
                saveApiBodyInfo(bodyDto);

                while ((Boolean) bodyDto.getPagination().get("has_more")) {
                    bodyDto =  getBodyDto(fixtureLeagues, date, ++page);

                    if (!CommonUtils.isEmpty(bodyDto) && !CommonUtils.isEmpty(bodyDto.getData())) {
                        saveApiBodyInfo(bodyDto);
                    }
                }
            }
        }

        return RepeatStatus.FINISHED;
    }

    private void saveApiBodyInfo(BodyDto bodyDto) {
        bodyDto.getData().forEach(data -> {
            Teams homeTeam = null;
            Teams awayTeam = null;
            String venueName = "";
            Leagues league = null;

            log.info("data : {}", data);
            //경기에 참여하는 홈팀과 어웨이팀 정보 가져오기
            if (!CommonUtils.isEmpty(data.get("participants")) && data.get("participants") instanceof List) {
                List<Map<String, Object>> participantList = (List<Map<String, Object>>) data.get("participants");
                log.info("participantList : {}", participantList);

                for (Map<String, Object> participant : participantList) {
                    if (!CommonUtils.isEmpty(participant.get("id")) && participant.get("id") instanceof Integer) {
                        Integer teamApiId = (Integer) participant.get("id");

                        if (!CommonUtils.isEmpty(participant.get("meta")) && participant.get("meta") instanceof Map) {
                            Map<String, Object> metaInfo = (Map<String, Object>) participant.get("meta");

                            String location = (String) metaInfo.get("location");

                            if (location.equals("home")) {
                                homeTeam = teamRepository.findByApiId(teamApiId);
                            } else if (location.equals("away")) {
                                awayTeam = teamRepository.findByApiId(teamApiId);
                            }

                        }
                    }
                }
            }

            //경기장 정보 가져오기
            if (!CommonUtils.isEmpty(data.get("venue")) && data.get("venue") instanceof Map) {
                Map<String, Object> venueInfo = (Map<String, Object>) data.get("venue");

                venueName = (String) venueInfo.get("name");
            }

            //리그 정보 가져오기
            if (!CommonUtils.isEmpty(data.get("league_id")) && data.get("league_id") instanceof Integer) {
                Integer leagueId = (Integer) data.get("league_id");
                league = leagueRepository.findByApiId(leagueId);
            }

            Fixtures fixture = Fixtures.builder()
                    .venue(venueName)
                    .homeTeam(homeTeam)
                    .awayTeam(awayTeam)
                    .leagues(league)
                    .apiId((Integer) data.get("id"))
                    .datetime(CommonUtils.convertStringToLocalDateTime((String) data.get("starting_at")))
                    .build();

            fixtureRepository.save(fixture);
        });
    }


    private BodyDto getBodyDto(String leagues, String date, int page) {
        String fixtureLeagues = "fixtureLeagues:" + leagues;
        return FootballRestClient.getRestClient().get()
                .uri(uriBuilder ->
                        uriBuilder.path("/football/fixtures/date/{date}")
                                .queryParam("api_token", your_api_token)
                                .queryParam("page", page)
                                .queryParam("include", "participants;venue")
                                .queryParam("filters", fixtureLeagues)
                                .build(date))
                .retrieve()
                .body(BodyDto.class);
    }
}