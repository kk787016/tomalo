package com.jjp.tomalo.dto.auth;


import com.jjp.tomalo.domain.profiles.Gender;
import com.jjp.tomalo.domain.profiles.IncomeRange;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class ProfileCreateRequestDto {

    @NotBlank (message = "닉네임은 필수 입력 항목입니다.")
    @Size(min=2, max=10, message = "닉네임은 2자 이상 10자 이하로 입력해주세요.")
    private String nickname;


    @NotNull(message = "성별은 필수 선택 항목입니다.")
    private Gender gender;

    @NotNull(message = "생년월일은 필수 입력 항목입니다.")
    @Past(message = "생년월일은 현재 날짜보다 이전이어야 합니다.")
    private LocalDate birthday;

    @NotBlank(message = "거주지는 필수 입력 항목입니다.")
    private String place;

    @NotBlank(message = "직업은 필수 입력 항목입니다.")
    private String job;

    @Size(max = 200, message = "자기소개는 최대 200자까지 입력 가능합니다.")
    private String selfIntroduction;

    @Pattern(regexp = "^[E|I][N|S][T|F][J|P]$", message = "MBTI 형식이 올바르지 않습니다.")
    private String mbti;

    @NotNull(message = "키는 필수 입력 항목입니다.")
    @Min(value = 140, message = "키는 140cm 이상이어야 합니다.")
    @Max(value = 220, message = "키는 220cm 이하이어야 합니다.")
    private Integer height;

    @NotNull(message = "연봉 정보는 필수 선택 항목입니다.")
    private IncomeRange income;

    private String education;

    private List<String> profileFavorites;

    private List<String> imageUrls;


    @NotNull(message = "궁합 정보는 필수 입력 항목입니다.")
    @Valid // 중첩된 DTO의 유효성 검사를 활성화
    private CompatibilityDto compatibility;

    @Valid // 이상형 정보는 선택적으로 입력 가능하므로 @NotNull은 제외
    private IdealTypeDto idealType;

}