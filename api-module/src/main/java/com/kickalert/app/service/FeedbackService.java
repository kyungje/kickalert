package com.kickalert.app.service;

import com.kickalert.app.dto.internal.FeedbackInDto;
import com.kickalert.app.dto.internal.FollowInDto;
import com.kickalert.app.repository.FeedbackRepository;
import com.kickalert.app.repository.MemberRepository;
import com.kickalert.app.repository.PlayerFollowingRepository;
import com.kickalert.app.repository.PlayerRepository;
import com.kickalert.core.customEnum.DeleteYn;
import com.kickalert.core.customEnum.FeedBackLike;
import com.kickalert.core.domain.FeedBack;
import com.kickalert.core.domain.Members;
import com.kickalert.core.domain.PlayerFollowing;
import com.kickalert.core.domain.Players;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FeedbackService {
    private final MemberRepository memberRepository;
    private final FeedbackRepository feedbackRepository;

    @Transactional
    public Map<String, Object> feedback(FeedbackInDto.ReqFeedBack reqFeedback) {
        Map<String, Object> response = new HashMap<>();

        Members member = memberRepository.findById(Long.parseLong(reqFeedback.userId()))
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        FeedBack feedBack = FeedBack
                .builder()
                .member(member)
                .feedBackLike(FeedBackLike.valueOf(reqFeedback.like()))
                .contents(reqFeedback.contents())
                .build();


        Long id = feedbackRepository.save(feedBack).getId();

        response.put("feedbackId", id);

        return response;
    }

}
