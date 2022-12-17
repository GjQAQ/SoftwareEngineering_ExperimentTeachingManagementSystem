package team.segroup.etms.usersys.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.segroup.etms.usersys.dto.UserDto;
import team.segroup.etms.usersys.entity.User;
import team.segroup.etms.TokenInfo;
import team.segroup.etms.usersys.service.TokenService;
import team.segroup.etms.usersys.service.UserService;

import java.time.Instant;
import java.util.HashMap;

@Service
public class TokenServiceImpl implements TokenService {
    private static final String SECRET = "!34ADAS";
    private static final long EXPIRY = 60 * 60 * 2;

    private final Algorithm algorithm = Algorithm.HMAC256(SECRET);
    private final JWTVerifier verifier = JWT.require(algorithm).build();

    private UserService userService;

    @Override
    public String generateToken(String nid) {
        //TODO:验证nid
        return JWT.create()
            .withHeader(new HashMap<>())
            .withClaim("nid", nid)
            .withExpiresAt(Instant.now().plusSeconds(EXPIRY))
            .sign(algorithm);
    }

    @Override
    public TokenInfo verifyToken(String token) {
        DecodedJWT decodedJWT;
        try {
            decodedJWT = verifier.verify(token);
        } catch (JWTVerificationException e) {
            return null;
        }

        String nid = decodedJWT.getClaim("nid").asString();
        UserDto user = userService.retrieveUser(nid);
        return new TokenInfo(
            decodedJWT.getExpiresAtAsInstant(),
            nid,
            user.isAdmin()
        );
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
