package team.segroup.etms.coursesys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.segroup.etms.coursesys.dto.CourseDto;
import team.segroup.etms.coursesys.dto.StudentDto;
import team.segroup.etms.coursesys.dto.TeacherDto;
import team.segroup.etms.coursesys.entity.Course;
import team.segroup.etms.coursesys.entity.Student;
import team.segroup.etms.coursesys.entity.Teacher;
import team.segroup.etms.coursesys.repository.CourseRepository;
import team.segroup.etms.coursesys.repository.StudentRepository;
import team.segroup.etms.coursesys.repository.TeacherRepository;
import team.segroup.etms.coursesys.service.CourseService;

import java.sql.Date;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// TODO:针对无效的请求抛出warning
@Service
public class CourseServiceImpl implements CourseService {
    private CourseRepository courseRepository;
    private StudentRepository studentRepository;
    private TeacherRepository teacherRepository;

    @Override
    public CourseDto createCourse(CourseDto courseDto) {
        Course course = courseDto.toCourse();

        Optional<Course> prior = courseRepository.findByCode(course.getCode());
        if (prior.isPresent()) {
            return null;
        }

        Date startTime = course.getStartTime();
        Date endTime = course.getEndTime();
        Date today = Date.valueOf(LocalDate.now());
        if (today.compareTo(startTime) < 0) {
            course.setStatus(Course.Status.NOT_STARTED);
        } else if (today.compareTo(endTime) < 0) {
            course.setStatus(Course.Status.GOING_ON);
        } else {
            return null;
        }

        Course saved = courseRepository.save(course);
        return new CourseDto(saved);
    }

    @Override
    public boolean closeCourse(String code) {
        Optional<Course> course = courseRepository.findByCode(code);
        return course.map(c -> {
            c.setStatus(Course.Status.TERMINATED);
            courseRepository.save(c);
            return true;
        }).orElse(false);
    }

    @Override
    public CourseDto modifyCourse(CourseDto courseDto) {
        // TODO:check&只修改部分字段？
        return new CourseDto(courseRepository.save(courseDto.toCourse()));
    }

    @Override
    public CourseDto findCourseByCode(String code) {
        Optional<Course> course = courseRepository.findByCode(code);
        return course.map(CourseDto::new).orElse(null);
    }

    @Override
    public List<CourseDto> findStudentsCourses(String nid) {
        List<Student> students = studentRepository.findByNid(nid);
        return students.stream()
            .map(Student::getCourse)
            .map(CourseDto::new)
            .collect(Collectors.toList());
    }

    @Override
    public List<CourseDto> findTeachersCourses(String nid) {
        List<Teacher> teachers = teacherRepository.findByNid(nid);
        return teachers.stream()
            .map(Teacher::getCourse)
            .map(CourseDto::new)
            .collect(Collectors.toList());
    }

    @Override
    public CourseDto findCourseByName(String name) {
        Optional<Course> course = courseRepository.findByCname(name);
        return course.map(CourseDto::new).orElse(null);
    }

    @Override
    public Page<CourseDto> listCoursesInPage(int pageNum, int pageSize) {
        Page<Course> all = courseRepository.findAll(PageRequest.of(pageNum, pageSize));
        return all.map(CourseDto::new);
    }

    @Override
    public boolean deleteCourse(String code) {
        Optional<Course> courseOpt = courseRepository.findByCode(code);
        if (!courseOpt.isPresent()) {
            return false;
        }
        courseRepository.deleteById(courseOpt.get().getCid());
        return true;
    }

    @Override
    public StudentDto addStudent(String courseCode, StudentDto studentDto) {
        if (studentRepository.findByNidAndCourseCode(
            studentDto.getNid(), courseCode
        ).isPresent()) {
            return null;
        }

        Optional<Course> courseOpt = courseRepository.findByCode(courseCode);
        if (!courseOpt.isPresent()) {
            return null;
        }

        Student student = studentDto.toStudent();
        student.setCourse(courseOpt.get());
        return new StudentDto(studentRepository.save(student));
    }

    @Override
    public Pair<List<String>, List<String>> addStudentBatch(
        String courseCode,
        Stream<StudentDto> stream
    ) {
        Pair<List<String>, List<String>> res = Pair.of(
            new LinkedList<>(), new LinkedList<>()
        );
        stream.forEach(newStudent -> {
            StudentDto result = addStudent(courseCode, newStudent);
            if (result == null) {
                res.getSecond().add(newStudent.getNid());
            } else {
                res.getFirst().add(result.getNid());
            }
        });

        return res;
    }

    @Override
    public boolean removeStudent(String courseCode, String nid) {
        Optional<Student> studentOpt = studentRepository.findByNidAndCourseCode(nid, courseCode);
        if (!studentOpt.isPresent()) {
            return false;
        } else {
            studentRepository.delete(studentOpt.get());
            return true;
        }
    }

    @Override
    public StudentDto findStudentByNid(String courseCode, String nid) {
        Optional<Student> studentOpt = studentRepository.findByNidAndCourseCode(nid, courseCode);
        return studentOpt.map(StudentDto::new).orElse(null);
    }

    @Override
    public List<StudentDto> listAllStudents(String courseCode) {
        List<Student> students = studentRepository.findByCourseCode(courseCode);
        return students.stream()
            .map(StudentDto::new)
            .collect(Collectors.toList());
    }

    @Override
    public TeacherDto addTeacher(String courseCode, TeacherDto teacherDto) {
        if (teacherRepository.findByNidAndCourseCode(
            teacherDto.getNid(), courseCode
        ).isPresent()) {
            return null;
        }

        Optional<Course> courseOpt = courseRepository.findByCode(courseCode);
        if (!courseOpt.isPresent()) {
            return null;
        }

        Teacher teacher = teacherDto.toTeacher();
        teacher.setCourse(courseOpt.get());
        return new TeacherDto(teacherRepository.save(teacher));
    }

    @Override
    public TeacherDto changeRole(String courseCode, String nid, Teacher.Role newRole) {
        Optional<Teacher> teacherOpt = teacherRepository.findByNidAndCourseCode(nid, courseCode);
        if (!teacherOpt.isPresent()) {
            return null;
        }

        Teacher teacher = teacherOpt.get();
        teacher.setRole(newRole);
        return new TeacherDto(teacherRepository.save(teacher));
    }

    @Override
    public boolean removeTeacher(String courseCode, String nid) {
        Optional<Teacher> teacherOpt = teacherRepository.findByNidAndCourseCode(nid, courseCode);
        if (!teacherOpt.isPresent()) {
            return false;
        } else {
            teacherRepository.delete(teacherOpt.get());
            return true;
        }
    }

    @Override
    public TeacherDto findTeacherByNid(String courseCode, String nid) {
        Optional<Teacher> teacherOpt = teacherRepository.findByNidAndCourseCode(nid, courseCode);
        return teacherOpt.map(TeacherDto::new).orElse(null);
    }

    @Override
    public List<TeacherDto> listAllTeachers(String courseCode) {
        List<Teacher> teachers = teacherRepository.findByCourseCode(courseCode);
        return teachers.stream()
            .map(TeacherDto::new)
            .collect(Collectors.toList());
    }

    @Autowired
    public void setCourseRepository(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Autowired
    public void setStudentRepository(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Autowired
    public void setTeacherRepository(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }
}
