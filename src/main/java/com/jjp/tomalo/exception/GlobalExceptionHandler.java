package com.jjp.tomalo.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 잘못된 값 들어왔을 때  (400 BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity <ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("잘못된 인자 값 또는 권한 없음: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ErrorCode.INVALID_ARGUMENT.getCode(), ErrorCode.INVALID_ARGUMENT.getMessage()));

    }
    // 유저 찾을 수 없을 때 (404 NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e) {
        log.warn("사용자를 찾을 수 없음: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ErrorCode.USER_NOT_FOUND.getCode(), ErrorCode.USER_NOT_FOUND.getMessage()));
    }

    // 이메일 또는 전화번호 중복 가입 시 (409 Conflict)
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExistsException(RuntimeException e) {
        log.warn("회원가입 이메일 중복 에러: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT) // 409 상태 코드
                .body(new ErrorResponse(ErrorCode.EMAIL_ALREADY_EXISTS.getCode(), ErrorCode.EMAIL_ALREADY_EXISTS.getMessage()));
    }
    @ExceptionHandler(PhoneNumberAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handlePhoneNumberAlreadyExistsException(RuntimeException e) {
        log.warn("회원가입 전화번호 중복 에러: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT) // 409 상태 코드
                .body(new ErrorResponse(ErrorCode.PHONE_NUMBER_ALREADY_EXISTS.getCode(), ErrorCode.PHONE_NUMBER_ALREADY_EXISTS.getMessage()));
    }


    // 아이디/비밀번호 불일치 (401 Unauthorized)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException e) {
        log.warn("인증 실패: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED) // 401 상태 코드
                .body(new ErrorResponse(ErrorCode.UNAUTHORIZED.getCode(), ErrorCode.UNAUTHORIZED.getMessage()));
    }

    // 예상하지 못한 모든 예외를 처리 (500 Internal Server Error)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception e) {
        log.error("처리되지 않은 예외 발생", e); // 👈 error 레벨로, 스택 트레이스 전체를 기록
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR) // 500 상태 코드
                .body(new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR.getCode(), ErrorCode.INTERNAL_SERVER_ERROR.getMessage()));
    }
}
