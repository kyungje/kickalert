package com.kickalert.app.repository;

import com.kickalert.app.repository.custom.PlayerRepositoryCustom;
import com.kickalert.core.domain.FeedBack;
import com.kickalert.core.domain.Players;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<FeedBack, Long> {
}
