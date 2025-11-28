package com.jjp.tomalo.domain.oauth2;

import com.jjp.tomalo.domain.User;

import java.util.Map;

public class GoogleOAuth2UserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }


    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public User toEntity() {
        return User.builder()
                .email(this.getEmail())
                .name(this.getName())
                .provider(this.getProvider())
                .providerId(this.getProviderId())
                .privacyPolicyAgreed(true)
                .build();
    }


}
