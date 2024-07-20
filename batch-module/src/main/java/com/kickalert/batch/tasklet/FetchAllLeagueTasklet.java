package com.kickalert.batch.tasklet;

import com.kickalert.batch.dto.api.BodyDto;
import com.kickalert.batch.repository.CountryRepository;
import com.kickalert.batch.repository.LeagueRepository;
import com.kickalert.batch.tasklet.common.FootballRestClient;
import com.kickalert.core.domain.Countries;
import com.kickalert.core.domain.Leagues;
import com.kickalert.core.util.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class FetchAllLeagueTasklet implements Tasklet {
    private final LeagueRepository leagueRepository;
    private final CountryRepository countryRepository;

    @Value("${football.api-token}")
    private String your_api_token;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        //전체삭제
        leagueRepository.truncateTable();
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

            Leagues leagues = Leagues.builder()
                    .leagueName(Optional.ofNullable(data.get("name")).map(Object::toString).orElse("NotExist"))
                    .apiId((Integer)data.get("id"))
                    .leagueLogo(Optional.ofNullable(data.get("image_path")).map(Object::toString).orElse("NotExist"))
                    .country(countries)
                    .build();

            leagueRepository.save(leagues);
        });
    }


    private BodyDto getBodyDto(Integer countryId, int page) {
        return  FootballRestClient.getRestClient().get()
                .uri(uriBuilder ->
                        uriBuilder.path("/football/leagues/countries/{countryId}")
                                .queryParam("api_token", your_api_token)
                                .queryParam("page", page)
                                .build(countryId))
                .retrieve()
                .body(BodyDto.class);
    }


}
