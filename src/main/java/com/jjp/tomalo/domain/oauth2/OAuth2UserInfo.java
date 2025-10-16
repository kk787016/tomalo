package com.jjp.tomalo.domain.oauth2;

import com.jjp.tomalo.domain.User;

import java.util.Map;

public interface OAuth2UserInfo {
    Map<String, Object> getAttributes();
    String getProviderId();
    String getProvider();
    String getEmail();
    String getName();


    User toEntity();

}
