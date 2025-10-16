package com.jjp.tomalo.domain;


import com.jjp.tomalo.domain.profiles.Profile;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(name= "uk_user_provider_provider_id", columnNames = {"provider", "provider_id"})})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 20)
    private String phoneNumber;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(nullable = false, length = 20)
    private String provider;

    private String providerId;

    @Column(length = 30)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.USER;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.ACTIVE;

    private LocalDateTime lastLoginAt;

    @Column(nullable = false)
    private boolean privacyPolicyAgreed;

    @Column(nullable = false)
    private boolean marketingEmailOptIn;

    @Column(nullable = false)
    private boolean marketingSmsOptIn;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Profile profile;

    @Builder
    public User(String phoneNumber, String email, String password,
                String provider, String providerId, String name,
                boolean privacyPolicyAgreed, boolean marketingEmailOptIn, boolean marketingSmsOptIn) {

        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.provider = provider;
        this.providerId = providerId;
        this.name = name;
        this.privacyPolicyAgreed = privacyPolicyAgreed;
        this.marketingEmailOptIn = marketingEmailOptIn;
        this.marketingSmsOptIn = marketingSmsOptIn;

    }


    public void setProfile(Profile profile) {
        if (this.profile != null) {
            this.profile.setUser(null);
        }
        this.profile = profile;
        if (profile != null) {
            profile.setUser(this);
        }
    }


    public void updateLastLogin(){
        this.lastLoginAt = LocalDateTime.now();
    }

    public User updateOAuth2Info(String name, String email) {
        this.name = name;
        this.email = email;
        return this;
    }


}
