package team.segroup.etms.scoresys.service;

import team.segroup.etms.scoresys.dto.AssignmentDto;
import team.segroup.etms.scoresys.entity.Submission;

import java.util.List;
import java.util.Set;

public interface AssignmentService {
    AssignmentDto create(AssignmentDto assignmentDto, Set<String> nids);

    AssignmentDto findByAsid(int asid);

    List<AssignmentDto> findByCourseCode(String code);

    List<AssignmentDto> findByOwner(String nid);

    List<String> findSuccessList(int asid);

    List<String> findAbsentList(int asid);

    List<String> findParticipants(int asid);

    Submission.Status querySubmissionStatus(int asid, String nid);

    String querySubmissionContent(int asid, String nid);

    Submission.Status submit(int asid, String nid, String content);

    Integer grade(int asid, String nid, int score);

    AssignmentDto modify(AssignmentDto assignmentDto);

    boolean delete(int asid);
}
