package com.jjp.tomalo.dto.auth;

import com.jjp.tomalo.domain.profiles.AnimalFace;
import com.jjp.tomalo.domain.profiles.IncomeRange;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;


@Getter
public class IdealTypeDto {

    private AnimalFace idealAnimalFace;

    private IncomeRange minIncome;

    @Min(value = 140, message = "최소 키는 140cm 이상이어야 합니다.")
    private Integer minHeight;

    @Max(value = 220, message = "최대 키는 220cm 이하이어야 합니다.")
    private Integer maxHeight;
}
