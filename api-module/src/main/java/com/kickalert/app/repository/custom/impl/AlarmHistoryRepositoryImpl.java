package com.kickalert.app.repository.custom.impl;

import com.kickalert.app.dto.internal.AlarmInDto;
import com.kickalert.app.repository.custom.AlarmHistoryRepositoryCustom;
import com.kickalert.app.util.RepositorySliceHelper;
import com.kickalert.core.util.CommonUtils;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.ZonedDateTime;
import java.util.List;

import static com.kickalert.core.domain.QAlarmHistory.alarmHistory;
import static com.kickalert.core.domain.QFixtures.fixtures;
import static com.kickalert.core.domain.QLeagues.leagues;
import static com.kickalert.core.domain.QPlayers.players;
import static com.kickalert.core.domain.QTeams.teams;

public class AlarmHistoryRepositoryImpl implements AlarmHistoryRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public AlarmHistoryRepositoryImpl(EntityManager entityManager){
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Slice<AlarmInDto.ResAlarmHistoryInfo> alarmHistoryList(Long memberId , Pageable pageable){
        List<AlarmInDto.ResAlarmHistoryInfo> content = queryFactory
                .select(Projections.constructor(AlarmInDto.ResAlarmHistoryInfo.class,
                        alarmHistory.homeTeam.id.as("homeTeamId"),
                        alarmHistory.awayTeam.id.as("awayTeamId"),
                        ExpressionUtils.as(JPAExpressions.select(teams.teamName).from(teams).where(teams.id.eq(alarmHistory.homeTeam.id)), "homeTeamName"),
                        ExpressionUtils.as(JPAExpressions.select(teams.teamName).from(teams).where(teams.id.eq(alarmHistory.awayTeam.id)), "awayTeamName"),
                        alarmHistory.matchDateTime.as("matchDateTime"),
                        ExpressionUtils.as(JPAExpressions.select(teams.teamLogo).from(teams).where(teams.id.eq(alarmHistory.homeTeam.id)), "homeLogo"),
                        ExpressionUtils.as(JPAExpressions.select(teams.teamLogo).from(teams).where(teams.id.eq(alarmHistory.awayTeam.id)), "awayLogo"),
                        fixtures.leagues.id,
                        ExpressionUtils.as(JPAExpressions.select(leagues.leagueName).from(leagues).where(leagues.id.eq(fixtures.leagues.id)), "leagueName"),
                        alarmHistory.player.id,
                        ExpressionUtils.as(JPAExpressions.select(players.playerName).from(players).where(players.id.eq(alarmHistory.player.id)), "playerName"),
                        alarmHistory.alarmType,
                        alarmHistory.alarmDateTime))
                .from(alarmHistory)
                .join(alarmHistory.fixture, fixtures)
                .where(alarmHistory.member.id.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return RepositorySliceHelper.toSlice(content, pageable);
    }

    public Slice<AlarmInDto.ResActiveAlarmInfo> activeAlarmList(Long memberId, Pageable pageable){
        List<AlarmInDto.ResActiveAlarmInfo> content = queryFactory
                .select(Projections.fields(AlarmInDto.ResActiveAlarmInfo.class,
                        alarmHistory.fixture.id.as("fixtureId"),
                        alarmHistory.homeTeam.id.as("homeTeamId"),
                        alarmHistory.awayTeam.id.as("awayTeamId"),
                        ExpressionUtils.as(JPAExpressions.select(teams.teamName).from(teams).where(teams.id.eq(alarmHistory.homeTeam.id)), "homeTeamName"),
                        ExpressionUtils.as(JPAExpressions.select(teams.teamName).from(teams).where(teams.id.eq(alarmHistory.awayTeam.id)), "awayTeamName"),
                        alarmHistory.matchDateTime.as("matchDateTimeOriginal"),
                        ExpressionUtils.as(JPAExpressions.select(teams.teamLogo).from(teams).where(teams.id.eq(alarmHistory.homeTeam.id)), "homeLogo"),
                        ExpressionUtils.as(JPAExpressions.select(teams.teamLogo).from(teams).where(teams.id.eq(alarmHistory.awayTeam.id)), "awayLogo")
                        ))
                .from(alarmHistory)
                .where(alarmHistory.member.id.eq(memberId)
                        .and(alarmHistory.matchDateTime.after(ZonedDateTime.now().toLocalDateTime())))
                .groupBy(alarmHistory.fixture.id, alarmHistory.homeTeam.id, alarmHistory.awayTeam.id, alarmHistory.matchDateTime)
                .orderBy(alarmHistory.matchDateTime.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        content.forEach(activeAlarm -> {
            List<AlarmInDto.AlarmPlayerInfo> alarmPlayers = playerList(memberId, activeAlarm.getFixtureId());
            activeAlarm.setAlarmPlayers(alarmPlayers);
            activeAlarm.setMatchDateTime(CommonUtils.toUTCStringFromDateTime(activeAlarm.getMatchDateTimeOriginal()));
        });

        return RepositorySliceHelper.toSlice(content, pageable);
    }

     List<AlarmInDto.AlarmPlayerInfo> playerList(Long memberId, Long fixtureId){
         return queryFactory
                .select(Projections.constructor(AlarmInDto.AlarmPlayerInfo.class,
                        players.id.as("playerId"),
                        players.playerName,
                        players.playerPhotoUrl,
                        alarmHistory.alarmType
                        ))
                .from(alarmHistory)
                .join(alarmHistory.player, players).on(alarmHistory.player.id.eq(players.id))
                .where(alarmHistory.member.id.eq(memberId)
                        .and(alarmHistory.fixture.id.eq(fixtureId)
                ))
                .fetch();
    }

}
