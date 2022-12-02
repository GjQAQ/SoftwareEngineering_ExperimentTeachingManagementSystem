package team.segroup.etms.usersys.service;

import team.segroup.etms.usersys.dto.UncheckedUserDto;
import team.segroup.etms.usersys.entity.UncheckedUser;
import team.segroup.etms.usersys.entity.User;

public interface UserService {
    /**
     * 验证传入的学号密码组合是否正确。
     * @param nid 学号
     * @param password 密码
     * @return 是否正确
     */
    boolean verify(String nid, String password);

    /**
     * 注册。
     * @param userDto 包含用户信息的DTO
     * @return 新用户实例。失败则返回null。
     */
    UncheckedUser register(UncheckedUserDto userDto);

    /**
     * 查验通过某个用户。
     * @param nid 学号
     * @return 是否成功
     */
    boolean validateUser(String nid);

    /**
     * 激活用户。
     * @param nid 学号
     * @return 是否成功
     */
    boolean activateUser(String nid);

    /**
     * 获取用户信息
     * @param nid 学号
     * @return 用户实例。失败返回null。
     */
    User retrieveUser(String nid);
}
