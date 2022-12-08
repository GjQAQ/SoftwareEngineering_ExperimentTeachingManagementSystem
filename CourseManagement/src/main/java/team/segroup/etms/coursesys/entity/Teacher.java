package team.segroup.etms.coursesys.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Teacher {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cid")
    private Course course;

    private String nid;

    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    @AllArgsConstructor
    public enum Role {
        ASSISTANT("TA"),
        CHARGING_TEACHER("RT"),
        TEACHER("T");

        private final String repr;
    }
}
