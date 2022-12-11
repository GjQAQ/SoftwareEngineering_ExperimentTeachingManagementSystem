package team.segroup.etms.expsys.repository;

import org.springframework.data.repository.CrudRepository;
import team.segroup.etms.expsys.entity.Experiment;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface ExperimentRepository extends CrudRepository<Experiment, Integer> {
    Optional<Experiment> findByName(String name);

    Stream<Experiment> findByCode(String code);

    Stream<Experiment> findByDescriptionContains(String keyword);
}
