package schedully.schedully.schedule.member;

import lombok.*;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MemberDTO {
    @NotNull
    private String name;

    @NotNull
    private String password;
}
