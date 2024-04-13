package com.kickalert.app.repository.custom;

import com.kickalert.app.dto.internal.FixtureInDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface FixtureRepositoryCustom {
    FixtureInDto.ResFixtureInfo findFixtureInfo(Long fixtureId);

    Slice<FixtureInDto.ResFixtureInfo> nextFixtureList(Long memberId, Pageable pageable);
}
