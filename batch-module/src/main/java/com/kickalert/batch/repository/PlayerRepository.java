package com.kickalert.batch.repository;

import com.kickalert.core.domain.Players;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PlayerRepository extends JpaRepository<Players, Long> {
    @Modifying
    @Query(value = "truncate kickalert.players", nativeQuery = true)
    void truncateTable();
}
