package com.kickalert.batch.dto.api;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class BodyDto {
    List<Map<String,Object>> data;
    Map<String,Object> pagination;
    String timezone;
}
