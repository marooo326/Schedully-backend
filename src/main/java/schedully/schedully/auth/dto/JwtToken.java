package schedully.schedully.auth.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor (access = AccessLevel.PRIVATE)
public class JwtToken {
    private String grantType;
    private String accessToken;
    private String refreshToken;
}