package com.jjp.tomalo.repository;

import com.jjp.tomalo.domain.chat.ChatMessage;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {


    @EntityGraph(attributePaths = {"chatRoom"})
    List<ChatMessage> findByChatRoomIdOrderBySentAtAsc(Long chatRoomId);
}

