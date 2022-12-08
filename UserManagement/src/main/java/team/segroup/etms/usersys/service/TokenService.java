package team.segroup.etms.usersys.service;

public interface TokenService {
    /**
     * 根据给定学号生成token。
     * @param nid 学号
     * @return token字符串
     */
    String generateToken(String nid);

    /**
     * 验证给定token。
     * @param token token
     * @return 学号字符串。如果token无效，返回null
     */
    TokenInfo verifyToken(String token);
}
