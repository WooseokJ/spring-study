package hello.servlet.web.springmvc.old;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestHandler;

import java.io.IOException;

@Component("/springmvc/request-handler")
public class MyhttpRequestHandler implements HttpRequestHandler {
    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    // 2. handlerAdapter의 HttpRequestHandlerAdapter의 handle 실행해서  HttpRequestHandler 찾아 실행.

    // 1. url 요청되면 HandlerMapping의 BeanNameUrlHandlerMapping 통해 MyhttpRequestHandler 찾음.
    // 2. 조회: HandlerAdapter는 support() 통해  HttpRequestHandler를 반환받고  HttpRequestHandlerAdapter가 지원대상이된다.
    // 3. 실행: HttpRequestHandlerAdapter의 handle() 실행해서 handleRequest 실행

}
