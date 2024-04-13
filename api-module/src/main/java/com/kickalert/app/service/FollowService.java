package com.kickalert.app.service;

import com.kickalert.app.dto.internal.FollowInDto;
import com.kickalert.app.repository.MemberRepository;
import com.kickalert.app.repository.PlayerFollowingRepository;
import com.kickalert.app.repository.PlayerRepository;
import com.kickalert.core.customEnum.DeleteYn;
import com.kickalert.core.domain.AlarmHistory;
import com.kickalert.core.domain.Members;
import com.kickalert.core.domain.PlayerFollowing;
import com.kickalert.core.domain.Players;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FollowService {
    private final PlayerFollowingRepository playerFollowingRepository;
    private final MemberRepository memberRepository;
    private final PlayerRepository playerRepository;

    @Transactional
    public Map<String, Object> playerFollowing(FollowInDto.ReqFollow reqFollow) {
        Map<String, Object> response = new HashMap<>();

        Members member = memberRepository.findById(Long.parseLong(reqFollow.userId()))
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        Players player = playerRepository.findById(Long.parseLong(reqFollow.playerId()))
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));

        PlayerFollowing playerFollowing = playerFollowingRepository.findByPlayerAndMemberAndDeleteYn(player, member, DeleteYn.N)
                .orElseGet(() -> PlayerFollowing.builder()
                        .member(member)
                        .player(player)
                        .deleteYn(DeleteYn.N)
                        .build());

        Long id = playerFollowingRepository.save(playerFollowing).getId();

        response.put("playerFollowingId", id);

        return response;
    }

    @Transactional
    public Map<String, Object> playerUnFollowing(FollowInDto.ReqFollow reqFollow) {
        Map<String, Object> response = new HashMap<>();
        Long id = 0L;

        Members member = memberRepository.findById(Long.parseLong(reqFollow.userId()))
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        Players player = playerRepository.findById(Long.parseLong(reqFollow.playerId()))
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));

        Optional<PlayerFollowing> playerFollowingOp = playerFollowingRepository.findByPlayerAndMemberAndDeleteYn(player, member, DeleteYn.N);

        if(playerFollowingOp.isPresent()) {
            PlayerFollowing playerFollowing = playerFollowingOp.get();
            playerFollowing.changeDeleteYn(DeleteYn.Y);

            id = playerFollowingRepository.save(playerFollowing).getId();
        }

        response.put("playerFollowingId", id);

        return response;
    }
}
