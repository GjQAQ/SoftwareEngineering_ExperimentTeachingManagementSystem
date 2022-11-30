package team.segroup.etms.usersys.service;

public interface TokenService {
    String generateToken(int uid);
    int verifyToken(String token);
}
