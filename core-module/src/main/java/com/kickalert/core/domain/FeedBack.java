package com.kickalert.core.domain;

import com.kickalert.core.customEnum.FeedBackLike;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "feedback")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FeedBack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Members member;

    @Enumerated(EnumType.STRING)
    @Column(name = "feedback_like")
    private FeedBackLike feedBackLike;

    @Column(name = "contents")
    private String contents;

    @Builder
    public FeedBack(Members member, FeedBackLike feedBackLike, String contents) {
        this.member = member;
        this.feedBackLike = feedBackLike;
        this.contents = contents;
    }
}
