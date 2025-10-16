package com.jjp.tomalo.service;

import com.jjp.tomalo.domain.User;
import com.jjp.tomalo.dto.auth.LocalSignUpRequestDto;
import com.jjp.tomalo.dto.auth.LoginRequestDto;
import com.jjp.tomalo.dto.auth.TokenDto;
import com.jjp.tomalo.exception.EmailAlreadyExistsException;
import com.jjp.tomalo.exception.PhoneNumberAlreadyExistsException;
import com.jjp.tomalo.jwt.JwtTokenProvider;
import com.jjp.tomalo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    @Override
    @Transactional
    public TokenDto localSignUp(LocalSignUpRequestDto requestDto) {

        if (userRepository.existsByPhoneNumber(requestDto.getPhoneNumber())) {
            throw new PhoneNumberAlreadyExistsException("이미 가입된 전화번호입니다.");
        }
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new EmailAlreadyExistsException("이미 사용 중인 이메일입니다.");
        }
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        User newUser = User.builder()
                .phoneNumber(requestDto.getPhoneNumber())
                .email(requestDto.getEmail())
                .password(encodedPassword)
                .provider("local")
                .name(requestDto.getName())
                .privacyPolicyAgreed(requestDto.isPrivacyPolicyAgreed())
                .marketingEmailOptIn(requestDto.isMarketingEmailOptIn())
                .marketingSmsOptIn(requestDto.isMarketingSmsOptIn())
                .build();

//        //Todo 회원가입 시 닉네임 프로파일에 추가.
//        Profile newProfile = new Profile();
//        newUser.setProfile(newProfile);

        userRepository.save(newUser);

        // 회원가입 후 바로 로그인 처리하여 토큰 발급
        log.info("회원가입 성공, 자동 로그인 진행: {}", requestDto.getEmail());

        LoginRequestDto loginDto = new LoginRequestDto(requestDto.getEmail(), requestDto.getPassword());
        return login(loginDto);
    }

    @Override
    @Transactional
    public TokenDto login(LoginRequestDto requestDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword())
            );
            TokenDto tokenDto = jwtTokenProvider.generateTokenDto(authentication);

            // Refresh Token 저장 로직을 서비스에 위임
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            refreshTokenService.saveOrUpdateRefreshToken(userPrincipal.getUser().getId(), tokenDto.getRefreshToken());

            // 유저 최근 로그인 시간
            User user = userPrincipal.getUser();
            user.updateLastLogin();

            log.info("로그인 성공: {}", requestDto.getEmail());
            return tokenDto;

        } catch (AuthenticationException e) {
            // 5. 인증 실패 시 예외 처리
            log.warn("로그인 실패: {}", requestDto.getEmail());
            throw new BadCredentialsException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
    }

    @Override
    @Transactional
    public TokenDto reissueToken(String refreshTokenValue) {
        return refreshTokenService.reissueToken(refreshTokenValue);

    }
}
