package team.segroup.etms.usersys.service;

import team.segroup.etms.usersys.dto.UncheckedUserDto;
import team.segroup.etms.usersys.dto.UserDto;
import team.segroup.etms.usersys.entity.UncheckedUser;
import team.segroup.etms.usersys.entity.User;

import java.util.List;
import java.util.stream.Stream;

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
     * 批量注册。
     * @param users 用户信息的Iterable
     * @param checked 所添加用户是否已查验
     * @return 两个nid的列表，第一个是成功列表，第二个是未成功列表
     */
    List<String>[] registerBatch(Stream<UncheckedUserDto> users, boolean checked);

    /**
     * 查验通过/不通过某个用户。
     * @param nid 学号
     * @param valid 是否通过
     * @return 有效用户对象，失败返回null
     */
    User validateUser(String nid, boolean valid);

    /**
     * 激活用户。
     * @param nid 学号
     * @return 是否成功
     */
    boolean activateUser(String nid);

    /**
     * 获取用户信息
     * @param nid 学号
     * @return 用户实例。失败返回null
     */
    UserDto retrieveUser(String nid);

    List<UserDto> listAllUsers();

    UncheckedUserDto retrieveUncheckedUser(String nid);

    List<UncheckedUserDto> listAllUncheckedUsers();

    /**
     * 注销账号
     * @param nid 学号
     * @return 是否成功
     */
    boolean removeUser(String nid);

    /**
     * 更新用户信息。
     * @param userDto 新的用户信息
     * @return 是否成功
     */
    boolean updateUser(UserDto userDto);

    boolean updatePassword(String password, String nid);
}
