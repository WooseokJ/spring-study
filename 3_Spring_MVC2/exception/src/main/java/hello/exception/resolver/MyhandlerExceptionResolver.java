package hello.exception.resolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Slf4j
public class MyhandlerExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            if (ex instanceof IllegalArgumentException) {
                log.info("IllegalArgumentException resolver to 400");
                // sendError는 체크예외라 try-catch로 잡아줘야해.
                // 원래 500 -> 400으로 바꿈
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage()); // 400, 오류메세지
                return new ModelAndView();
            }


        } catch (IOException e) {
            log.error("resolver ex", e);
        }
        return null;
    }
}
