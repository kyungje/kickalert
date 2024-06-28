package com.kickalert.app.repository;

import com.kickalert.core.domain.Players;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PlayerRepositoryTest {
    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private PlayerRepository playerRepository;

//    @Test
//    void insertPlayer() {
//        //given
//        //when
//        //then
//        Players player = Players.builder()
//                .id(1L)
//                .playerName("son")
//                .build();
//
//        testEntityManager.persist(player);
//    }

    @Test
    void matchPlayerList() {
        playerRepository.matchPlayerList(1L, 1L, 1L);
    }
}