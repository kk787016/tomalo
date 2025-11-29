package com.jjp.tomalo.service;

import com.jjp.tomalo.domain.User;
import com.jjp.tomalo.domain.chat.ChatMessage;
import com.jjp.tomalo.domain.chat.ChatRoom;
import com.jjp.tomalo.domain.match.DailyMatch;
import com.jjp.tomalo.domain.profiles.Profile;
import com.jjp.tomalo.dto.chat.ChatMessageDto;
import com.jjp.tomalo.dto.chat.ChatMessageResponseDto;
import com.jjp.tomalo.dto.chat.ChatRoomListDto;
import com.jjp.tomalo.repository.ChatMessageRepository;
import com.jjp.tomalo.repository.ChatRoomRepository;
import com.jjp.tomalo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;


    @Transactional
    public void createChatRoom(DailyMatch match) {
        ChatRoom chatRoom = ChatRoom.builder()
                .dailyMatch(match)
                .build();
        chatRoomRepository.save(chatRoom);
    }

    @Transactional
    public ChatMessage saveMessage(ChatMessageDto messageDto) {
        ChatRoom chatRoom = chatRoomRepository.findById(messageDto.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다.")); // todo 예외처리

        User sender = userRepository.findById(messageDto.getSenderId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .content(messageDto.getContent())
                .type(ChatMessage.MessageType.TALK)
                .build();

        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);

        chatRoom.updateLastMessage(messageDto.getContent(), savedMessage.getSentAt());

        return savedMessage;
    }

    @Transactional
    public List<ChatMessageResponseDto> getMessages(Long roomId) {
        return chatMessageRepository.findByChatRoomIdOrderBySentAtAsc(roomId)
                .stream()
                .map(ChatMessageResponseDto::from)
                .collect(Collectors.toList());
    }
    @Transactional
    public List<ChatRoomListDto> getMyChatRooms(User user) {
        Long userId = user.getId();

        List<ChatRoom> chatRooms = chatRoomRepository.findMyChatRooms(userId);

        return chatRooms.stream().map(room -> {
            Profile partner = room.getDailyMatch().getProfileA().getUser().getId().equals(userId)
                    ? room.getDailyMatch().getProfileB()
                    : room.getDailyMatch().getProfileA();

            String imageUrl = partner.getImages().isEmpty() ? "" : partner.getImages().get(0).getImageUrl();

            return ChatRoomListDto.builder()
                    .roomId(room.getId())
                    .partnerNickname(partner.getNickname())
                    .partnerImageUrl(imageUrl)
                    .lastMessage(room.getLastMessage())
                    .lastMessageTime(room.getLastMessageAt())
                    .build();
        }).collect(Collectors.toList());
    }
}

