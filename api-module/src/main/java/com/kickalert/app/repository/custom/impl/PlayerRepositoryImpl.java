package com.kickalert.app.repository.custom.impl;

import com.kickalert.app.dto.internal.PlayerInDto;
import com.kickalert.app.repository.custom.PlayerRepositoryCustom;
import com.kickalert.app.util.RepositorySliceHelper;
import com.kickalert.core.customEnum.DeleteYn;
import com.kickalert.core.util.CommonUtils;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.List;

import static com.kickalert.core.domain.QAlarmHistory.alarmHistory;
import static com.kickalert.core.domain.QCountries.countries;
import static com.kickalert.core.domain.QFixtures.fixtures;
import static com.kickalert.core.domain.QPlayerFollowing.playerFollowing;
import static com.kickalert.core.domain.QPlayers.players;
import static com.kickalert.core.domain.QTeams.teams;

public class PlayerRepositoryImpl implements PlayerRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public PlayerRepositoryImpl(EntityManager entityManager){
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Slice<PlayerInDto.ResFollowIngPlayerInfo> followingPlayerList(Long memberId , Pageable pageable){
        List<PlayerInDto.ResFollowIngPlayerInfo> content = queryFactory
                .select(Projections.fields(
                        PlayerInDto.ResFollowIngPlayerInfo.class,
                        players.playerPhotoUrl,
                        players.playerName,
                        players.id.as("playerId"),
                        teams.id.as("teamId"),
                        teams.teamName,
                        teams.teamLogo,
                        countries.id.as("countryId"),
                        countries.countryName.as("country"),
                        countries.countryLogo,
                        ExpressionUtils.as(JPAExpressions.select(fixtures.datetime.min())
                                .from(fixtures)
                                .where(fixtures.homeTeam.id.eq(teams.id)
                                        .or(fixtures.awayTeam.id.eq(teams.id)))
                                .where(fixtures.datetime.after(LocalDateTime.now()))
                                .orderBy(fixtures.datetime.asc())
                                ,"nextMatchDateTimeOriginal"))
                ).from(teams)
                .join(players).on(teams.id.eq(players.team.id))
                .join(countries).on(players.country.id.eq(countries.id))
                .where(players.id.in(
                        JPAExpressions.select(playerFollowing.player.id)
                                .from(playerFollowing)
                                .where(playerFollowing.member.id.eq(memberId)
                                        .and(playerFollowing.deleteYn.eq(DeleteYn.N)))))
                .orderBy(players.playerName.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        content.forEach(playerInfo -> {
            playerInfo.setNextMatchDateTime(CommonUtils.toUTCStringFromDateTime(playerInfo.getNextMatchDateTimeOriginal()));
        });

        return RepositorySliceHelper.toSlice(content, pageable);
    }

    public Slice<PlayerInDto.ResSearchPlayerInfo> searchPlayerList(Long memberId, String searchKeyword, Pageable pageable){
        List<PlayerInDto.ResSearchPlayerInfo> content = queryFactory
                .select(Projections.constructor(
                        PlayerInDto.ResSearchPlayerInfo.class,
                        players.id,
                        players.playerName,
                        players.playerPhotoUrl,
                        ExpressionUtils.as(new CaseBuilder()
                                .when(JPAExpressions.selectOne()
                                        .from(playerFollowing)
                                        .where(playerFollowing.player.eq(players)
                                                .and(playerFollowing.member.id.eq(memberId))
                                                .and(playerFollowing.deleteYn.eq(DeleteYn.N)))
                                        .exists())
                                .then("Y")
                                .otherwise("N"),"flowYn")))
                .from(players)
                .where(players.playerName.contains(searchKeyword))
                .orderBy(players.playerName.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return RepositorySliceHelper.toSlice(content, pageable);
    }


    public List<PlayerInDto.ResMatchPlayerInfo> matchPlayerList(Long teamId, Long memberId, Long fixtureId){
        List<PlayerInDto.ResMatchPlayerInfo> content = queryFactory
                .select(Projections.constructor(
                        PlayerInDto.ResMatchPlayerInfo.class,
                        players.id.as("playerId"),
                        players.playerName,
                        players.playerPhotoUrl,
                        ExpressionUtils.as(JPAExpressions.select(alarmHistory.alarmType).from(alarmHistory)
                                .where(players.eq(alarmHistory.player)
                                        .and(alarmHistory.member.id.eq(memberId))
                                        .and(alarmHistory.fixture.id.eq(fixtureId))),"alarmType"),
                        ExpressionUtils.as(new CaseBuilder()
                                .when(JPAExpressions.selectOne()
                                        .from(playerFollowing)
                                        .where(playerFollowing.player.eq(players)
                                                .and(playerFollowing.member.id.eq(memberId))
                                                .and(playerFollowing.deleteYn.eq(DeleteYn.N)))
                                        .exists())
                                .then("Y")
                                .otherwise("N"),"flowYn"))
                ).from(teams)
                .join(players).on(teams.id.eq(players.team.id))
                .where(teams.id.eq(teamId))
                .fetch();

        return content;
    }
}
