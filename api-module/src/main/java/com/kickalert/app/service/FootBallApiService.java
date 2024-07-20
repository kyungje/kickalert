package com.kickalert.app.service;

import com.kickalert.app.dto.api.BodyDto;
import com.kickalert.core.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@Slf4j
public class FootBallApiService {

    public BodyDto fetchFixtureData() {
        int fixtureId = 18528480;
        String your_api_token = "04Cn60k0vqix4U9O59V8FpFpwJl0xOOlSmhX9D8tnHW920QLabmkYhWFz4Qu";

        RestClient restClient = RestClient.builder()
                .baseUrl("https://api.sportmonks.com/v3/football")
                .build();

        return restClient.get()
                .uri(uriBuilder ->
                        uriBuilder.path("/fixtures/{fixtureId}")
                                .queryParam("api_token", your_api_token)
                                .build(fixtureId))
                .retrieve()
                .body(BodyDto.class);
    }

    public BodyDto fetchCountryData() {
        String your_api_token = "04Cn60k0vqix4U9O59V8FpFpwJl0xOOlSmhX9D8tnHW920QLabmkYhWFz4Qu";

        RestClient restClient = RestClient.builder()
                .baseUrl("https://api.sportmonks.com/v3")
                .build();

        BodyDto bodyDto = restClient.get()
                .uri(uriBuilder ->
                        uriBuilder.path("/core/countries")
                                .queryParam("api_token", your_api_token)
                                .build())
                .retrieve()
                .body(BodyDto.class);

        if(!CommonUtils.isEmpty(bodyDto) && !CommonUtils.isEmpty(bodyDto.getData())) {
            bodyDto.getData().forEach(data -> {
                log.info("data : {}", data);
            });

            bodyDto.getPagination().forEach((key, value) -> {
                log.info("key : {}, value : {}", key, value);
            });
        }

        return bodyDto;
    }
}
