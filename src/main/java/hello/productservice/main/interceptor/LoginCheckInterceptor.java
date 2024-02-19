package hello.productservice.main.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        try {
            log.info("Login 인증 인터셉터 실행", requestURI);
            HttpSession session = request.getSession();
            if (session == null || session.getAttribute("LOGIN_MEMBER") == null) {
                log.info("Login 미인증 사용자 요청{}", requestURI);
                response.sendRedirect("/members/login?redirectURL=" + requestURI);
                return false;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            log.info("Login 인증 인터셉터 종료");
        }


        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;//호출할 컨트롤러 메서딍 모든 정보가 포함됭 ㅓ있다.

        }
        return true;
    }


}

