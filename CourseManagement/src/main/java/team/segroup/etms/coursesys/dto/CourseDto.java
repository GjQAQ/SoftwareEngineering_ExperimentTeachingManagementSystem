package team.segroup.etms.coursesys.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.segroup.etms.coursesys.entity.Course;
import team.segroup.etms.utils.LocalDate2Timestamp;
import team.segroup.etms.utils.LocalDateSerializer;

import java.sql.Date;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto {
    private String name;
    private String code;
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate startTime;
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate endTime;
    private Course.Status status;
    private String description;
    private Integer attendanceWeight;

    public CourseDto(Course course) {
        this.name = course.getCname();
        this.code = course.getCode();
        this.startTime = course.getStartTime().toLocalDate();
        this.endTime = course.getEndTime().toLocalDate();
        this.status = course.getStatus();
        this.description=course.getDescription();
        attendanceWeight=course.getAttendanceWeight();
    }

    public Course toCourse() {
        return new Course(
            null,
            getName(),
            getCode(),
            Date.valueOf(getStartTime()),
            Date.valueOf(getEndTime()),
            getStatus(),
            getDescription(),
            getAttendanceWeight()
        );
    }
}
