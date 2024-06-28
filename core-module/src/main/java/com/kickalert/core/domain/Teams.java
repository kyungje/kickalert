package com.kickalert.core.domain;

import com.kickalert.core.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "teams")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Teams extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;

    @Column(name = "team_name")
    private String teamName;

    @Column(name = "team_logo")
    private String teamLogo;

    @Column(name = "api_id")
    private Integer apiId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Countries country;

    @Builder
    public Teams(Long id, String teamName, String teamLogo, Integer apiId, Countries country) {
        this.id = id;
        this.teamName = teamName;
        this.teamLogo = teamLogo;
        this.apiId = apiId;
        this.country = country;
    }
}
