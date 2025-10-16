package com.jjp.tomalo.repository;

import com.jjp.tomalo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByPhoneNumber(String phoneNumber);
    Optional<User> findByEmail(String email);
    Optional<User> findByProviderAndProviderId(String provider, String providerId);

    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByEmail(String email);
}
