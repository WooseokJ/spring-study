package hello.login.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Slf4j

public class LogFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init");
    }

    // http 요청올떄마다 doFilter 실행.
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 로직
        // ServletRequest(부)는 HttpServletRequest(자)로 기능이 별로없어서 다운케스팅.
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String requestURI = httpServletRequest.getRequestURI();

        // http 요청을 구분하기위해 만듬.(요청시 매번 다른uuid 생성)
        String uuid = UUID.randomUUID().toString();

        try{
            log.info("Request [{}] [{}]", uuid, requestURI);
            chain.doFilter(request, response); // ⭐️ 다음필터 있으면 필터 호출, 없으면 서블릿 호출. (이걸통해 서블릿으로 건너감), DispatcherSublet이므로 controller 호출.
        } catch (Exception e) {
            throw  e;
        } finally {
            // 확인용
            log.info("Response!! [{}] [{}]", uuid, requestURI);
        }
    }

    @Override
    public void destroy() {
        log.info("log filter destory");
    }
}
