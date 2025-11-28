package com.jjp.tomalo.repository;

import com.jjp.tomalo.domain.profiles.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    // userId로 프로필을 찾는 메소드
    Optional<Profile> findByUserId(Long userId);


    @Query("SELECT p FROM Profile p " +
            "LEFT JOIN FETCH p.images " + // images 컬렉션을 함께 가져오도록 명시
            "WHERE p.user.id = :userId")
    Optional<Profile> findProfileWithImagesByUserId(@Param("userId") Long userId);

    boolean existsByUserId(Long userId);

    @Query("SELECT DISTINCT p FROM Profile p " +
            "LEFT JOIN FETCH p.compatibilityProfile " + // 가치관 정보 함께 로딩
            "LEFT JOIN FETCH p.idealTypeFilter " +      // 이상형 필터 함께 로딩
            "WHERE p.opt = :status")
    List<Profile> findAllByOptStatus(@Param("status") boolean status);
}