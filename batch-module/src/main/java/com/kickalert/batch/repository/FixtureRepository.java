package com.kickalert.batch.repository;

import com.kickalert.core.domain.Fixtures;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FixtureRepository extends JpaRepository<Fixtures, Long> {
}
