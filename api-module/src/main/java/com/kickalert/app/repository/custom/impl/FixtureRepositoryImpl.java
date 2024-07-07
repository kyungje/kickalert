package com.kickalert.app.repository.custom.impl;

import com.kickalert.app.dto.internal.AlarmInDto;
import com.kickalert.app.dto.internal.FixtureInDto;
import com.kickalert.app.repository.custom.FixtureRepositoryCustom;
import com.kickalert.app.util.RepositorySliceHelper;
import com.kickalert.core.customEnum.DeleteYn;
import com.kickalert.core.domain.QTeams;
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
import static com.kickalert.core.domain.QPlayerFollowing.playerFollowing;
import static com.kickalert.core.domain.QPlayers.players;
import static com.kickalert.core.domain.QTeams.teams;

public class FixtureRepositoryImpl implements FixtureRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public FixtureRepositoryImpl(EntityManager entityManager){
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public FixtureInDto.ResFixtureInfo findFixtureInfo(Long fixtureId){
        QTeams homeTeam = new QTeams("homeTeam");
        QTeams awayTeam = new QTeams("awayTeam");

        return queryFactory
                .select(Projections.constructor(
                        FixtureInDto.ResFixtureInfo.class,
                        fixtures.id.as("fixtureId"),
                        homeTeam.id.as("homeTeamId"),
                        awayTeam.id.as("awayTeamId"),
                        homeTeam.teamName.as("homeTeamName"),
                        awayTeam.teamName.as("awayTeamName"),
                        fixtures.datetime.as("matchDateTime"),
                        homeTeam.teamLogo.as("homeLogo"),
                        awayTeam.teamLogo.as("awayLogo"),
                        leagues.id.as("leagueId"),
                        leagues.leagueName.as("leagueName")))
                .from(fixtures)
                .join(fixtures.homeTeam, homeTeam)
                .join(fixtures.awayTeam, awayTeam)
                .join(fixtures.leagues, leagues)
                .where(fixtures.id.eq(fixtureId))
                .fetchOne();
    }

    public Slice<FixtureInDto.ResFixtureInfo> nextFixtureList(Long memberId , Pageable pageable){
        List<Long> teamIds = queryFactory
                .select(teams.id)
                .from(teams)
                .join(players)
                .on(teams.eq(players.team))
                .where(players.id.in(
                        JPAExpressions
                                .select(playerFollowing.player.id)
                                .from(playerFollowing)
                                .where(playerFollowing.member.id.eq(memberId)
                                        .and(playerFollowing.deleteYn.eq(DeleteYn.N)))
                ))
                .fetch();

        List<FixtureInDto.ResFixtureInfo> content = queryFactory
                .select(Projections.fields(
                        FixtureInDto.ResFixtureInfo.class,
                        fixtures.id.as("fixtureId"),
                        fixtures.homeTeam.id.as("homeTeamId"),
                        fixtures.awayTeam.id.as("awayTeamId"),
                        ExpressionUtils.as(JPAExpressions.select(teams.teamName).from(teams).where(teams.id.eq(fixtures.homeTeam.id)),"homeTeamName"),
                        ExpressionUtils.as(JPAExpressions.select(teams.teamName).from(teams).where(teams.id.eq(fixtures.awayTeam.id)),"awayTeamName"),
                        fixtures.datetime.as("matchDateTimeOriginal"),
                        ExpressionUtils.as(JPAExpressions.select(teams.teamLogo).from(teams).where(teams.id.eq(fixtures.homeTeam.id)),"homeTeamLogo"),
                        ExpressionUtils.as(JPAExpressions.select(teams.teamLogo).from(teams).where(teams.id.eq(fixtures.awayTeam.id)),"awayTeamLogo"),
                        leagues.id.as("leagueId"),
                        leagues.leagueName.as("leagueName")))
                .from(fixtures)
                .where(fixtures.homeTeam.id.in(teamIds)
                        .or(fixtures.awayTeam.id.in(teamIds))
                        .and(fixtures.datetime.after(ZonedDateTime.now().toLocalDateTime())))
                .orderBy(fixtures.datetime.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        content.forEach(fixtureInfo -> {
            List<AlarmInDto.AlarmPlayerInfo> alarmPlayers = playerList(memberId, fixtureInfo.getFixtureId());
            fixtureInfo.setAlarmPlayers(alarmPlayers);
            fixtureInfo.setMatchDateTime(CommonUtils.toUTCStringFromDateTime(fixtureInfo.getMatchDateTimeOriginal()));
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
