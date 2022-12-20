package team.segroup.etms.coursesys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.segroup.etms.coursesys.dto.CourseDto;
import team.segroup.etms.coursesys.dto.StudentDto;
import team.segroup.etms.coursesys.dto.TeacherDto;
import team.segroup.etms.coursesys.service.CourseService;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import static team.segroup.etms.utils.ControllerUtils.*;

@RestController
@RequestMapping("/courses")
public class CourseController {
    private CourseService courseService;

    @PostMapping
    public ResponseEntity<CourseDto> createCourse(@RequestBody CourseDto courseDto) {
        CourseDto course = courseService.createCourse(courseDto);
        return defaultBadRequest(course != null, course);
    }

    @PostMapping("/{code}/close")
    public ResponseEntity<String> closeCourse(@PathVariable("code") String code) {
        boolean result = courseService.closeCourse(code);
        return defaultBadRequest(result, "ok");
    }

    @PutMapping
    public ResponseEntity<CourseDto> modifyCourse(@RequestBody CourseDto courseDto) {
        CourseDto course = courseService.modifyCourse(courseDto);
        return defaultBadRequest(course != null, course);
    }

    @GetMapping("/{code}")
    public ResponseEntity<CourseDto> findCourseByCode(@PathVariable("code") String code) {
        CourseDto course = courseService.findCourseByCode(code);
        return defaultResponse(course != null, course, ResponseEntity.status(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{code}/{nid}")
    public ResponseEntity<Object> findRole(
        @PathVariable("code") String code,
        @PathVariable("nid") String nid
    ) {
        StudentDto student = courseService.findStudentByNid(code, nid);
        TeacherDto teacher = courseService.findTeacherByNid(code, nid);
        Object target = student == null ? teacher : student;
        return defaultNotFound(target != null, target);
    }

    @GetMapping("/student/{nid}")
    public ResponseEntity<List<CourseDto>> findStudentsCourses(
        @PathVariable("nid") String nid
    ) {
        List<CourseDto> courses = courseService.findStudentsCourses(nid);
        return defaultNotFound(courses.size() > 0, courses);
    }

    @GetMapping("/teacher/{nid}")
    public ResponseEntity<List<CourseDto>> findTeachersCourses(
        @PathVariable("nid") String nid
    ) {
        List<CourseDto> courses = courseService.findTeachersCourses(nid);
        return defaultNotFound(courses.size() > 0, courses);
    }

    @Autowired
    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }
}
