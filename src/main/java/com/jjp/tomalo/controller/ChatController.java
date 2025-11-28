package com.jjp.tomalo.controller;

import com.jjp.tomalo.domain.chat.ChatMessage;
import com.jjp.tomalo.dto.chat.ChatMessageDto;
import com.jjp.tomalo.dto.chat.ChatMessageResponseDto;
import com.jjp.tomalo.dto.chat.ChatRoomListDto;
import com.jjp.tomalo.service.ChatService;
import com.jjp.tomalo.service.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/chat/message")
    public void sendMessage(ChatMessageDto messageDto) {
        log.info("메시지 수신: room={}, sender={}, content={}",
                messageDto.getRoomId(), messageDto.getSenderId(), messageDto.getContent());

        // 1. DB에 메시지 저장
        ChatMessage savedMessage = chatService.saveMessage(messageDto);

        // 2. 구독자(방에 있는 사람들)에게 메시지 전송
        // 경로: "/sub/chat/room/{roomId}"
        messagingTemplate.convertAndSend("/sub/chat/room/" + messageDto.getRoomId(), messageDto);
    }
    @GetMapping("/api/v1/chats/{roomId}/messages")
    public ResponseEntity<List<ChatMessageResponseDto>> getChatMessages(@PathVariable Long roomId) {
        return ResponseEntity.ok(chatService.getMessages(roomId));
    }

    @GetMapping("/api/v1/chats")
    public ResponseEntity<List<ChatRoomListDto>> getMyChatRooms(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(chatService.getMyChatRooms(userPrincipal.getUser()));
    }
}
