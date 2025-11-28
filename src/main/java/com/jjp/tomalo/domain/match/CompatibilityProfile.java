package com.jjp.tomalo.domain.match;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private LifePriority lifePriority;

    @Enumerated(EnumType.STRING)
    private WeekendStyle weekendStyle;

    @Enumerated(EnumType.STRING)
    private ConflictStyle conflictStyle;

    @Enumerated(EnumType.STRING)
    private LoveLanguage loveLanguage;

    @Builder
    public CompatibilityProfile(LifePriority lifePriority, WeekendStyle weekendStyle, ConflictStyle conflictStyle, LoveLanguage loveLanguage) {
        this.lifePriority = lifePriority;
        this.weekendStyle = weekendStyle;
        this.conflictStyle = conflictStyle;
        this.loveLanguage = loveLanguage;
    }

}
