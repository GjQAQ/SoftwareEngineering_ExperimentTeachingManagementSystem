package team.segroup.etms.scoresys.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;

@Entity
@IdClass(SubmissionKey.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Submission {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asid", insertable = false, updatable = false)
    private Assignment assignment;

    @Id
    private Integer asid;

    @Id
    private String nid;

    private Timestamp submitTime;

    private String message;

    @Enumerated(EnumType.STRING)
    private Status status;

    private int score;

    public enum Status {SUCCESS, GOING_ON, ABSENT}
}
