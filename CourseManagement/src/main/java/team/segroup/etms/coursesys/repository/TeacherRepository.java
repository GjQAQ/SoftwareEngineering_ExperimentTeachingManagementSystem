package team.segroup.etms.coursesys.repository;

import org.springframework.data.repository.CrudRepository;
import team.segroup.etms.coursesys.entity.Teacher;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends CrudRepository<Teacher, Integer> {
    Optional<Teacher> findByNidAndCourseCode(String nid, String code);
    List<Teacher> findByNid(String nid);
    List<Teacher> findByCourseCode(String code);
}
