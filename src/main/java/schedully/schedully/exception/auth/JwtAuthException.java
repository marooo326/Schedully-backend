package schedully.schedully.exception.auth;

import schedully.schedully.exception.CustomException;
import schedully.schedully.exception.ErrorCode;

public class JwtAuthException extends CustomException {
    public JwtAuthException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public JwtAuthException(ErrorCode errorCode) {
        super(errorCode);
    }
}
