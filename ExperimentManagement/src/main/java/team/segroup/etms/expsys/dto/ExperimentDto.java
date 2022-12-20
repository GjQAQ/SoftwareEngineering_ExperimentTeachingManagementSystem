package team.segroup.etms.expsys.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.segroup.etms.expsys.entity.Experiment;
import team.segroup.etms.utils.LocalDateTimeSerializer;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExperimentDto {
    private Integer eid;
    private String courseCode;
    private String name;
    private String description;
    private String ownerNid;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime startTime;
    private String equipments;

    public ExperimentDto(Experiment experiment) {
        eid = experiment.getEid();
        courseCode = experiment.getCode();
        name = experiment.getName();
        description = experiment.getDescription();
        ownerNid = experiment.getOwnerNid();
        startTime=experiment.getStartTime();
        equipments=experiment.getEquipments();
    }

    public Experiment toExperiment() {
        return new Experiment(
            eid,
            courseCode,
            name,
            description,
            ownerNid,
            startTime,
            equipments
        );
    }
}
