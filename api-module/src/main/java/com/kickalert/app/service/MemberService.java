package com.kickalert.app.service;

import com.kickalert.app.exception.UserCustomException;
import com.kickalert.app.repository.MemberRepository;
import com.kickalert.core.customEnum.DeleteYn;
import com.kickalert.core.domain.Members;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.kickalert.app.exception.advice.ErrorCode.UNAUTHORIZED_KICK;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public Map<String, Object> initUser(String fcmToken) {
        Map<String, Object> response = new HashMap<>();

        Optional<Members> optionalMembers = memberRepository.findByFcmTokenAndDeleteYn(fcmToken, DeleteYn.N);

        if (optionalMembers.isPresent()) {
            response.put("userId", optionalMembers.get().getId());
        } else {
            Members member = Members.builder()
                    .fcmToken(fcmToken)
                    .deleteYn(DeleteYn.N)
                    .build();

            Members returnValue = memberRepository.save(member);

            response.put("userId", returnValue.getId());
        }

        return response;
    }
}
