package com.kickalert.batch.repository;

import com.kickalert.core.domain.Countries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CountryRepository extends JpaRepository<Countries, Long> {
    @Modifying
    @Query(value = "truncate kickalert.countries", nativeQuery = true)
    void truncateTable();

    Countries findByApiId(Integer apIid);
}
