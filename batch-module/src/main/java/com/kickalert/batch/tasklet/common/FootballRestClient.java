package com.kickalert.batch.tasklet.common;

import org.springframework.web.client.RestClient;

public class FootballRestClient {
    public static RestClient getRestClient() {
        return RestClient.builder()
                .baseUrl("https://api.sportmonks.com/v3")
                .build();
    }


}
