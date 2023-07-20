package schedully.schedully.controller.DTO;

import lombok.*;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class LoginRequestDTO {
    @NotNull
    private String username;

    @NotNull
    private String password;
}
