package team.segroup.etms.scoresys.repository;

import org.springframework.data.repository.CrudRepository;
import team.segroup.etms.scoresys.entity.Submission;
import team.segroup.etms.scoresys.entity.SubmissionKey;

import java.util.List;
import java.util.Optional;

public interface SubmissionRepository
    extends CrudRepository<Submission, SubmissionKey> {
    List<Submission> findByAssignmentCourseCode(String code);

    List<Submission> findByAsid(int asid);

    Optional<Submission> findByAsidAndNid(int asid, String nid);

    List<Submission> findByAsidAndStatus(int asid, Submission.Status status);
}
