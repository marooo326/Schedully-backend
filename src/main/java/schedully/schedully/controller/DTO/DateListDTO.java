package schedully.schedully.controller.DTO;

import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class DateListDTO {
    @NotNull
    private Long scheduleId;

    @NotNull
    private Long memberId;

    private List<LocalDate> dates = new ArrayList<>();

}
