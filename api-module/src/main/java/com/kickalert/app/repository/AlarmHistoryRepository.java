package com.kickalert.app.repository;

import com.kickalert.app.repository.custom.AlarmHistoryRepositoryCustom;
import com.kickalert.app.repository.custom.PlayerRepositoryCustom;
import com.kickalert.core.customEnum.DeleteYn;
import com.kickalert.core.domain.AlarmHistory;
import com.kickalert.core.domain.Fixtures;
import com.kickalert.core.domain.Players;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlarmHistoryRepository extends JpaRepository<AlarmHistory, Long>, AlarmHistoryRepositoryCustom {
    Optional<AlarmHistory> findByPlayerAndFixture(Players player, Fixtures fixture);
}
