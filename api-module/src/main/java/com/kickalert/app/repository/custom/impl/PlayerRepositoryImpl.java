package com.kickalert.app.repository.custom.impl;

import com.kickalert.app.dto.internal.PlayerInDto;
import com.kickalert.app.repository.custom.PlayerRepositoryCustom;
import com.kickalert.app.util.RepositorySliceHelper;
import com.kickalert.core.customEnum.DeleteYn;
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

import static com.kickalert.core.domain.QCountries.countries;
import static com.kickalert.core.domain.QFixtures.fixtures;
import static com.kickalert.core.domain.QLeagues.leagues;
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
                .select(Projections.constructor(
                        PlayerInDto.ResFollowIngPlayerInfo.class,
                        players.playerPhotoUrl,
                        players.playerName,
                        players.id.as("playerId"),
                        teams.id.as("teamId"),
                        teams.teamName,
                        teams.teamLogo,
                        teams.id.as("leagueId"),
                        ExpressionUtils.as(JPAExpressions.select(leagues.leagueName).from(leagues).where(teams.league.id.eq(leagues.id)),"leagueName"),
                        countries.id.as("countryId"),
                        countries.countryName.as("country"),
                        countries.countryLogo,
                        ExpressionUtils.as(JPAExpressions.select(fixtures.datetime)
                                .from(fixtures)
                                .where(fixtures.homeTeam.id.eq(teams.id)
                                        .or(fixtures.awayTeam.id.eq(teams.id)))
                                .where(fixtures.datetime.after(LocalDateTime.now()))
                                .orderBy(fixtures.datetime.asc())
                                .limit(1),"nextMatchDateTime"))
                ).from(teams)
                .join(players).on(teams.id.eq(players.team.id))
                .join(countries).on(players.country.id.eq(countries.id))
                .where(players.id.in(
                        JPAExpressions.select(playerFollowing.player.id)
                                .from(playerFollowing)
                                .where(playerFollowing.member.id.eq(memberId)
                                        .and(playerFollowing.deleteYn.eq(DeleteYn.N)))))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

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
                                        .where(playerFollowing.id.eq(players.id)
                                                .and(playerFollowing.member.id.eq(memberId))
                                                .and(playerFollowing.deleteYn.eq(DeleteYn.N)))
                                        .exists())
                                .then("Y")
                                .otherwise("N"),"flowYn")))
                .from(players)
                .where(players.playerName.contains(searchKeyword))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return RepositorySliceHelper.toSlice(content, pageable);
    }
}
