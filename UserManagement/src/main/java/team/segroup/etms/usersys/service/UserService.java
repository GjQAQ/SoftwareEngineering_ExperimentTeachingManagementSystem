package team.segroup.etms.usersys.service;

import team.segroup.etms.usersys.dto.UncheckedUserDto;
import team.segroup.etms.usersys.entity.UncheckedUser;
import team.segroup.etms.usersys.entity.User;

public interface UserService {
    boolean verify(int uid, String password);
    UncheckedUser register(UncheckedUserDto userDto);
    boolean validateUser(int uid, boolean valid);
    boolean activateUser(int uid);
    User retrieveUser(int uid);
}
