package schedully.schedully.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("SELECT m FROM Member AS m WHERE m.schedule.id=:scheduleId")
    List<Member> findByScheduleJpql(@Param("scheduleId") Long id);
}

