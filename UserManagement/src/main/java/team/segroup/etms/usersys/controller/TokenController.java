package team.segroup.etms.usersys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.segroup.etms.ResponseBody;
import team.segroup.etms.TokenInfo;
import team.segroup.etms.usersys.entity.User;
import team.segroup.etms.usersys.service.TokenService;
import team.segroup.etms.usersys.service.UserService;

import java.time.Instant;

// TODO:当http请求中的参数无效时，应该返回什么状态码？暂时使用bad_request
@RestController
@RequestMapping("/token")
public class TokenController {
    private UserService userService;
    private TokenService tokenService;

    private final static ResponseEntity.BodyBuilder buildForbidden =
        ResponseEntity.status(HttpStatus.FORBIDDEN);

    /**
     * 身份验证通过则生成一个token并返回（包括是否为管理员），
     * 未通过返回403
     *
     * @param nid      学工号
     * @param password 密码（已SHA1）
     * @return token和isAdmin
     */
    @PostMapping
    public ResponseEntity<ResponseBody> generateToken(
        @RequestParam("nid") String nid,
        @RequestParam("password") String password
    ) {
        if (!userService.verify(nid, password)) {
            return buildForbidden.build();
        }

        String token = tokenService.generateToken(nid);
        User user = userService.retrieveUser(nid);
        return ResponseEntity.ok(ResponseBody.create()
            .add("token", token)
            .add("isAdmin", user.isAdmin())
        );
    }

    /**
     * 接受一个token并解析。
     *
     * @param token token字符串
     * @return 解析成功则返回过期时间、学工号和是否为管理员；若过期，返回432；否则返回403
     */
    @GetMapping
    public ResponseEntity<TokenInfo> verifyToken(@RequestParam("token") String token) {
        TokenInfo tokenInfo = tokenService.verifyToken(token);
        if (tokenInfo == null) {
            return buildForbidden.build();
        } else if (tokenInfo.getExpiresAt().compareTo(Instant.now()) < 0) {
            return ResponseEntity.status(432).build();
        } else {
            return ResponseEntity.ok(tokenInfo);
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
