package com.jjp.tomalo.domain.match;

import com.jjp.tomalo.domain.profiles.Profile;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyMatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_a_id", nullable = false)
    private Profile profileA;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_b_id", nullable = false)
    private Profile profileB;

    @Column(nullable = false)
    private double compatibilityScore;

    @CreatedDate // 엔티티 생성 시 날짜 자동 저장
    @Column(nullable = false)
    private LocalDate matchDate;

    @Enumerated(EnumType.STRING)
    private MatchStatus matchStatus;

    @Builder
    public DailyMatch(Profile profileA, Profile profileB, double compatibilityScore) {
        this.profileA = profileA;
        this.profileB = profileB;
        this.compatibilityScore = compatibilityScore;
        this.matchStatus = MatchStatus.PENDING;
    }
}
