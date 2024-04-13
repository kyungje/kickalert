package com.kickalert.app.repository;

import com.kickalert.app.repository.custom.AlarmHistoryRepositoryCustom;
import com.kickalert.core.customEnum.DeleteYn;
import com.kickalert.core.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayerFollowingRepository extends JpaRepository<PlayerFollowing, Long> {
    Optional<PlayerFollowing> findByPlayerAndMemberAndDeleteYn(Players player, Members member, DeleteYn deleteYn);
}
