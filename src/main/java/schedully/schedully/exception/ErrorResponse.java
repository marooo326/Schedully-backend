package schedully.schedully.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@Builder
@RequiredArgsConstructor
public class ErrorResponse {
    private final ErrorCode errorCode;
    private final String message;
    private final String cause;
}
