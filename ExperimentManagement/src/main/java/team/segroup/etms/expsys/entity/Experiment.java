package team.segroup.etms.expsys.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sun.xml.bind.v2.model.core.ID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.segroup.etms.utils.LocalDateTimeSerializer;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Experiment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer eid;

    private String code;

    private String name;

    private String description;

    private String ownerNid;

    private LocalDateTime startTime;

    private String equipments;
}
