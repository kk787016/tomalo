package com.jjp.tomalo.dto.profile;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MyProfileResponse {
    private final String nickname;
    private final String mainImageUrl;
    private final boolean isOptIn;
    private final int age;
    private final String location;

    @Builder
    public MyProfileResponse(String nickname, String mainImageUrl, boolean isOptIn, int age, String location) {
        this.nickname = nickname;
        this.mainImageUrl = mainImageUrl;
        this.isOptIn = isOptIn;
        this.age = age;
        this.location = location;
    }
}
