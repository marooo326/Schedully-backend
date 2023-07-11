package schedully.schedully.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

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

    @Column(nullable = false)
    private String name;

    @Column
    private String password;

    @OneToMany(mappedBy = "member")
    private List<Date> availableDates = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    public void updateSchedule(Schedule schedule) {
        this.schedule = schedule;
        schedule.addMember(this);
    }

    public void addAvailableDate(Date date){
        this.availableDates.add(date);
    }
}
