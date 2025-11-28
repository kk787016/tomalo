package com.jjp.tomalo.domain.match;


import com.jjp.tomalo.domain.profiles.AnimalFace;
import com.jjp.tomalo.domain.profiles.IncomeRange;
import com.jjp.tomalo.domain.profiles.Profile;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IdealTypeFilter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AnimalFace idealAnimalFace; // 원하는 상대방의 동물상

    private Integer minAge;

    private Integer maxAge;

    private Integer maxDistance; // 최대 거리

    private Boolean preferSmoker; // 상대방의 선호 여부

    private String religionPreference; // 종교

    private String drinkingPreference; // 음주

}
