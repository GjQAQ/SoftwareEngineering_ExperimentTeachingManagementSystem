package team.segroup.etms.usersys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.segroup.etms.usersys.service.TokenService;
import team.segroup.etms.usersys.service.UserService;

// TODO:当http请求中的参数无效时，应该返回什么状态码？暂时使用bad_request
@RestController
@RequestMapping("/token")
public class TokenController {
    private UserService userService;
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<String> generateToken(
        @RequestParam("uid") int uid,
        @RequestParam("password") String password
    ) {
        if (!userService.verify(uid, password)) {
            return ResponseEntity.badRequest().build();
        }

        String token = tokenService.generateToken(uid);
        return ResponseEntity.ok(token);
    }

    @GetMapping
    public ResponseEntity<Integer> verifyToken(@RequestParam("token") String token) {
        int uid = tokenService.verifyToken(token);
        if (uid == -1) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(uid);
        }
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
