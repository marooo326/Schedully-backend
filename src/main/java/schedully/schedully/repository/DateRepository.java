package schedully.schedully.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import schedully.schedully.domain.Date;
import schedully.schedully.domain.Member;

import java.util.List;

public interface DateRepository extends JpaRepository<Date,Long> {
    @Query("SELECT m FROM Date AS m WHERE m.member.id=:memberId")
    List<Member> findByMemberJpql(@Param("memberId") Long id);
}
