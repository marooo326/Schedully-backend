package schedully.schedully.exception.member;

import schedully.schedully.exception.CustomException;
import schedully.schedully.exception.ErrorCode;

public class MemberException extends CustomException {
    public MemberException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public MemberException(ErrorCode errorCode) {
        super(errorCode);
    }
}
