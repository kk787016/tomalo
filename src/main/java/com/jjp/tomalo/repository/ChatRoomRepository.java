package com.jjp.tomalo.repository;

import com.jjp.tomalo.domain.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findByDailyMatchId(Long dailyMatchId);


    @Query("SELECT c FROM ChatRoom c " +
            "JOIN FETCH c.dailyMatch m " +
            "JOIN FETCH m.profileA pa " +
            "JOIN FETCH pa.user " +
            "JOIN FETCH m.profileB pb " +
            "JOIN FETCH pb.user " +
            "WHERE pa.user.id = :userId OR pb.user.id = :userId " +
            "ORDER BY c.lastMessageAt DESC")
    List<ChatRoom> findMyChatRooms(@Param("userId") Long userId);
}
