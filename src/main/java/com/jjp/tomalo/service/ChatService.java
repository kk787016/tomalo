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


    @Transactional
    public void createChatRoom(DailyMatch match) {
        ChatRoom chatRoom = ChatRoom.builder()
                .dailyMatch(match)
                .build();
        chatRoomRepository.save(chatRoom);
    }

    @Transactional
    public ChatMessage saveMessage(ChatMessageDto messageDto) {
        // 채팅방 조회
        ChatRoom chatRoom = chatRoomRepository.findById(messageDto.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));

        // 메시지 엔티티 생성 및 저장
        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .senderId(messageDto.getSenderId())
                .content(messageDto.getContent())
                .type(ChatMessage.MessageType.TALK)
                .build();

        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);

        // 채팅방에 마지막 메시지 내용 업데이트 (목록 조회 성능용)
        chatRoom.updateLastMessage(messageDto.getContent(), LocalDateTime.now());

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

        // 1. 내가 포함된 모든 DailyMatch를 찾고 -> 연결된 ChatRoom을 가져옴
        // (ChatRoomRepository에 관련 쿼리 메서드가 필요함. 아래 참고)
        List<ChatRoom> chatRooms = chatRoomRepository.findMyChatRooms(userId);

        return chatRooms.stream().map(room -> {
            // 2. 상대방 찾기
            Profile myProfile = user.getProfile(); // Fetch Join 등으로 가져왔다고 가정
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

