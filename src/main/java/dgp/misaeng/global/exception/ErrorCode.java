package dgp.misaeng.global.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {
    // 공통
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "잘못된 입력 값입니다."),
    MISSING_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C002", "인자가 부족합니다."),
    HANDLE_ACCESS_DENIED(HttpStatus.FORBIDDEN, "C003", "접근권한이 없습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C004", "사용할 수 없는 메서드입니다."),
    NO_SUCH_API(HttpStatus.BAD_REQUEST, "C005", "요청 주소가 올바르지 않습니다."),
    INVALID_PATH_VALUE(HttpStatus.BAD_REQUEST,"C006","요청이 잘못됐습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C007", "서버 에러"),

    // 인증 Auth
    NO_SUCH_MEMBER(HttpStatus.UNAUTHORIZED, "A001", "존재하지 않는 사용자입니다."),
    PASSWORD_NOT_MATCH(HttpStatus.UNAUTHORIZED, "A002", "비밀번호가 일치하지 않습니다."),
    UNAUTHENTICATED_MEMBER(HttpStatus.UNAUTHORIZED,"A003","인증되지 않은 사용자입니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "A004", "토큰이 만료되었습니다."),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "A005", "유효하지 않은 토큰입니다."),
    TOKEN_MALFORMED(HttpStatus.UNAUTHORIZED,"A006","잘못된 토큰 형식입니다."),

    // 유저 Member
    MEMBER_EMAIL_EXISTS(HttpStatus.BAD_REQUEST, "M001", "이미 존재하는 이메일입니다."),
    MEMBER_CONTACT_EXISTS(HttpStatus.BAD_REQUEST, "M002", "이미 등록된 번호입니다."),
    INVALID_EMAIL_FORMAT(HttpStatus.BAD_REQUEST, "M003", "이메일 제약조건에 맞지 않습니다."),
    INVALID_PASSWORD_FORMAT(HttpStatus.BAD_REQUEST, "M004", "비밀번호 제약조건에 맞지 않습니다."),
    INVALID_NAME_FORMAT(HttpStatus.BAD_REQUEST, "M005", "이름 제약조건에 맞지 않습니다."),
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "M007", "멤버가 존재하지 않습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
