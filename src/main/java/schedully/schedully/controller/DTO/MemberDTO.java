package schedully.schedully.controller.DTO;

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
