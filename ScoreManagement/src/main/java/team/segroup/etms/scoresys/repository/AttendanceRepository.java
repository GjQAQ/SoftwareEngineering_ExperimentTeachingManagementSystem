package team.segroup.etms.scoresys.repository;

import org.springframework.data.repository.CrudRepository;
import team.segroup.etms.scoresys.entity.Attendance;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends CrudRepository<Attendance, Integer> {
    Optional<Attendance> findByAtid(Integer atid);

    List<Attendance> findByCourseCode(String courseCode);

    List<Attendance> findByOwnerNid(String nid);
}
