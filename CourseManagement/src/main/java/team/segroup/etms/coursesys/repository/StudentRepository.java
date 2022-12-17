package team.segroup.etms.coursesys.repository;

import org.springframework.data.repository.CrudRepository;
import team.segroup.etms.coursesys.entity.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends CrudRepository<Student, Integer> {
    List<Student> findByNid(String nid);
    List<Student> findByCourseCode(String code);
    Optional<Student> findByNidAndCourseCode(String nid, String code);
}
