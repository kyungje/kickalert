package com.kickalert.batch.repository;

import com.kickalert.core.domain.Teams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TeamRepository extends JpaRepository<Teams, Long> {
    @Modifying
    @Query(value = "truncate kickalert.teams", nativeQuery = true)
    void truncateTable();

    Teams findByApiId(Integer apiId);
}
