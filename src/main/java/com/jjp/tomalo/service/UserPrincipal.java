package com.jjp.tomalo.service;

import com.jjp.tomalo.domain.User;
import com.jjp.tomalo.domain.UserStatus;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
public class UserPrincipal implements UserDetails, OAuth2User {

    private final User user;
    //private final Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> oauth2Attributes;


    // Constructor for standard login

    private UserPrincipal(User user) {
        this.user = user;
    }

    // Constructor for OAuth2 login
    private UserPrincipal(User user, Map<String, Object> oauth2Attributes) {
        this.user = user;
        this.oauth2Attributes = oauth2Attributes;
    }

    // Factory method for standard UserDetails creation
    public static UserPrincipal create(User user) {
        return new UserPrincipal(user);
    }

    // Factory method for OAuth2User creation
    public static UserPrincipal create(User user, Map<String, Object> attributes) {
        return new UserPrincipal(user, attributes);
    }



    // == UserDetails Methods ==
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getStatus() != UserStatus.DORMANT && user.getStatus() != UserStatus.WITHDRAWN;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getStatus() == UserStatus.ACTIVE;
    }

    // == OAuth2User Methods ==
    @Override
    public Map<String, Object> getAttributes() {
        return oauth2Attributes != null ? oauth2Attributes : Collections.emptyMap();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    @Override
    public String getName() {
        // OAuth2 standard for the primary key of the user
        return String.valueOf(user.getId());
    }
}
