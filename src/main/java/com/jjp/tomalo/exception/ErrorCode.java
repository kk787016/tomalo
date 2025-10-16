package com.jjp.tomalo.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // --- 인증 및 권한 관련 (A: Authentication/Authorization) ---
    INVALID_ARGUMENT("A400", "잘못된 인자 값입니다."),
    UNAUTHORIZED("A401", "인증 정보가 유효하지 않습니다."),
    USER_NOT_FOUND("A404", "해당 사용자를 찾을 수 없습니다."),

    // --- 회원가입 및 사용자 정보 관련 (U: User) ---
    EMAIL_ALREADY_EXISTS("U409", "이미 가입된 이메일입니다."),
    PHONE_NUMBER_ALREADY_EXISTS("U409", "이미 가입된 전화번호입니다."),

    // --- 서버 관련 관련 (S: Server) ---
    INTERNAL_SERVER_ERROR("S500", "서버 에러입니다");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
