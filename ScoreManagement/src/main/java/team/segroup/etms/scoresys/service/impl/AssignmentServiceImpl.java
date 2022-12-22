package team.segroup.etms.scoresys.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import team.segroup.etms.scoresys.dto.AssignmentDto;
import team.segroup.etms.scoresys.entity.Assignment;
import team.segroup.etms.scoresys.entity.Submission;
import team.segroup.etms.scoresys.repository.AssignmentRepository;
import team.segroup.etms.scoresys.repository.SubmissionRepository;
import team.segroup.etms.scoresys.service.AssignmentService;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AssignmentServiceImpl implements AssignmentService {
    private AssignmentRepository assignmentRepository;
    private SubmissionRepository submissionRepository;

    private final DelayQueue<CheckSubmissionTask> checkers = new DelayQueue<>();

    @PostConstruct
    public void init() {
        Executors.newSingleThreadExecutor().execute(new Thread(() -> {
            while (true) {
                try {
                    checkers.take().check();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    log.error("Assignment checker fails.");
                }
            }
        }));
    }

    @Override
    public AssignmentDto create(
        AssignmentDto assignmentDto,
        Set<String> nids
    ) {
        assignmentDto.setAsid(null);

        Assignment result = assignmentRepository.save(assignmentDto.toAssignment());
        long duration = result.getEndTime().getTime() - System.currentTimeMillis();
        checkers.put(new CheckSubmissionTask(
            duration, System.currentTimeMillis(), result.getAsid()
        ));
        log.info("Schedule task set(asid=" + result.getAsid() + ")");

        for (String nid : nids) {
            submissionRepository.save(new Submission(
                null,
                result.getAsid(),
                nid,
                Timestamp.from(Instant.now()),
                "",
                Submission.Status.GOING_ON,
                0
            ));
        }
        return new AssignmentDto(result);
    }

    @Override
    public AssignmentDto findByAsid(int asid) {
        return assignmentRepository.findByAsid(asid)
            .map(AssignmentDto::new)
            .orElse(null);
    }

    @Override
    public List<AssignmentDto> findByCourseCode(String code) {
        return assignmentRepository.findByCourseCode(code)
            .stream()
            .map(AssignmentDto::new)
            .collect(Collectors.toList());
    }

    @Override
    public List<AssignmentDto> findByOwner(String nid) {
        return assignmentRepository.findByOwnerNid(nid)
            .stream()
            .map(AssignmentDto::new)
            .collect(Collectors.toList());
    }

    @Override
    public List<String> findSuccessList(int asid) {
        return findListByStatus(asid, Submission.Status.SUCCESS);
    }

    @Override
    public List<String> findAbsentList(int asid) {
        return findListByStatus(asid, Submission.Status.ABSENT);
    }

    @Override
    public List<String> findParticipants(int asid) {
        return findListByStatus(asid, null);
    }

    @Override
    public Submission.Status querySubmissionStatus(int asid, String nid) {
        return submissionRepository.findByAsidAndNid(asid, nid)
            .map(Submission::getStatus)
            .orElse(null);
    }

    @Override
    public String querySubmissionContent(int asid, String nid) {
        return submissionRepository.findByAsidAndNid(asid, nid)
            .map(Submission::getMessage)
            .orElse(null);
    }

    @Override
    public Submission.Status submit(int asid, String nid, String content) {
        Optional<Submission> target = submissionRepository.findByAsidAndNid(asid, nid);
        if (!target.isPresent()) {
            return null;
        }

        long now = System.currentTimeMillis();
        Submission submission = target.get();
        Assignment assignment = submission.getAssignment();
        if (now < assignment.getEndTime().getTime()) {
            submission.setStatus(Submission.Status.SUCCESS);
            submission.setMessage(content);
            submission.setScore(0);
            submission.setSubmitTime(Timestamp.from(Instant.now()));
        }

        return submissionRepository.save(submission).getStatus();
    }

    @Override
    public Integer grade(int asid, String nid, int score) {
        Optional<Submission> target = submissionRepository.findByAsidAndNid(asid, nid);
        if (!target.isPresent()) {
            return null;
        }

        Submission submission = target.get();
        submission.setScore(score);
        return submissionRepository.save(submission).getScore();
    }

    @Override
    public AssignmentDto modify(AssignmentDto assignmentDto) {
        if (assignmentDto.getAsid() == null ||
            !assignmentRepository.findByAsid(assignmentDto.getAsid()).isPresent()) {
            return null;
        }
        Assignment result = assignmentRepository.save(assignmentDto.toAssignment());
        return new AssignmentDto(result);
    }

    @Override
    public boolean delete(int asid) {
        if (!assignmentRepository.findByAsid(asid).isPresent()) {
            return false;
        }
        assignmentRepository.deleteById(asid);
        return true;
    }

    //TODO:implement
    private boolean validateAssignment(AssignmentDto assignmentDto, boolean ext) {
        throw new NotImplementedException();
    }

    private List<String> findListByStatus(int atid, Submission.Status status) {
        if (!assignmentRepository.findByAsid(atid).isPresent()) {
            return null;
        }

        List<Submission> ret;
        if (status == null) {
            ret = submissionRepository.findByAsid(atid);
        } else {
            ret = submissionRepository.findByAsidAndStatus(atid, status);
        }
        return ret
            .stream()
            .map(Submission::getNid)
            .collect(Collectors.toList());
    }

    private class CheckSubmissionTask extends CheckCronTask {
        public CheckSubmissionTask(long duration, long birth, int atid) {
            super(duration, birth, atid);
        }

        @Override
        public void check() {
            List<Submission> targets = submissionRepository.findByAsid(targetId);
            for (Submission checkout : targets) {
                if (checkout.getStatus() == Submission.Status.GOING_ON) {
                    checkout.setStatus(Submission.Status.ABSENT);
                }
            }
            submissionRepository.saveAll(targets);
            log.info("Scheduled task completed(atid=" + targetId + ")");
        }
    }

    @Autowired
    public void setAssignmentRepository(AssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }

    @Autowired
    public void setSubmissionRepository(SubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
    }
}
