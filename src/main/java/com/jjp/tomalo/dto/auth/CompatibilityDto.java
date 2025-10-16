package com.jjp.tomalo.dto.auth;

import com.jjp.tomalo.domain.match.ConflictStyle;
import com.jjp.tomalo.domain.match.LifePriority;
import com.jjp.tomalo.domain.match.LoveLanguage;
import com.jjp.tomalo.domain.match.WeekendStyle;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CompatibilityDto {

    @NotNull(message = "인생 우선순위는 필수 선택 항목입니다.")
    private LifePriority lifePriority;

    @NotNull(message = "주말 스타일은 필수 선택 항목입니다.")
    private WeekendStyle weekendStyle;

    @NotNull(message = "갈등 해결 방식은 필수 선택 항목입니다.")
    private ConflictStyle conflictStyle;

    @NotNull(message = "애정 표현 방식은 필수 선택 항목입니다.")
    private LoveLanguage loveLanguage;
}
