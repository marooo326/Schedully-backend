package schedully.schedully.exception.schedule;

import schedully.schedully.exception.CustomException;
import schedully.schedully.exception.ErrorCode;

public class ScheduleException extends CustomException {
    public ScheduleException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public ScheduleException(ErrorCode errorCode) {
        super(errorCode);
    }
}
