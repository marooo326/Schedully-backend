package schedully.schedully.schedule;

import jakarta.persistence.*;
import lombok.*;
import schedully.schedully.member.Member;

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
    private String content;

    @Column
    private String password;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @OneToMany(mappedBy = "schedule")
    private List<Member> members;

    public void addMember(Member member) {
        this.members.add(member);
    }
}
