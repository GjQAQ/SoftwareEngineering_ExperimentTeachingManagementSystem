package team.segroup.etms.scoresys.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.segroup.etms.scoresys.entity.Assignment;
import team.segroup.etms.scoresys.entity.Attendance;
import team.segroup.etms.utils.LocalDateTimeSerializer;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentDto {
    private Integer asid;
    private String courseCode;
    private String name;
    private String description;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime endTime;
    private String ownerNid;

    public AssignmentDto(Assignment assignment) {
        asid = assignment.getAsid();
        courseCode = assignment.getCourseCode();
        name = assignment.getName();
        description = assignment.getDescription();
        endTime = assignment.getEndTime().toLocalDateTime();
        ownerNid = assignment.getOwnerNid();
    }

    public Assignment toAssignment() {
        return new Assignment(
            null,
            courseCode,
            name,
            description,
            Timestamp.valueOf(endTime),
            ownerNid
        );
    }
}
