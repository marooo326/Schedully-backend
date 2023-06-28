package schedully.schedully.member;

import lombok.*;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class MemberDTO {
    @NotNull
    private String name;

    @NotNull
    private String password;
}
