package schedully.schedully.domain;

import jakarta.persistence.*;
import lombok.*;
import schedully.schedully.controller.dto.ScheduleRequestDto;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "TB_SCHEDULE")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String title;

    @Column
    private String explanation;

    @Column
    private String password;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.REMOVE)
    private List<Member> members;

    public void addMember(Member member) {
        this.members.add(member);
    }

    public void updateInfo(ScheduleRequestDto.ScheduleDto info) {
        this.title = info.getTitle();
        this.explanation = info.getExplanation();
        this.password = info.getPassword();
        this.startDate = info.getStartDate();
        this.endDate = info.getEndDate();
    }
}
