package com.kickalert.core.domain;

import com.kickalert.core.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "leagues")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Leagues extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "league_id")
    private Long id;

    @Column(name = "league_name")
    private String leagueName;

    @Column(name = "league_logo")
    private String leagueLogo;

    @Column(name = "api_id")
    private Integer apiId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Countries country;

    @Builder
    public Leagues(Long id, String leagueName, String leagueLogo, Integer apiId, Countries country) {
        this.id = id;
        this.leagueName = leagueName;
        this.leagueLogo = leagueLogo;
        this.apiId = apiId;
        this.country = country;
    }
}
