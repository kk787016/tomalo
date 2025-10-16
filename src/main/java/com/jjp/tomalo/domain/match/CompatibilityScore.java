package com.jjp.tomalo.domain.match;


import com.jjp.tomalo.domain.profiles.Profile;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(CompatibilityScore.CompatibilityScoreId.class)
public class CompatibilityScore {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_a_id")
    private Profile profileA;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_b_id")
    private Profile profileB;

    @Column(nullable = false)
    private double score;

    private LocalDateTime lastCalculatedAt;

    public static class CompatibilityScoreId implements Serializable {
        private Long profileA;
        private Long profileB;
    }
}
