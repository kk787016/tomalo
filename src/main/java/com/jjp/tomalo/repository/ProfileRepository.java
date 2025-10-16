package com.jjp.tomalo.repository;

import com.jjp.tomalo.domain.profiles.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    // userId로 프로필을 찾는 메소드
    Optional<Profile> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}