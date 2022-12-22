package team.segroup.etms.scoresys.service;

import team.segroup.etms.scoresys.dto.AttendanceDto;
import team.segroup.etms.scoresys.entity.Checkout;

import java.time.Instant;
import java.util.List;

public interface AttendanceService {
    AttendanceDto create(AttendanceDto attendanceDto);

    AttendanceDto findByAtid(int atid);

    List<AttendanceDto> findByCourseCode(String code);

    List<AttendanceDto> findByOwner(String nid);

    List<String> findSuccessList(int atid);

    List<String> findLateList(int atid);

    List<String> findAbsentList(int atid);

    List<String> findIndividualInvolved(int atid);

    Checkout.Status queryAttendanceStatus(int atid, String nid);

    Checkout.Status signIn(int atid, String nid, Instant checkTime);

    AttendanceDto modify(AttendanceDto attendanceDto);

    boolean delete(int atid);
}
