package com.kickalert.core.domain;

import com.kickalert.core.customEnum.DeleteYn;
import com.kickalert.core.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "player_following")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PlayerFollowing extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_following_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Members member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private Players player;

    @Enumerated(EnumType.STRING)
    @Column(name = "delete_yn")
    private DeleteYn deleteYn;

    public void changeDeleteYn(DeleteYn deleteYn) {
        this.deleteYn = deleteYn;
    }

    @Builder
    public PlayerFollowing(Members member, Players player, DeleteYn deleteYn) {
        this.member = member;
        this.player = player;
        this.deleteYn = deleteYn;
    }
}
