package hello.productservice.main.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

@Slf4j
public class LoginCheckFilter implements Filter {

    private static final String[] whitelist = {"/","/members/add","login","/logout","/css/*"};


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String requestURI = httpServletRequest.getRequestURI();

        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        try {
            log.info("인증 체크 필터{}",requestURI);
            if(isLoginCheckPath(requestURI)){
                log.info("인증 체크 로직 실행{}",requestURI);
                HttpSession session = httpServletRequest.getSession(false);
                if (session==null|| session.getAttribute("LOGIN_MEMBER")==null){
                    log.info("미인증 사용자 요청{}",requestURI);
                    httpServletResponse.sendRedirect("/login?redirectURL="+requestURI);
                    return;
                };

            }
            filterChain.doFilter(request,response);
        }catch (Exception e){
            throw e;//예외 로깅 가능 하지만 톰캣까지 예외를 보내주어야 함
        }finally {
            log.info("인증 체크 필터 종료");
        }

        /*/

         */
    }

    private boolean isLoginCheckPath(String requestURI){
        //whitelist에 없는 URI로 요청시 false return
        return !PatternMatchUtils.simpleMatch(whitelist,requestURI);

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
