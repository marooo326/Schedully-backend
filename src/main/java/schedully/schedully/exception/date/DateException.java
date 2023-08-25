package schedully.schedully.exception.date;

import schedully.schedully.exception.CustomException;
import schedully.schedully.exception.ErrorCode;

public class DateException extends CustomException {
    public DateException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public DateException(ErrorCode errorCode) {
        super(errorCode);
    }
}
