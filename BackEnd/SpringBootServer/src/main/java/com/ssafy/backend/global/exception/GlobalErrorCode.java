package com.ssafy.backend.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GlobalErrorCode {
    REDIS_CONNECTION_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "Redis 연결에 실패했습니다.");

    private final HttpStatus httpStatus; // 에러 상황에 해당하는 HTTP 상태 코드
    private final String errorMessage; // 에러 상황을 설명하는 메시지
}
