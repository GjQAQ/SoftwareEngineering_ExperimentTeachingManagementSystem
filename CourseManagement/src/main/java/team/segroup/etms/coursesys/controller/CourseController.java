package team.segroup.etms.coursesys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.segroup.etms.coursesys.dto.CourseDto;
import team.segroup.etms.coursesys.service.CourseService;
import static team.segroup.etms.coursesys.utils.ControllerUtils.*;

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
        return defaultResponse(course!=null, course, ResponseEntity.status(HttpStatus.NOT_FOUND));
    }

//    @GetMapping("/{name}")
//    public ResponseEntity<CourseDto> findCourseByName(@PathVariable("name") String name) {
//        CourseDto course = courseService.findCourseByName(name);
//        return defaultResponse(course!=null, course, ResponseEntity.status(HttpStatus.NOT_FOUND));
//    }

    @Autowired
    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }
}
