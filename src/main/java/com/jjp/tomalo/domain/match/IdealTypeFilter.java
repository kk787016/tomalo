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

    @Enumerated(EnumType.STRING)
    private IncomeRange minIncome; // 원하는 상대방의 최소 연봉 구간

    private Integer minHeight; // 원하는 상대방의 최소 키

    private Integer maxHeight; // 원하는 상대방의 최대 키

}
