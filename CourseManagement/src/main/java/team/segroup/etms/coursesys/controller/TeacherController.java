package team.segroup.etms.coursesys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.segroup.etms.coursesys.dto.TeacherDto;
import team.segroup.etms.coursesys.entity.Teacher;
import team.segroup.etms.coursesys.service.CourseService;

import static team.segroup.etms.coursesys.utils.ControllerUtils.*;

@RestController
@RequestMapping("/teachers")
public class TeacherController {
    private CourseService courseService;

    @PostMapping("/{code}")
    public ResponseEntity<TeacherDto> addTeacher(
        @PathVariable("code") String code,
        @RequestBody TeacherDto teacherDto
    ) {
        TeacherDto teacher = courseService.addTeacher(code, teacherDto);
        return defaultBadRequest(teacher != null, teacher);
    }

    @PatchMapping("/{code}/{nid}")
    public ResponseEntity<TeacherDto> changeTeacherRole(
        @PathVariable("code") String code,
        @PathVariable("nid") String nid,
        @RequestParam("role") Teacher.Role role
    ) {
        TeacherDto teacher = courseService.changeRole(code, nid, role);
        return defaultBadRequest(teacher != null, teacher);
    }

    @DeleteMapping("/{code}/{nid}")
    public ResponseEntity<String> removeTeacher(
        @PathVariable("code") String code,
        @PathVariable("nid") String nid
    ) {
        boolean result = courseService.removeTeacher(code, nid);
        return defaultBadRequest(result, "ok");
    }

    @Autowired
    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }
}
