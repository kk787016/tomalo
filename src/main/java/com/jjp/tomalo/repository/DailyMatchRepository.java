package com.jjp.tomalo.repository;

import com.jjp.tomalo.domain.match.DailyMatch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyMatchRepository extends JpaRepository<DailyMatch, Long> {
}
