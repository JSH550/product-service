package hello.productservice.main.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    //X-Request-ID 사용자 정의 HTTP 헤더
    private static final String REQUEST_ID_HEADER = "X-Request-ID";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //requestURL 객체에 저장
        String requestURL = request.getRequestURL().toString();
        //request에 사용할 고유식별자 생성 및 객체에 추가
        //싱글톤이기 때문에 메서드밖에서 set하면 안됨
        String requestId = UUID.randomUUID().toString();
        request.setAttribute(REQUEST_ID_HEADER, requestId);
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;//호출할 컨트롤러 메서드의 모든 정보가 포함되어있음
            log.info("PreHandle - Request URL: {}, Request ID: {}, Handler: {}", requestURL, requestId, handler);
        }else{
            log.info("PreHandle - Request URL :{}, Request ID:{},Handler:{}", requestURL, requestId, handler);
        }
        log.info("Start Time: {}", LocalDateTime.now());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String requestId = request.getAttribute(REQUEST_ID_HEADER).toString();
//        log.info("postHandle{}", modelAndView);
        log.info("PostHandle - Request ID:{}",requestId);
        log.info("End Time: {}",  LocalDateTime.now());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestId = request.getAttribute(REQUEST_ID_HEADER).toString();
        log.info("Request Completed - Request ID:{}",requestId);
        if (ex != null) {
            log.error("afterCompletion error", ex);
        }

    }
}
