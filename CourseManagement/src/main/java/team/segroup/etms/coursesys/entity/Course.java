package team.segroup.etms.coursesys.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cid;

    private String cname;

    private String code;

    private Date startTime;

    private Date endTime;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String description;

    private Integer attendanceWeight;

    @AllArgsConstructor
    public enum Status {
        NOT_STARTED, GOING_ON, TERMINATED
    }
}
