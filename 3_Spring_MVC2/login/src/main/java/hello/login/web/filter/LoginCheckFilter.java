package hello.login.web.filter;

import hello.login.web.session.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class LoginCheckFilter implements Filter {

    // 인증 제외 리스트
    private static final String[] whiteList = {"/", "/members/add", "/login", "/logout", "css/*"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String requestURI = httpServletRequest.getRequestURI();

        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        try {
            log.info("인증 체크 필터 시작 {}", requestURI);
            if (isLoginCheckPath(requestURI)) {
                log.info("인증 체크 필터 실행", requestURI);
                HttpSession session = httpServletRequest.getSession(false);

                if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
                    log.info("미인증 사용자 요청 {}", requestURI);
                    // 로그인으로 redirect
                    // /login?redirectURL=를 같이 붙여서 넣어주는이유
                    // -> 로그인 성공하고나서는 홈화면이아닌 상품관리 페이지로 보여주고싶을떄 사옹.
                    httpServletResponse.sendRedirect("/login?redirectURL="+requestURI);
                    return; // 꼭넣어야해: 미인증 사용자는 필터 다음으로 진행안하고 끝.(redirect가 응답으로 적용되고 요청끝)

                }
            }
            chain.doFilter(request, response);
        }catch (Exception e) {
            throw e;
        }finally {
            log.info("인증 체크 필터 종료 {}", requestURI);

        }


    }
    // whiteList의 경우 인증체크 x
    private Boolean isLoginCheckPath(String requestURI) {
        return !PatternMatchUtils.simpleMatch(whiteList, requestURI); // requestURI와 whiteList를 매칭해서 매칭되면 false로 바꿔서 리턴.
    }

}
