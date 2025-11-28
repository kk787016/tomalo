package com.jjp.tomalo.dto.match;

import com.jjp.tomalo.domain.profiles.IncomeRange;
import com.jjp.tomalo.domain.profiles.Profile;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.Period;

@Getter
@Builder
public class MatchPartnerResponseDto {

    private Long profileId;
    private String nickname;
    private int age;
    private String job;
    private String selfIntroduction;
    private String mbti;
    private int height;
    private IncomeRange income; // 프론트에서 Enum 처리가 필요할 수 있음
    private String education;
    private boolean isSmoker;
    private String religion;
    private String drinking;
    private String place;
    private double compatibilityScore;
    private String mainImageUrl;

    public static MatchPartnerResponseDto from(Profile profile, double score) {
        // 나이 계산
        int age = (profile.getBirthday() != null)
                ? Period.between(profile.getBirthday(), LocalDate.now()).getYears()
                : 0;

        // 메인 이미지 URL 추출
        String imageUrl = profile.getImages().isEmpty() ? "" : profile.getImages().get(0).getImageUrl();

        return MatchPartnerResponseDto.builder()
                .profileId(profile.getId())
                .nickname(profile.getNickname())
                .age(age)
                .job(profile.getJob())
                .selfIntroduction(profile.getSelfIntroduction())
                .mbti(profile.getMbti())
                .height(profile.getHeight())
                .income(profile.getIncome())
                .education(profile.getEducation())
                .isSmoker(profile.isSmoker())
                .religion(profile.getReligion())
                .drinking(profile.getDrinking())
                .place(profile.getPlace())
                .compatibilityScore(score)
                .mainImageUrl(imageUrl)
                .build();
    }
}