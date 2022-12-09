package team.segroup.etms.coursesys.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.segroup.etms.coursesys.entity.Course;

import java.sql.Date;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto {
    private String name;
    private String code;
    private LocalDate startTime;
    private LocalDate endTime;
    private Course.Status status;
    private String description;

    public CourseDto(Course course) {
        this.name = course.getCname();
        this.code = course.getCode();
        this.startTime = course.getStartTime().toLocalDate();
        this.endTime = course.getEndTime().toLocalDate();
        this.status = course.getStatus();
        this.description=course.getDescription();
    }

    public Course toCourse() {
        return new Course(
            null,
            getName(),
            getCode(),
            Date.valueOf(getStartTime()),
            Date.valueOf(getEndTime()),
            getStatus(),
            getDescription()
        );
    }
}
