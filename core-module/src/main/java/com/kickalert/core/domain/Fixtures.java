package com.kickalert.core.domain;

import com.kickalert.core.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "fixtures")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Fixtures extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fixture_id")
    private Long id;

    @Column(name = "timezone")
    private String timezone;

    @Column(name = "datetime")
    private LocalDateTime datetime;

    @Column(name = "venue")
    private String venue;

    @Column(name = "api_id")
    private Integer apiId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_team_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Teams homeTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "away_team_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Teams awayTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "league_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Leagues leagues;

    @Builder
    public Fixtures(Long id, String timezone, LocalDateTime datetime, String venue, Integer apiId, Teams homeTeam, Teams awayTeam, Leagues leagues) {
        this.id = id;
        this.timezone = timezone;
        this.datetime = datetime;
        this.venue = venue;
        this.apiId = apiId;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.leagues = leagues;
    }
}
