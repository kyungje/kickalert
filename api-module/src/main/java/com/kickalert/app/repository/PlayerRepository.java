package com.kickalert.app.repository;

import com.kickalert.app.repository.custom.PlayerRepositoryCustom;
import com.kickalert.core.domain.Players;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Players, Long>, PlayerRepositoryCustom {
}
