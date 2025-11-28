package com.jjp.tomalo.repository;

import com.jjp.tomalo.domain.match.DailyMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface DailyMatchRepository extends JpaRepository<DailyMatch, Long> {

    // 오늘 날짜 + (A가 나 이거나 OR B가 나) 인 매칭 조회
    @Query("SELECT m FROM DailyMatch m " +
            "JOIN FETCH m.profileA " +
            "JOIN FETCH m.profileB " +
            "WHERE m.matchDate = :date " +
            "AND (m.profileA.user.id = :userId OR m.profileB.user.id = :userId)")
    Optional<DailyMatch> findTodayMatchByUserId(@Param("userId") Long userId, @Param("date") LocalDate date);
}