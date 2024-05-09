package hello.servlet.web.frontcontroller.V5;

import hello.servlet.web.frontcontroller.ModelView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface MyHandlerAdapter {
    // adapter가 해당 Controller 처리할수있는지 판단.
    boolean supports(Object handler); // handler는 Controller 의미.

    // adapter는 실제 Controller 호출, 그결과의 반환값으로 ModelView 반환.(만약, 실제 Controller가 ModelView 반환못하면 생성해서 반환.)
    // 3. handle adapter 호출해서 내부적으로 handler호출    handler -> FC에게 ModelView 반환.
    ModelView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException , IOException;


}
