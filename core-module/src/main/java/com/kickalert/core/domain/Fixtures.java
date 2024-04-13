package com.kickalert.core.domain;

import com.kickalert.core.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_team_id")
    private Teams homeTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "away_team_id")
    private Teams awayTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "league_id")
    private Leagues leagues;

    @Column(name = "home_team_lineup", length = 3000)
    private String homeTeamLineup;

    @Column(name = "away_team_lineup", length = 3000)
    private String awayTeamLineup;

}
