package com.ssafy.backend.domain.member.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode {

    EXIST_MEMBER_EMAIL(HttpStatus.CONFLICT, "이미 가입되어 있는 이메일입니다."),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "해당 회원을 찾을 수 없습니다."),
    NOT_MATCH_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    ALREADY_MEMBER_LOGOUT(HttpStatus.CONFLICT, "이미 로그아웃 된 회원입니다."),
    CURRENT_CHANGE_MATCH_PASSWORD(HttpStatus.BAD_REQUEST, "현재 비밀번호가 변경하려는 비밀번호와 같습니다."),
    PASSWORD_CONFIRMATION_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호 변경과 비밀번호 변경 확인이 같지 않습니다.");

    private final HttpStatus httpStatus;
    private final String errorMessage;
}
