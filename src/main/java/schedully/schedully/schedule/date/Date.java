package schedully.schedully.schedule.date;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import schedully.schedully.schedule.member.Member;
import schedully.schedully.schedule.Schedule;

import java.time.LocalDate;

@Entity
@Table(name = "TB_DATE")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Date {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private boolean morningAvailable;

    @Column(nullable = false)
    private boolean afternoonAvailable;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
