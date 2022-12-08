package team.segroup.etms.usersys.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;
import team.segroup.etms.usersys.service.TokenInfo;
import team.segroup.etms.usersys.service.TokenService;

import java.time.Instant;
import java.util.HashMap;

@Service
public class TokenServiceImpl implements TokenService {
    private static final String SECRET = "!34ADAS";
    private static final long EXPIRY = 60 * 60 * 2;

    private final Algorithm algorithm = Algorithm.HMAC256(SECRET);
    private final JWTVerifier verifier = JWT.require(algorithm).build();

    @Override
    public String generateToken(String nid) {
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
        return new TokenInfo(
            decodedJWT.getExpiresAtAsInstant(),
            decodedJWT.getClaim("nid").asString()
        );
    }
}
