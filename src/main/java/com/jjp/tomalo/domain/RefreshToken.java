package com.jjp.tomalo.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User 엔티티와 직접적인 관계(Foreign Key)를 맺는 대신, User의 ID만 저장하여 결합도를 낮춤
    @Column(nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false)
    private String tokenValue;

    public RefreshToken(Long userId, String tokenValue) {
        this.userId = userId;
        this.tokenValue = tokenValue;
    }

    // 토큰 값을 갱신하는 비즈니스 메서드
    public void updateToken(String newTokenValue) {
        this.tokenValue = newTokenValue;
    }
}
