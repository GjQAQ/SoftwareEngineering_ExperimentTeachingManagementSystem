package team.segroup.etms.scoresys.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import team.segroup.etms.scoresys.dto.AttendanceDto;
import team.segroup.etms.scoresys.entity.Attendance;
import team.segroup.etms.scoresys.entity.Checkout;
import team.segroup.etms.scoresys.repository.AttendanceRepository;
import team.segroup.etms.scoresys.repository.CheckoutRepository;
import team.segroup.etms.scoresys.service.AttendanceService;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AttendanceServiceImpl implements AttendanceService {
    private static final long DEFAULT_DELAY = 1000 * 60 * 60;

    private AttendanceRepository attendanceRepository;
    private CheckoutRepository checkoutRepository;

    @Override
    public AttendanceDto create(
        AttendanceDto attendanceDto
    ) {
        attendanceDto.setAtid(null);
        if (!validateAttendance(attendanceDto)) {
            return null;
        }

        Attendance result = attendanceRepository.save(attendanceDto.toAttendance());
        return new AttendanceDto(result);
    }

    @Override
    public AttendanceDto findByAtid(int atid) {
        return attendanceRepository.findByAtid(atid)
            .map(AttendanceDto::new)
            .orElse(null);
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
        throw new NotImplementedException();
    }

    @Override
    public List<String> findLateList(int atid) {
        throw new NotImplementedException();
    }

    @Override
    public List<String> findAbsentList(int atid) {
        throw new NotImplementedException();
    }

    @Override
    public List<String> findIndividualInvolved(int atid) {
        throw new NotImplementedException();
    }

    @Override
    public Checkout.Status queryAttendanceStatus(int atid, String nid) {
        Optional<Checkout> checkOutOpt = checkoutRepository.findByAtidAndNid(atid, nid);
        if (checkOutOpt.isPresent()) {
            return checkOutOpt.get().getStatus();
        }

        Optional<Attendance> attOpt = attendanceRepository.findByAtid(atid);
        if (!attOpt.isPresent()) {
            return null;
        }
        Attendance attendance = attOpt.get();
        Timestamp now = Timestamp.from(Instant.now());
        if (now.compareTo(attendance.getStartTime()) < 0) {
            return Checkout.Status.NOT_START;
        } else if (now.compareTo(attendance.getEndTime()) < 0) {
            return Checkout.Status.GOING_ON;
        } else {
            return Checkout.Status.ABSENT;
        }
    }

    @Transactional
    @Override
    public Checkout.Status signIn(int atid, String nid, Instant checkTime) {
        Checkout.Status status = queryAttendanceStatus(atid, nid);
        if (status == null) {
            return null;
        } else if (status != Checkout.Status.GOING_ON &&
            status != Checkout.Status.ABSENT) {
            return Checkout.Status.FAILED;
        }

        Attendance attendance = attendanceRepository.findByAtid(atid).get();
        Timestamp checkT = Timestamp.from(checkTime);
        Timestamp endT = attendance.getEndTime();
        Timestamp ddl = Timestamp.from(Instant.ofEpochMilli(endT.getTime() + DEFAULT_DELAY));
        if (endT.compareTo(checkT) > 0) {
            checkoutRepository.save(new Checkout(
                null,
                atid,
                nid,
                checkT,
                Checkout.Status.SUCCESS
            ));
            return Checkout.Status.SUCCESS;
        } else if (ddl.compareTo(checkT) > 0) {
            checkoutRepository.save(new Checkout(
                null,
                atid,
                nid,
                checkT,
                Checkout.Status.LATE
            ));
            return Checkout.Status.LATE;
        } else {
            return Checkout.Status.FAILED;
        }
    }

    @Override
    public AttendanceDto modify(AttendanceDto attendanceDto) {
        if (attendanceDto.getAtid() == null ||
            !attendanceRepository.findByAtid(attendanceDto.getAtid()).isPresent()) {
            return null;
        }
        Attendance result = attendanceRepository.save(attendanceDto.toAttendance());
        return new AttendanceDto(result);
    }

    @Override
    public boolean delete(int atid) {
        if(!attendanceRepository.findByAtid(atid).isPresent()){
            return false;
        }

        attendanceRepository.deleteById(atid);
        return true;
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
