package hello.login.web.Interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
public class logInterceptor implements HandlerInterceptor { // cntl + o

    public static final String LOG_ID = "logID"; // command + option + c

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString();

        // 여기서 선언한 uuid를 afterCompletion떄 쓰고싶으면? -> 넣어줘서 꺼내자(requset.getAttribute)
        request.setAttribute(LOG_ID,uuid);

        // 참고: @RequestMapping사용시 HandlerMethod 가 넘어옴.
        // 정적리소스사용시 ResourceHttpRequestHandler

        if(handler instanceof HandlerMethod) {
            // 호출할 컨트롤러 메서드의 모든정보 포함.
            HandlerMethod allHm = (HandlerMethod) handler;

        }
        log.info("reqeust [{}] [{}] [{}] ",uuid, requestURI, handler);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle {}", modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        String logid = (String) request.getAttribute(LOG_ID);
        log.info("response [{}] [{}] [{}]", logid ,requestURI, handler);
        if(ex != null) {
//            log.error("aftercomplete error {} ", ex);    이거와 동일
            log.error("aftercomplete error", ex);
        }
    }
}
