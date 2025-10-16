package com.jjp.tomalo.domain.oauth2;

import com.jjp.tomalo.domain.User;

import java.util.Map;

public class KakaoOAuth2UserInfo implements OAuth2UserInfo {


    private final Map<String, Object> attributes;
    private final Map<String, Object> kakaoAccount;
    private final Map<String, Object> profile;

    @SuppressWarnings("unchecked")
    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
        this.kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        this.profile = (Map<String, Object>) kakaoAccount.get("profile");
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getEmail() {
        return (String) kakaoAccount.get("email");
    }

    @Override
    public String getName() {
        return (String) kakaoAccount.get("name");

    }

    public String getPhoneNumber() {
        return (String) kakaoAccount.get("phone_number");
    }


    @Override
    public User toEntity() {
        return User.builder()
                .email(this.getEmail())
                .name(this.getName())
                .provider(this.getProvider())
                .providerId(this.getProviderId())
                .phoneNumber(this.getPhoneNumber()) // 카카오 휴대폰 번호 사용
                .privacyPolicyAgreed(true)
                .build();
    }

}
