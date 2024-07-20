package com.kickalert.batch.tasklet;

import com.kickalert.batch.dto.api.BodyDto;
import com.kickalert.batch.repository.CountryRepository;
import com.kickalert.batch.repository.PlayerRepository;
import com.kickalert.batch.repository.TeamRepository;
import com.kickalert.batch.tasklet.common.FootballRestClient;
import com.kickalert.core.domain.Countries;
import com.kickalert.core.domain.Players;
import com.kickalert.core.domain.Teams;
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
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class FetchAllTeamTasklet implements Tasklet {
    private final TeamRepository teamRepository;
    private final CountryRepository countryRepository;
    private final PlayerRepository playerRepository;

    @Value("${football.api-token}")
    private String your_api_token;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        //전체삭제
        teamRepository.truncateTable();
        playerRepository.truncateTable();

        Integer[] countryIds = {11, 17, 32, 251, 462}; //Germany, France, spain, Italy, England
        //페이지별 데이터 저장
        for(Integer countryId: countryIds){
            int page = 1; //시작 페이지
            BodyDto bodyDto = getBodyDto(countryId, page);

            if(!CommonUtils.isEmpty(bodyDto) && !CommonUtils.isEmpty(bodyDto.getData())) {
                saveApiBodyInfo(bodyDto);

                while((Boolean)bodyDto.getPagination().get("has_more")){
                    bodyDto = getBodyDto(countryId, ++page);

                    if(!CommonUtils.isEmpty(bodyDto) && !CommonUtils.isEmpty(bodyDto.getData())) {
                        saveApiBodyInfo(bodyDto);
                    }
                }
            }
        }

        return RepeatStatus.FINISHED;
    }

    private void saveApiBodyInfo(BodyDto bodyDto) {
        bodyDto.getData().forEach(data -> {
            Integer countryId = (Integer)data.get("country_id");
            Countries countries = null;

            if(!CommonUtils.isEmpty(countryId)) {
                countries = countryRepository.findByApiId(countryId);
                if(CommonUtils.isEmpty(countries)) {
                    log.error("Not Found CountryId : {}", countryId);
                    return;
                }
            }

            Teams teams = Teams.builder()
                    .teamName(Optional.ofNullable(data.get("name")).map(Object::toString).orElse("NotExist"))
                    .apiId((Integer)data.get("id"))
                    .teamLogo(Optional.ofNullable(data.get("image_path")).map(Object::toString).orElse("NotExist"))
                    .country(countries)
                    .build();

            teamRepository.save(teams);

            //팀에 속한 선수 저장
            if(!CommonUtils.isEmpty(data.get("players")) && data.get("players") instanceof List) {
                List<Map<String, Object>> playerList = (List<Map<String, Object>>) data.get("players");

                for(Map<String, Object> player: playerList){
                    if(!CommonUtils.isEmpty(player.get("player")) && player.get("player") instanceof Map) {
                        Map<String, Object> playerInfo = (Map<String, Object>) player.get("player");

                        Players players = Players.builder()
                                .playerName(Optional.ofNullable(playerInfo.get("name")).map(Object::toString).orElse("NotExist"))
                                .apiId((Integer) playerInfo.get("id"))
                                .playerPhotoUrl(Optional.ofNullable(playerInfo.get("image_path")).map(Object::toString).orElse("NotExist"))
                                .team(teams)
                                .country(countries)
                                .build();

                        playerRepository.save(players);
                    }
                }
            }
        });
    }


    private BodyDto getBodyDto(Integer countryId, int page) {
        return  FootballRestClient.getRestClient().get()
                .uri(uriBuilder ->
                        uriBuilder.path("/football/teams/countries/{countryId}")
                                .queryParam("api_token", your_api_token)
                                .queryParam("page", page)
                                .queryParam("include", "players.player")
                                .build(countryId))
                .retrieve()
                .body(BodyDto.class);
    }
}
