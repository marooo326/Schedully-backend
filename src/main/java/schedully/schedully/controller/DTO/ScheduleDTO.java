package schedully.schedully.controller.DTO;

import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
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
