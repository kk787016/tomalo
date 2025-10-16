package com.jjp.tomalo.domain.match;


import com.jjp.tomalo.domain.profiles.Profile;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompatibilityProfile {
    @Id
    private Long id; // Profile의 ID와 동일한 값을 사용 (Shared Primary Key)

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // Profile의 ID를 이 엔티티의 ID로 매핑
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @Enumerated(EnumType.STRING)
    private LifePriority lifePriority;

    @Enumerated(EnumType.STRING)
    private WeekendStyle weekendStyle;

    @Enumerated(EnumType.STRING)
    private ConflictStyle conflictStyle;

    @Enumerated(EnumType.STRING)
    private LoveLanguage loveLanguage;

    @Builder
    public CompatibilityProfile(Profile profile, LifePriority lifePriority, WeekendStyle weekendStyle, ConflictStyle conflictStyle, LoveLanguage loveLanguage) {
        this.profile = profile;
        this.lifePriority = lifePriority;
        this.weekendStyle = weekendStyle;
        this.conflictStyle = conflictStyle;
        this.loveLanguage = loveLanguage;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
