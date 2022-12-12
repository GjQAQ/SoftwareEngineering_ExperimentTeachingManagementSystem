package team.segroup.etms.scoresys.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.segroup.etms.scoresys.entity.Attendance;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceDto {
    private Integer atid;
    private String courseCode;
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String ownerNid;

    public AttendanceDto(Attendance attendance) {
        atid = attendance.getAtid();
        courseCode = attendance.getCourseCode();
        name = attendance.getName();
        startTime = attendance.getStartTime().toLocalDateTime();
        endTime = attendance.getEndTime().toLocalDateTime();
        ownerNid = attendance.getOwnerNid();
    }

    public Attendance toAttendance() {
        return new Attendance(
            null,
            courseCode,
            name,
            Timestamp.valueOf(startTime),
            Timestamp.valueOf(endTime),
            ownerNid
        );
    }
}
