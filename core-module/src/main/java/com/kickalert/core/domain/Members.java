package com.kickalert.core.domain;

import com.kickalert.core.customEnum.DeleteYn;
import com.kickalert.core.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Members extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "fcm_token")
    private String fcmToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "delete_yn")
    private DeleteYn deleteYn;

    @Builder
    public Members(Long id, String fcmToken, DeleteYn deleteYn) {
        this.id = id;
        this.fcmToken = fcmToken;
        this.deleteYn = deleteYn;
    }
}
