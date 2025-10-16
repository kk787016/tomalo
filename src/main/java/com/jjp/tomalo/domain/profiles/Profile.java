package com.jjp.tomalo.domain.profiles;

import com.jjp.tomalo.domain.User;
import com.jjp.tomalo.domain.match.CompatibilityProfile;
import com.jjp.tomalo.domain.match.IdealTypeFilter;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    private String place;   //  현재 위치의 도시 이름. 행정구역 (시) 단위.

    private String selfIntroduction;

    private String job;

    private Integer height;

    private String education;

    private String mbti;

    private boolean opt;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProfileImage> images = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProfileFavorite> profileFavorites = new ArrayList<>();


    @OneToOne(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private CompatibilityProfile compatibilityProfile;

    //for vip
    @OneToOne(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private IdealTypeFilter idealTypeFilter; // VIP용 이상형 필터 정보

    @Enumerated(EnumType.STRING)
    private IncomeRange income; // 연봉 정보

    @Enumerated(EnumType.STRING)
    private AnimalFace animalFace; // AI가 분석한 나의 동물상


    @Builder
    public Profile(String nickname, Gender gender, LocalDate birthday, String place,
                   String selfIntroduction, String job, String education, String mbti,
                   Integer height, IncomeRange income, User user) {
        this.nickname = nickname;
        this.gender = gender;
        this.birthday = birthday;
        this.place = place;
        this.selfIntroduction = selfIntroduction;
        this.job = job;
        this.education = education;
        this.mbti = mbti;
        this.height = height;
        this.income = income;
        this.user = user;
        this.opt = false;
    }


    public void setUser(User user) {
        this.user = user;
    }

    public void addImage(ProfileImage image) {
        this.images.add(image);
        image.setProfile(this);
    }

    public void addFavorite(ProfileFavorite profileFavorite) {
        this.profileFavorites.add(profileFavorite);
        profileFavorite.setProfile(this);
    }

    public void setCompatibilityProfile(CompatibilityProfile compatibilityProfile) {
        this.compatibilityProfile = compatibilityProfile;
        compatibilityProfile.setProfile(this); // 양방향 관계 설정
    }
    public void setIdealTypeFilter(IdealTypeFilter idealTypeFilter) {
        this.idealTypeFilter = idealTypeFilter;
        idealTypeFilter.setProfile(this); // 양방향 관계 설정
    }

    public void setOptIn(){
        this.opt = true;
    }
    public void setOptOut(){
        this.opt = false;
    }
}
