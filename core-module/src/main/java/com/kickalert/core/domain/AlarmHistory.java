package com.kickalert.core.domain;

import com.kickalert.core.customEnum.DeleteYn;
import com.kickalert.core.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "alarm_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AlarmHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_history_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Members member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Players player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_team_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Teams homeTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "away_team_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Teams awayTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fixture_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Fixtures fixture;

    @Column(name = "alarm_type")
    private String alarmType;

    @Column(name = "alarm_date_time")
    private LocalDateTime alarmDateTime;

    @Column(name = "match_date_time")
    private LocalDateTime matchDateTime;

    public void changeAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public void changeAlarmDateTime(LocalDateTime alarmDateTime) {
        this.alarmDateTime = alarmDateTime;
    }

    @Builder
    public AlarmHistory(Members member, Players player, Teams homeTeam, Teams awayTeam, Fixtures fixture, String alarmType, LocalDateTime alarmDateTime, LocalDateTime matchDateTime) {
        this.member = member;
        this.player = player;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.fixture = fixture;
        this.alarmType = alarmType;
        this.alarmDateTime = alarmDateTime;
        this.matchDateTime = matchDateTime;
    }
}
