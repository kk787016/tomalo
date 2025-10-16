package com.jjp.tomalo.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LocalSignUpRequestDto {

    @NotBlank(message = "휴대폰 번호는 필수입니다.")
    @Pattern(regexp = "^010[0-9]{8}$", message = "휴대폰 번호 형식을 확인해주세요.")
    private String phoneNumber;

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식을 확인해주세요.")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    private String password;

    @NotBlank(message = "닉네임은 필수입니다.")
    @Size(min = 2, max = 10, message = "닉네임은 2자 이상 10자 이하로 입력해주세요.")
    private String nickname;

    private String name;

    private boolean privacyPolicyAgreed;

    private boolean marketingEmailOptIn;

    private boolean marketingSmsOptIn;
}
