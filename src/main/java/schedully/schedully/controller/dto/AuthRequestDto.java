package schedully.schedully.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class AuthRequestDto {
    @Getter
    public static class SignUpDto {

        @NotBlank
        @Size(min=3, max = 12, message = "3자 이상 12자 이하의 이름을 입력해주세요.")
        private String username;

        @NotBlank
        @Pattern.List({
                @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z]).+", message = "영문 대소문자를 포함해주세요."),
                @Pattern(regexp = "^(?=.*[0-9]).+", message = "숫자를 포함해주세요."),
                @Pattern(regexp = "^(?=.*[-+_!@#\\$%^&*., ?]).+", message = "특수문자를 포함해주세요.")
        })
        private String password;

        @NotBlank
        private String checkedPassword;

    }

    @Getter
    public static class LonginDto {
        @NotBlank
        @Size(min=3, max = 12, message = "3자 이상 12자 이하의 이름을 입력해주세요.")
        private String username;

        @NotBlank
        private String password;
    }
}
