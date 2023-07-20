package schedully.schedully.controller.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import schedully.schedully.domain.Member;
import schedully.schedully.domain.Role;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class SignUpRequestDTO {

    @NotNull
    private String username;

    @NotNull
   // @Pattern("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,30}$")
    private String password;

    @NotNull
    private String checkedPassword;

    private Role role;

    @Builder
    public Member toEntity(){
        return Member.builder()
                .username(username)
                .password(password)
                .role(Role.USER)
                .build();
    }
}
