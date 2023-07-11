package schedully.schedully.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import schedully.schedully.domain.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}