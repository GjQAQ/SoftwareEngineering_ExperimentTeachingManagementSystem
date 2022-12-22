package team.segroup.etms.scoresys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.segroup.etms.scoresys.dto.AssignmentDto;
import team.segroup.etms.scoresys.dto.AttendanceDto;
import team.segroup.etms.scoresys.entity.Submission;
import team.segroup.etms.scoresys.service.AssignmentService;

import static team.segroup.etms.utils.ControllerUtils.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/assignments")
public class AssignmentController {
    private AssignmentService assignmentService;

    @PostMapping
    public ResponseEntity<AssignmentDto> create(
        @RequestParam("courseCode") String code,
        @RequestParam("name") String name,
        @RequestParam("description") String description,
        @RequestParam("end") LocalDateTime endTime,
        @RequestParam("owner") String ownerNid,
        @RequestBody Set<String> nids
    ) {
        AssignmentDto assignmentDto = new AssignmentDto(
            null, code, name, description, endTime, ownerNid
        );
        AssignmentDto result = assignmentService.create(assignmentDto, nids);
        return defaultBadRequest(result != null, result);
    }

    @GetMapping(params = "courseCode")
    public ResponseEntity<List<AssignmentDto>> findByCourseCode(
        @RequestParam("courseCode") String courseCode
    ) {
        List<AssignmentDto> result = assignmentService.findByCourseCode(courseCode);
        return defaultNotFound(result.size() > 0, result);
    }

    @GetMapping(params = "owner")
    public ResponseEntity<List<AssignmentDto>> findByOwner(
        @RequestParam("owner") String ownerNid
    ) {
        List<AssignmentDto> result = assignmentService.findByOwner(ownerNid);
        return defaultNotFound(result.size() > 0, result);
    }

    @GetMapping("/{asid}")
    public ResponseEntity<AssignmentDto> findByAsid(
        @PathVariable("asid") int asid
    ) {
        AssignmentDto dto = assignmentService.findByAsid(asid);
        return defaultNotFound(dto != null, dto);
    }

    @GetMapping("/{asid}/success")
    public ResponseEntity<List<String>> findSuccessList(
        @PathVariable("asid") int asid
    ) {
        List<String> result = assignmentService.findSuccessList(asid);
        return defaultNotFound(result != null, result);
    }

    @GetMapping("/{asid}/absent")
    public ResponseEntity<List<String>> findAbsentList(
        @PathVariable("asid") int asid
    ) {
        List<String> result = assignmentService.findAbsentList(asid);
        return defaultNotFound(result != null, result);
    }

    @GetMapping("/{asid}/participants")
    public ResponseEntity<List<String>> findParticipants(
        @PathVariable("asid") int asid
    ) {
        List<String> result = assignmentService.findParticipants(asid);
        return defaultNotFound(result != null, result);
    }

    @GetMapping("/{asid}/{nid}/status")
    public ResponseEntity<Submission.Status> querySubmissionStatus(
        @PathVariable("asid") int asid,
        @PathVariable("nid") String nid
    ) {
        Submission.Status status = assignmentService.querySubmissionStatus(asid, nid);
        return defaultNotFound(status != null, status);
    }

    @GetMapping("/{asid}/{nid}/content")
    public ResponseEntity<String> querySubmissionContent(
        @PathVariable("asid") int asid,
        @PathVariable("nid") String nid
    ) {
        String content = assignmentService.querySubmissionContent(asid, nid);
        return defaultNotFound(content != null, content);
    }

    @PostMapping("/{asid}/{nid}")
    public ResponseEntity<Submission.Status> submit(
        @PathVariable("asid") int asid,
        @PathVariable("nid") String nid,
        @RequestBody String content
    ) {
        Submission.Status status = assignmentService.submit(asid, nid, content);
        return defaultBadRequest(status != null, status);
    }

    @PatchMapping("/{asid}/{nid}")
    public ResponseEntity<Integer> grade(
        @PathVariable("asid") int asid,
        @PathVariable("nid") String nid,
        @RequestParam("score") int score
    ) {
        Integer grade = assignmentService.grade(asid, nid, score);
        return defaultBadRequest(grade != null, grade);
    }

    @PatchMapping("/{asid}")
    public ResponseEntity<AssignmentDto> modify(
        @RequestBody AssignmentDto assignmentDto
    ) {
        AssignmentDto modify = assignmentService.modify(assignmentDto);
        return defaultBadRequest(modify != null, modify);
    }

    @DeleteMapping("/{asid}")
    public ResponseEntity<String> delete(
        @PathVariable("asid") int asid
    ) {
        boolean result = assignmentService.delete(asid);
        return defaultNotFound(result, "ok");
    }

    @Autowired
    public void setAssignmentService(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }
}
