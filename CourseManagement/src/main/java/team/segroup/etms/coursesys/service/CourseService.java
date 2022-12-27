package team.segroup.etms.coursesys.service;

import org.springframework.data.domain.Page;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import team.segroup.etms.coursesys.dto.CourseDto;
import team.segroup.etms.coursesys.dto.StudentDto;
import team.segroup.etms.coursesys.dto.TeacherDto;
import team.segroup.etms.coursesys.entity.Teacher;

import java.util.List;
import java.util.stream.Stream;

@Service
public interface CourseService {
    CourseDto createCourse(CourseDto courseDto);

    boolean closeCourse(String code);

    CourseDto modifyCourse(CourseDto courseDto);

    CourseDto findCourseByCode(String code);

    CourseDto findCourseByName(String name);

    Page<CourseDto> listCoursesInPage(int pageNum, int pageSize);

    boolean deleteCourse(String code);

    StudentDto addStudent(String courseCode, StudentDto studentDto);

    Pair<List<String>, List<String>> addStudentBatch(String courseCode, Stream<StudentDto> stream);

    boolean removeStudent(String courseCode, String nid);

    StudentDto findStudentByNid(String courseCode, String nid);

    List<StudentDto> listAllStudents(String courseCode);

    List<CourseDto> findStudentsCourses(String nid);

    TeacherDto addTeacher(String courseCode, TeacherDto teacherDto);

    TeacherDto changeRole(String courseCode, String nid, Teacher.Role newRole);

    boolean removeTeacher(String courseCode, String nid);

    TeacherDto findTeacherByNid(String courseCode, String nid);

    List<CourseDto> findTeachersCourses(String nid);

    List<TeacherDto> listAllTeachers(String courseCode);
}
