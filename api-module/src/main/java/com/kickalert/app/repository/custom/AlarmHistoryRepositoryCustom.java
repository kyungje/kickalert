package com.kickalert.app.repository.custom;

import com.kickalert.app.dto.internal.AlarmInDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface AlarmHistoryRepositoryCustom {
    Slice<AlarmInDto.ResAlarmHistoryInfo> alarmHistoryList(Long memberId, Pageable pageable);

    Slice<AlarmInDto.ResActiveAlarmInfo> activeAlarmList(Long memberId, Pageable pageable);
}
