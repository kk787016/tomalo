package com.jjp.tomalo.service;

import com.jjp.tomalo.domain.User;
import com.jjp.tomalo.domain.oauth2.GoogleOAuth2UserInfo;
import com.jjp.tomalo.domain.oauth2.KakaoOAuth2UserInfo;
import com.jjp.tomalo.domain.oauth2.OAuth2UserInfo;
import com.jjp.tomalo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // 1. OAuth2 공급자로부터 사용자 정보 가져오기
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 2. 공급자별로 Oauth2UserInfo 객체 생성 (비즈니스 로직 분리)
        OAuth2UserInfo oAuth2UserInfo = getOAuth2UserInfo(userRequest, oAuth2User.getAttributes());

        // 3. DB에서 사용자를 조회하거나 새로 생성 (가입/로그인 처리)
        User user = findOrCreateUser(oAuth2UserInfo);

        // 4. UserPrincipal 객체 생성 및 반환 (가장 중요한 부분)
        // User 엔티티와 OAuth2 속성을 모두 담아서 생성
        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private OAuth2UserInfo getOAuth2UserInfo(OAuth2UserRequest userRequest, Map<String, Object> attributes) {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        if (registrationId.equals("google")) {
            log.info("구글 로그인 요청");
            return new GoogleOAuth2UserInfo(attributes);
        } else if (registrationId.equals("kakao")) {
            log.info("카카오 로그인 요청");
            return new KakaoOAuth2UserInfo(attributes);
        }

        throw new OAuth2AuthenticationException("지원하지 않는 소셜 로그인입니다: " + registrationId);
    }

    private User findOrCreateUser(OAuth2UserInfo oAuth2UserInfo) {
        Optional<User> userOptional = userRepository.findByProviderAndProviderId(
                oAuth2UserInfo.getProvider(), oAuth2UserInfo.getProviderId()
        );

        return userOptional.map(user -> {
            // 기존 회원인 경우: 이름, 이메일 등 정보가 변경되었을 수 있으므로 업데이트
            log.info("기존 회원입니다: {}", user.getEmail());
            return user.updateOAuth2Info(oAuth2UserInfo.getName(), oAuth2UserInfo.getEmail());

        }).orElseGet(() -> {
            // 신규 회원인 경우: 회원가입 처리
            log.info("신규 회원입니다. 자동 회원가입을 진행합니다: {}", oAuth2UserInfo.getEmail());

            User newUser = oAuth2UserInfo.toEntity();

            //Todo this.getProvider() + "_" + this.getProviderId()) 이거로 프로파일에 추가.

            return userRepository.save(newUser);
        });
    }
}
