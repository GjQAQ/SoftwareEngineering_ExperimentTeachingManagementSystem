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

    @AllArgsConstructor
    public enum Status {
        NOT_STARTED("N"),
        GOING_ON("Y"),
        TERMINATED("C");

        private final String rep;
    }
}
