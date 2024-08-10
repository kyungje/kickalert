package com.kickalert.core.domain;

import com.kickalert.core.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "players")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Players extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_id")
    private Long id;

    @Column(name = "player_photo_url")
    private String playerPhotoUrl;

    @Column(name = "player_name")
    private String playerName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Teams team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Countries country;

    @Column(name = "api_id")
    private Integer apiId;

    @Builder
    public Players(Long id, String playerPhotoUrl, String playerName, Teams team, Countries country, Integer apiId) {
        this.id = id;
        this.playerPhotoUrl = playerPhotoUrl;
        this.playerName = playerName;
        this.team = team;
        this.country = country;
        this.apiId = apiId;
    }
}
