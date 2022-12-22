package team.segroup.etms.scoresys.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;

@Entity
@IdClass(CheckoutKey.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Checkout {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "atid", insertable = false, updatable = false)
    private Attendance attendance;

    @Id
    private Integer atid;

    @Id
    private String nid;

    private Timestamp checkTime;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {SUCCESS, LATE, ABSENT, GOING_ON, NOT_START, FAILED}
}
