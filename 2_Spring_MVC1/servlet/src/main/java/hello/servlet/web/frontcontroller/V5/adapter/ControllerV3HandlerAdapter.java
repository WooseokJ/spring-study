package hello.servlet.web.frontcontroller.V5.adapter;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.V3.ControllerV3;
import hello.servlet.web.frontcontroller.V5.MyHandlerAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ControllerV3HandlerAdapter implements MyHandlerAdapter {
    @Override
    public boolean supports(Object handler) {
        return handler instanceof ControllerV3; // ControllerV3로 구현된건 true, 아니면 false
    }

    // 3. handle(handler) 로 핸들러 어댑터 호출됨.
    @Override
    public ModelView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException {
        ControllerV3 controller = (ControllerV3) handler; // handler ->  MemberFormControllerV3
        Map<String, String> paramMap = createParamMap(request);
        System.out.println("paramMap = " + paramMap.keySet() );
        ModelView modelView = controller.process(paramMap); // 4. 실제 handler(controller) 호출

        System.out.println("modelView = " + modelView);
        return modelView;
    }

    // 파라미터들을 다 뽑는다.
    private Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String, String > paramMap = new HashMap<>();

        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName))); // key( new-from) : value:
        return paramMap;
    }
}
