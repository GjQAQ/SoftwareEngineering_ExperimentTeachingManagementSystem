package team.segroup.etms.coursesys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.segroup.etms.coursesys.dto.StudentDto;
import team.segroup.etms.coursesys.service.CourseService;

import java.util.HashMap;
import java.util.List;

import static team.segroup.etms.coursesys.utils.ControllerUtils.*;

@RestController
@RequestMapping("/students")
public class StudentController {
    private CourseService courseService;

    @GetMapping("/{code}/{nid}")
    public ResponseEntity<StudentDto> findStudent(
        @PathVariable("code") String code,
        @PathVariable("nid") String nid
    ) {
        StudentDto student = courseService.findStudentByNid(code, nid);
        return defaultResponse(student != null, student, ResponseEntity.status(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{code}")
    public ResponseEntity<StudentDto> addStudent(
        @PathVariable("code") String code,
        @RequestBody StudentDto studentDto
    ) {
        StudentDto student = courseService.addStudent(code, studentDto);
        return defaultBadRequest(student != null, student);
    }

    @PostMapping("/{code}/batch")
    public ResponseEntity<BatchRegistryResponse> addStudentBatch(
        @RequestPart("csv") MultipartFile csv,
        @PathVariable("code") String code
    ) {
        //TODO:implement
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @DeleteMapping("/{code}/{nid}")
    public ResponseEntity<String> removeStudent(
        @PathVariable("code") String code,
        @PathVariable("nid") String nid
    ) {
        boolean result = courseService.removeStudent(code, nid);
        return defaultBadRequest(result, "ok");
    }

    @Autowired
    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    private static class BatchRegistryResponse extends HashMap<String, List<String>> {
    }
}
