package team.segroup.etms.scoresys.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.segroup.etms.scoresys.dto.AttendanceDto;
import team.segroup.etms.scoresys.entity.Checkout;
import team.segroup.etms.scoresys.service.AttendanceService;

import static team.segroup.etms.utils.ControllerUtils.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/attendances")
@Slf4j
public class AttendanceController {
    private AttendanceService attendanceService;

    @PostMapping
    public ResponseEntity<AttendanceDto> create(
        @RequestBody AttendanceDto attendanceDto
    ) {
        attendanceDto.setAtid(null);
        AttendanceDto attendance = attendanceService.create(attendanceDto);
        return defaultBadRequest(attendance != null, attendance);
    }

    @GetMapping(params = "courseCode")
    public ResponseEntity<List<AttendanceDto>> findByCourseCode(
        @RequestParam("courseCode") String courseCode
    ) {
        List<AttendanceDto> attendances = attendanceService.findByCourseCode(courseCode);
        return defaultNotFound(attendances.size() > 0, attendances);
    }

    @GetMapping(params = "owner")
    public ResponseEntity<List<AttendanceDto>> findByOwnerNid(
        @RequestParam("owner") String ownerNid
    ) {
        List<AttendanceDto> attendances = attendanceService.findByOwner(ownerNid);
        return defaultNotFound(attendances.size() > 0, attendances);
    }

    @GetMapping("/{atid}")
    public ResponseEntity<AttendanceDto> findByAtid(
        @PathVariable("atid") int atid
    ) {
        AttendanceDto dto = attendanceService.findByAtid(atid);
        return defaultNotFound(dto != null, dto);
    }

    @GetMapping("/{atid}/{nid}")
    public ResponseEntity<Checkout.Status> queryAttendanceStatus(
        @PathVariable("atid") int atid,
        @PathVariable("nid") String nid
    ) {
        Checkout.Status status = attendanceService.queryAttendanceStatus(atid, nid);
        return defaultNotFound(status != null, status);
    }

    @PatchMapping("/{atid}/{nid}")
    public ResponseEntity<Checkout.Status> signIn(
        @PathVariable("atid") int atid,
        @PathVariable("nid") String nid,
        @RequestParam("checkTime") long checkTime
    ) {
        Checkout.Status status = attendanceService.signIn(atid, nid, Instant.ofEpochMilli(checkTime));
        return defaultNotFound(status != null, status);
    }

    @PatchMapping("/{atid}")
    public ResponseEntity<AttendanceDto> modify(
        @RequestBody AttendanceDto attendanceDto
    ) {
        AttendanceDto result = attendanceService.modify(attendanceDto);
        return defaultBadRequest(result != null, result);
    }

    @DeleteMapping("/{atid}")
    public ResponseEntity<String> delete(
        @PathVariable("atid") int atid
    ) {
        boolean result = attendanceService.delete(atid);
        return defaultNotFound(result, "ok");
    }

    @Autowired
    public void setAttendanceService(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }
}
