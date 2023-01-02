package team.segroup.etms.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Authenticator implements HandlerInterceptor {
    private static final String WARRANT = "warrant";
    private static final String REMOTE_VALIDATOR = "http://120.78.65.145:8080/token?token=";

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
//        if (remoteValidate(token)) {
//            return true;
//        } else {
//            response.setStatus(403);
//            return false;
//        }
    }

    public boolean remoteValidate(String token) {
        URL url;
        try {
            url = new URL(REMOTE_VALIDATOR + token);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        }

        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.connect();

            return connection.getResponseCode() == HttpURLConnection.HTTP_OK;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (connection != null)
                connection.disconnect();
        }
    }
}
