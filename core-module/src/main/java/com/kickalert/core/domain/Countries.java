package com.kickalert.core.domain;

import com.kickalert.core.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Entity
@Table(name = "countries")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Countries extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_id")
    private Long id;

    @Column(name = "country_name")
    private String countryName;

    @Column(name = "country_logo")
    private String countryLogo;

    @Column(name = "api_id")
    private Integer apiId;

    @Column(name = "fifa_name")
    private String fifaName;

    @Builder
    public Countries(Long id, String countryName, String countryLogo, Integer apiId, String fifaName) {
        this.id = id;
        this.countryName = countryName;
        this.countryLogo = countryLogo;
        this.apiId = apiId;
        this.fifaName = fifaName;
    }
}
