package com.jjp.tomalo.repository;

import com.jjp.tomalo.domain.profiles.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Optional<Favorite> findByName(String name);
    List<Favorite> findAllByNameIn(List<String> names);

}
