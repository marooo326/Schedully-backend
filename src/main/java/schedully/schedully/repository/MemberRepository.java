package schedully.schedully.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import schedully.schedully.domain.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("SELECT m FROM Member AS m WHERE m.schedule.id=:scheduleId")
    List<Member> findByScheduleJpql(@Param("scheduleId") Long id);

    @Query("SELECT m FROM Member AS m WHERE m.username=:username AND m.schedule.id=:scheduleId")
    Member findByUsernameAndScheduleIdJpql(@Param("username") String username, @Param("scheduleId") Long scheduleId);

    @Query("SELECT m.schedule.id FROM Member m WHERE m.id=:memberId")
    Long findScheduleIdByIdJpql(@Param("memberId") Long id);
}

