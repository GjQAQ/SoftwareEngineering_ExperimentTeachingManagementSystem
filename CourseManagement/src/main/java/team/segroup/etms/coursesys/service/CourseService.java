package team.segroup.etms.coursesys.service;

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

    StudentDto addStudent(String courseCode, StudentDto studentDto);

    Pair<List<String>, List<String>> addStudentBatch(String courseCode, Stream<StudentDto> stream);

    boolean removeStudent(String courseCode, String nid);

    TeacherDto addTeacher(String courseCode, TeacherDto teacherDto, Teacher.Role role);

    TeacherDto changeRole(String courseCode, String nid, Teacher.Role newRole);

    boolean removeTeacher(String courseCode, String nid);
}