package com.kickalert.app.repository;

import com.kickalert.core.customEnum.DeleteYn;
import com.kickalert.core.domain.Members;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Members, Long> {
    Optional<Members> findByFcmTokenAndDeleteYn(String fcmToken, DeleteYn deleteYn);
}
