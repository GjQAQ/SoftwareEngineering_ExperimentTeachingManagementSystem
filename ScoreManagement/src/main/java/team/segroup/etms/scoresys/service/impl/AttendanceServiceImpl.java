package team.segroup.etms.scoresys.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import team.segroup.etms.scoresys.dto.AttendanceDto;
import team.segroup.etms.scoresys.entity.Attendance;
import team.segroup.etms.scoresys.entity.Checkout;
import team.segroup.etms.scoresys.repository.AttendanceRepository;
import team.segroup.etms.scoresys.repository.CheckoutRepository;
import team.segroup.etms.scoresys.service.AttendanceService;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AttendanceServiceImpl implements AttendanceService {
    private AttendanceRepository attendanceRepository;
    private CheckoutRepository checkoutRepository;

    private final DelayQueue<CheckAttendanceTask> checkers = new DelayQueue<>();

    @PostConstruct
    public void init() {
        log.trace("AttendanceService initializing...");
        Executors.newSingleThreadExecutor().execute(new Thread(() -> {
            while (true) {
                try {
                    checkers.take().check();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    log.error("Attendance checker fails.");
                }
            }
        }));
    }

    @Override
    public AttendanceDto create(
        AttendanceDto attendanceDto,
        Set<String> nids
    ) {
        attendanceDto.setAtid(null);
        if (!validateAttendance(attendanceDto)) {
            return null;
        }

        Attendance result = attendanceRepository.save(attendanceDto.toAttendance());
        long duration = result.getEndTime().getTime() - result.getStartTime().getTime();
        checkers.put(new CheckAttendanceTask(
            duration, result.getStartTime().getTime(), result.getAtid()
        ));
        log.info("Scheduled task set(atid=" + attendanceDto.getAtid() + ")");

        for (String nid : nids) {
            checkoutRepository.save(new Checkout(
                null,
                result.getAtid(),
                nid,
                null,
                Checkout.Status.GOING_ON
            ));
        }
        return new AttendanceDto(result);
    }

    @Override
    public List<AttendanceDto> findByCourseCode(String code) {
        return attendanceRepository.findByCourseCode(code)
            .stream()
            .map(AttendanceDto::new)
            .collect(Collectors.toList());
    }

    @Override
    public List<AttendanceDto> findByOwner(String nid) {
        return attendanceRepository.findByOwnerNid(nid)
            .stream()
            .map(AttendanceDto::new)
            .collect(Collectors.toList());
    }

    @Override
    public List<String> findSuccessList(int atid) {
        return findListByStatus(atid, Checkout.Status.SUCCESS);
    }

    @Override
    public List<String> findLateList(int atid) {
        return findListByStatus(atid, Checkout.Status.LATE);
    }

    @Override
    public List<String> findAbsentList(int atid) {
        return findListByStatus(atid, Checkout.Status.ABSENT);
    }

    @Override
    public List<String> findIndividualInvolved(int atid) {
        return findListByStatus(atid, null);
    }

    @Override
    public Checkout.Status queryAttendanceStatus(int atid, String nid) {
        Optional<Checkout> checkout = checkoutRepository.findByAtidAndNid(atid, nid);
        return checkout.map(Checkout::getStatus).orElse(null);
    }

    @Override
    public Checkout.Status signIn(int atid, String nid) {
        Optional<Checkout> target = checkoutRepository.findByAtidAndNid(atid, nid);
        if (!target.isPresent()) {
            return null;
        }

        long now = System.currentTimeMillis();
        Checkout checkout = target.get();
        Attendance attendance = checkout.getAttendance();
        if (now < attendance.getStartTime().getTime()) {
            checkout.setStatus(Checkout.Status.SUCCESS);
            checkout.setCheckTime(Timestamp.from(Instant.now()));
        } else if (now < attendance.getEndTime().getTime()) {
            checkout.setStatus(Checkout.Status.LATE);
            checkout.setCheckTime(Timestamp.from(Instant.now()));
        } else {
            checkout.setStatus(Checkout.Status.ABSENT);
        }

        return checkoutRepository.save(checkout).getStatus();
    }

    private boolean validateAttendance(AttendanceDto attendanceDto, boolean ext) {
        Integer atid = attendanceDto.getAtid();
        if (atid != null && !attendanceRepository.findByAtid(atid).isPresent()) {
            return false;
        }

        if (attendanceDto.getEndTime().compareTo(attendanceDto.getStartTime()) < 0) {
            return false;
        }
        //TODO:compare with now

        //TODO:implement
        if (ext) {
            throw new NotImplementedException();
        }

        return true;
    }

    private boolean validateAttendance(AttendanceDto attendanceDto) {
        return validateAttendance(attendanceDto, false);
    }

    private List<String> findListByStatus(int atid, Checkout.Status status) {
        if (!attendanceRepository.findByAtid(atid).isPresent()) {
            return null;
        }

        List<Checkout> ret;
        if (status == null) {
            ret = checkoutRepository.findByAtid(atid);
        } else {
            ret = checkoutRepository.findByAtidAndStatus(atid, status);
        }
        return ret
            .stream()
            .map(Checkout::getNid)
            .collect(Collectors.toList());
    }


    @Autowired
    public void setAttendanceRepository(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    @Autowired
    public void setCheckoutRepository(CheckoutRepository checkoutRepository) {
        this.checkoutRepository = checkoutRepository;
    }

    private class CheckAttendanceTask extends CheckCronTask {
        public CheckAttendanceTask(long duration, long birth, int atid) {
            super(duration, birth, atid);
        }

        public void check() {
            List<Checkout> targets = checkoutRepository.findByAtid(targetId);
            for (Checkout checkout : targets) {
                if (checkout.getStatus() == Checkout.Status.GOING_ON) {
                    checkout.setStatus(Checkout.Status.ABSENT);
                }
            }
            checkoutRepository.saveAll(targets);
            log.info("Scheduled task completed(atid=" + targetId + ")");
        }
    }
}
