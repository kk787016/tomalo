package com.jjp.tomalo.domain.profiles;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileFavorite {  // 중간 엔티티. 다대다 사용하지 않으려고

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "favorite_id" )
    private Favorite favorite;

    public ProfileFavorite(Favorite favorite) {
        this.favorite = favorite;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
