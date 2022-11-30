package team.segroup.etms.usersys.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import team.segroup.etms.usersys.service.UserService;
import team.segroup.etms.usersys.service.impl.UserServiceImpl;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {
    @Autowired
    private UserServiceImpl userService;

    @Test
    public void sha1Test(){
        System.out.println(userService.hash("hello"));
    }
}
