package team.segroup.etms.scoresys.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.segroup.etms.scoresys.dto.AttendanceDto;
import team.segroup.etms.scoresys.entity.Checkout;
import team.segroup.etms.scoresys.service.AttendanceService;

import static team.segroup.etms.utils.ControllerUtils.*;

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
        @RequestParam("courseCode") String courseCode,
        @RequestParam("name") String name,
        @RequestParam("start") LocalDateTime startTime,
        @RequestParam("end") LocalDateTime endTime,
        @RequestParam("owner") String ownerNid,
        @RequestBody Set<String> nids
    ) {
        //TODO:validate arguments
        AttendanceDto attendanceDto = new AttendanceDto(
            null, courseCode, name, startTime, endTime, ownerNid
        );
        AttendanceDto attendance = attendanceService.create(attendanceDto, nids);
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
        @PathVariable("atid")int atid
    ){
        AttendanceDto dto = attendanceService.findByAtid(atid);
        return defaultNotFound(dto!=null, dto);
    }

    @GetMapping("/{atid}/success")
    public ResponseEntity<List<String>> findSuccessList(
        @PathVariable("atid") int atid
    ) {
        List<String> successList = attendanceService.findSuccessList(atid);
        return defaultNotFound(successList != null, successList);
    }

    @GetMapping("/{atid}/late")
    public ResponseEntity<List<String>> findLateList(
        @PathVariable("atid") int atid
    ) {
        List<String> successList = attendanceService.findLateList(atid);
        return defaultNotFound(successList != null, successList);
    }

    @GetMapping("/{atid}/absent")
    public ResponseEntity<List<String>> findAbsentList(
        @PathVariable("atid") int atid
    ) {
        List<String> successList = attendanceService.findAbsentList(atid);
        return defaultNotFound(successList != null, successList);
    }

    @GetMapping("/{atid}/participants")
    public ResponseEntity<List<String>> findParticipantList(
        @PathVariable("atid") int atid
    ) {
        List<String> successList = attendanceService.findIndividualInvolved(atid);
        return defaultNotFound(successList != null, successList);
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
        @PathVariable("nid") String nid
    ) {
        Checkout.Status status = attendanceService.signIn(atid, nid);
        return defaultBadRequest(status != null, status);
    }


    @Autowired
    public void setAttendanceService(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }
}
