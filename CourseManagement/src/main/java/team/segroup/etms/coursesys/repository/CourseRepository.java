package team.segroup.etms.coursesys.repository;

import org.springframework.data.repository.CrudRepository;
import team.segroup.etms.coursesys.entity.Course;

public interface CourseRepository extends CrudRepository<Course, Integer> {
    Course findByCid(int cid);

    Course findByCode(String code);

    Course findByCname(String cname);
}
