package com.jjp.tomalo.dto.chat;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatMessageDto {
    private Long roomId;    // 어느 방에 보내는지
    private Long senderId;  // 누가 보내는지
    private String content; // 내용
}