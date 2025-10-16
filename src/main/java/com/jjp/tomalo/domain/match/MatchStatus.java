package com.jjp.tomalo.domain.match;

public enum MatchStatus {
    PENDING,      // 매칭 성사 후 8시 소개팅 시작 전
    ACCEPTED,     // 양쪽 모두 수락
    REJECTED,     // 한쪽이라도 거절
    EXPIRED       // 시간이 지나 만료됨
}
