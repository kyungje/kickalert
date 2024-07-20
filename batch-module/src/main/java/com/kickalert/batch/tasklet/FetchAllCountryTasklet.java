package com.kickalert.batch.tasklet;

import com.kickalert.batch.dto.api.BodyDto;
import com.kickalert.batch.repository.CountryRepository;
import com.kickalert.batch.tasklet.common.FootballRestClient;
import com.kickalert.core.domain.Countries;
import com.kickalert.core.util.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigInteger;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class FetchAllCountryTasklet implements Tasklet {
    private final CountryRepository countryRepository;

    @Value("${football.api-token}")
    private String your_api_token;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        //전체삭제
        countryRepository.truncateTable();

        //페이지별 데이터 저장
        int page = 1; //시작 페이지
        BodyDto bodyDto = getBodyDto(page);

        if(!CommonUtils.isEmpty(bodyDto) && !CommonUtils.isEmpty(bodyDto.getData())) {
            saveApiBodyInfo(bodyDto);

            while((Boolean)bodyDto.getPagination().get("has_more")){
                bodyDto = getBodyDto(++page);

                if(!CommonUtils.isEmpty(bodyDto) && !CommonUtils.isEmpty(bodyDto.getData())) {
                    saveApiBodyInfo(bodyDto);
                }
            }
        }
        return RepeatStatus.FINISHED;
    }

    private void saveApiBodyInfo(BodyDto bodyDto) {
        bodyDto.getData().forEach(data -> {
            Countries countries = Countries.builder()
                    .countryName(data.getOrDefault("name","").toString())
                    .apiId((Integer)data.get("id"))
                    .countryLogo(Optional.ofNullable(data.get("image_path")).map(Object::toString).orElse("NotExist"))
                    .fifaName(Optional.ofNullable(data.get("fifa_name")).map(Object::toString).orElse("NotExist"))
                    .build();

            countryRepository.save(countries);
        });
    }

    private BodyDto getBodyDto(int page) {
        return  FootballRestClient.getRestClient().get()
                .uri(uriBuilder ->
                        uriBuilder.path("/core/countries")
                                .queryParam("api_token", your_api_token)
                                .queryParam("page", page)
                                .build())
                .retrieve()
                .body(BodyDto.class);
    }
}