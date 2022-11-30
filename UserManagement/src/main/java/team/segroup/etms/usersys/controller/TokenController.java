package team.segroup.etms.usersys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.segroup.etms.usersys.service.TokenService;
import team.segroup.etms.usersys.service.UserService;

@RestController
@RequestMapping("/token")
public class TokenController {
    private UserService userService;
    private TokenService tokenService;

    /**
     * @param uid
     * @param password
     * @return JWT token string
     */
    @PostMapping
    public String generateToken(
        @RequestParam("uid") int uid,
        @RequestParam("password") String password
    ) {
        if (!userService.verify(uid, password))
            return "";

        return tokenService.generateToken(uid);
    }

    @GetMapping
    public int verifyToken(@RequestParam("token") String token) {
        return tokenService.verifyToken(token);
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }
}
