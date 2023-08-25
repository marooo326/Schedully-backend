package schedully.schedully.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    //Member
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "멤버가 존재하지 않습니다."),
    ROLE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 이름의 역할이 없습니다."),
    MEMBER_SCHEDULE_MISMATCH(HttpStatus.NOT_FOUND, "멤버와 스케줄이 일치하지 않습니다."),

    //Schedule
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 스케줄입니다."),

    //Date
    DATE_BAD_REQUEST(HttpStatus.NOT_FOUND, "날짜 형식이 올바르지 않습니다."),

    //Auth
    JWT_NOT_FOUND(HttpStatus.UNAUTHORIZED, "토큰이 존재하지 않습니다."),
    JWT_BAD_REQUEST(HttpStatus.UNAUTHORIZED, "잘못된 토큰입니다."),
    JWT_ALL_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    JWT_DENIED(HttpStatus.UNAUTHORIZED, "토큰 해독 중 오류가 발생하였습니다."),
    JWT_SERVER_ERROR(HttpStatus.UNAUTHORIZED, "JWT 에러가 발생했습니다."),

    //etc
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "문제가 생겼습니다. 잠시 후 다시 시도해주세요.");
    private final String message;
    private final HttpStatus status;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

}
