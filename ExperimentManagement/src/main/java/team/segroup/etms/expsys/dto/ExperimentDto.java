package team.segroup.etms.expsys.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.segroup.etms.expsys.entity.Experiment;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExperimentDto {
    private String courseCode;
    private String name;
    private String description;
    private String ownerNid;

    public ExperimentDto(Experiment experiment) {
        this.courseCode = experiment.getCode();
        this.name = experiment.getName();
        this.description = experiment.getDescription();
        this.ownerNid = experiment.getOwnerNid();
    }

    public Experiment toExperiment() {
        return new Experiment(
            null,
            courseCode,
            name,
            description,
            ownerNid
        );
    }
}
