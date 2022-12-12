package team.segroup.etms.scoresys.repository;

import org.springframework.data.repository.CrudRepository;
import team.segroup.etms.scoresys.entity.Checkout;
import team.segroup.etms.scoresys.entity.CheckoutKey;

import java.util.List;
import java.util.Optional;

public interface CheckoutRepository extends
    CrudRepository<Checkout, CheckoutKey> {
    List<Checkout> findByNid(String nid);

    List<Checkout> findByAttendanceCourseCode(String courseCode);

    List<Checkout> findByAtid(int atid);

    Optional<Checkout> findByAtidAndNid(int atid, String nid);

    List<Checkout> findByAtidAndStatus(int atid, Checkout.Status status);
}
