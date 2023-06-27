package schedully.schedully.schedule;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import schedully.schedully.member.MemberDTO;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ScheduleDTO {

    @NotNull
    private String title;

    private String content;

    @NotNull
    private String password;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private MemberDTO author;
}
