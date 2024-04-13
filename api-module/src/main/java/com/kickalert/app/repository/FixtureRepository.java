package com.kickalert.app.repository;

import com.kickalert.app.repository.custom.FixtureRepositoryCustom;
import com.kickalert.core.domain.Fixtures;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FixtureRepository extends JpaRepository<Fixtures, Long>, FixtureRepositoryCustom {
}
