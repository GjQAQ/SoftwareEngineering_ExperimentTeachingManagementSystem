package team.segroup.etms.coursesys.repository;

import org.springframework.data.repository.CrudRepository;
import team.segroup.etms.coursesys.entity.Course;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends CrudRepository<Course, Integer> {
    Optional<Course> findByCid(int cid);

    Optional<Course> findByCode(String code);

    Optional<Course> findByCname(String cname);
}
