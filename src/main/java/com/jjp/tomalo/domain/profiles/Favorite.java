package com.jjp.tomalo.domain.profiles;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private FavoriteCategory category;

    @OneToMany(mappedBy = "favorite")
    private List<ProfileFavorite> profileFavorites = new ArrayList<>();

    public Favorite(String name, FavoriteCategory category) {
        this.name = name;
        this.category = category;
    }
}
