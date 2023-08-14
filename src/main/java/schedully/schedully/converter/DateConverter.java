package schedully.schedully.converter;

import org.springframework.stereotype.Component;
import schedully.schedully.controller.dto.DateRequestDto;
import schedully.schedully.domain.Date;

@Component
public class DateConverter {
    public static Date toDate(DateRequestDto.DateDto dateDto) {
        return Date.builder()
                .date(dateDto.getDate())
                .morningAvailable(dateDto.isMorningAvailable())
                .afternoonAvailable(dateDto.isAfternoonAvailable())
                .build();
    }
}
