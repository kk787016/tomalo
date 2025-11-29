package com.jjp.tomalo.dto.chat;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatRoomListDto {
    private Long roomId;
    private String partnerNickname;
    private String partnerImageUrl;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
}