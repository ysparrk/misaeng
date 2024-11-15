package dgp.misaeng.global.auth.exception;


import dgp.misaeng.global.exception.CustomException;
import dgp.misaeng.global.exception.ErrorCode;

public class TokenException extends CustomException {
    public TokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
