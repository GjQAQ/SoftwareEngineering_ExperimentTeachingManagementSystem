import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import team.segroup.etms.scoresys.ScoreManagementApplication;
import team.segroup.etms.scoresys.entity.Checkout;
import team.segroup.etms.scoresys.entity.CheckoutKey;
import team.segroup.etms.scoresys.repository.CheckoutRepository;

import java.util.List;

@SpringBootTest(classes = ScoreManagementApplication.class)
@RunWith(SpringRunner.class)
public class TempTest {
    @Autowired
    private CheckoutRepository checkoutRepository;

    @Transactional
    @Test
    public void temp() {
        List<Checkout> checkouts =
            checkoutRepository.findByAttendanceCourseCode("20220000");
        System.out.println(checkouts.size());
        if (checkouts.size() > 0) {
            System.out.println(checkouts.get(0));
        }
    }
}
