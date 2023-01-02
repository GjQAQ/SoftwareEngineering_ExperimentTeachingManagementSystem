package team.segroup.etms.usersys.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import team.segroup.etms.TokenInfo;
import team.segroup.etms.usersys.service.TokenService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthenticationInterceptor implements HandlerInterceptor {
    private static final String WARRANT = "warrant";
    private TokenService tokenService;

    @Override
    public boolean preHandle(
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler
    ) throws Exception {
        return true;
//        if("false".equals(request.getParameter("warrant"))){
//            return true;
//        }
//
//        String token = request.getHeader("Token");
//        if (token == null) {
//            response.setStatus(401);
//            return false;
//        } else if (WARRANT.equals(token)) {
//            return true;
//        }
//
//        TokenInfo tokenInfo = tokenService.verifyToken(token);
//        if (tokenInfo == null) {
//            response.setStatus(403);
//            return false;
//        }
//
//        return true;
    }

    @Autowired
    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }
}
