package com.jjp.tomalo.dto.auth;

import com.jjp.tomalo.domain.profiles.AnimalFace;
import com.jjp.tomalo.domain.profiles.IncomeRange;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;


@Getter
public class IdealTypeDto {

    private AnimalFace idealAnimalFace;

    private Integer minAge;

    private Integer maxAge;

    private Integer maxDistance;

    private Boolean preferSmoker;

    private String religionPreference;

    private String drinkingPreference;
}
