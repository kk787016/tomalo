package com.jjp.tomalo.domain.chat;


import com.jjp.tomalo.domain.match.DailyMatch;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_match_id")
    private DailyMatch dailyMatch;

    private String lastMessage;
    private LocalDateTime lastMessageAt;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public ChatRoom(DailyMatch dailyMatch) {
        this.dailyMatch = dailyMatch;
    }

    // 메시지가 올 때마다 방 정보를 업데이트하는 편의 메서드
    public void updateLastMessage(String message, LocalDateTime time) {
        this.lastMessage = message;
        this.lastMessageAt = time;
    }
}
