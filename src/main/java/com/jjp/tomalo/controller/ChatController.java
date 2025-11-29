package com.jjp.tomalo.controller;

import com.jjp.tomalo.domain.User;
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

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/chat/message")
    public void sendMessage(ChatMessageDto messageDto) {
        log.info("메시지 수신: room={}, sender={}, content={}",
                messageDto.getRoomId(), messageDto.getSenderId(), messageDto.getContent());

        ChatMessage savedMessage = chatService.saveMessage(messageDto);

        ChatMessageResponseDto response = ChatMessageResponseDto.from(savedMessage);

        messagingTemplate.convertAndSend("/sub/chat/room/" + messageDto.getRoomId(), response);

    }
    @GetMapping("/api/v1/chats/{roomId}/messages")
    public ResponseEntity<List<ChatMessageResponseDto>> getChatMessages(@PathVariable Long roomId) {
        return ResponseEntity.ok(chatService.getMessages(roomId));
    }

    @GetMapping("/api/v1/chats")
    public ResponseEntity<List<ChatRoomListDto>> getMyChatRooms(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        User user = userPrincipal.getUser();
        return ResponseEntity.ok(chatService.getMyChatRooms(user));
    }
}
