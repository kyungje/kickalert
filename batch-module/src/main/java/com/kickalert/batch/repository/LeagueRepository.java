package com.kickalert.batch.repository;

import com.kickalert.core.domain.Leagues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface LeagueRepository extends JpaRepository<Leagues, Long> {
    @Modifying
    @Query(value = "truncate kickalert.leagues", nativeQuery = true)
    void truncateTable();

    Leagues findByApiId(Integer apiId);
}
