package team.segroup.etms.coursesys.repository;

import org.springframework.data.repository.CrudRepository;
import team.segroup.etms.coursesys.entity.Student;

import java.util.Optional;

public interface StudentRepository extends CrudRepository<Student, Integer> {
    Optional<Student> findByNid(String nid);
    Optional<Student> findByNidAndCourseCode(String nid, String code);
}
