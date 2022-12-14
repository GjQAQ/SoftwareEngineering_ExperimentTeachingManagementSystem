package team.segroup.etms.scoresys.repository;

import org.springframework.data.repository.CrudRepository;
import team.segroup.etms.scoresys.entity.Assignment;

import java.util.List;
import java.util.Optional;

public interface AssignmentRepository extends CrudRepository<Assignment, Integer> {
    Optional<Assignment> findByAsid(int asid);

    List<Assignment> findByCourseCode(String code);

    List<Assignment> findByOwnerNid(String nid);
}
