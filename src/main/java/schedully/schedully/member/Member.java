package schedully.schedully.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import schedully.schedully.date.Date;
import schedully.schedully.schedule.Schedule;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TB_MEMBER")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Enum role;

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private String password;

    @OneToMany(mappedBy = "member")
    private List<Date> availableDates = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    public void updateSchedule(Schedule schedule) {
        this.schedule = schedule;
        schedule.addMember(this);
    }
}
