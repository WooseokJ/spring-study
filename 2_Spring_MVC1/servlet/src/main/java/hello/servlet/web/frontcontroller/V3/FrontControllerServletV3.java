package hello.servlet.web.frontcontroller.V3;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.V3.controller.MemberFormControllerV3;
import hello.servlet.web.frontcontroller.V3.controller.MemberListControllerV3;
import hello.servlet.web.frontcontroller.V3.controller.MemberSaveControllerV3;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "frontControllerServletV3", urlPatterns = "/front-controller/v3/members/*")
public class FrontControllerServletV3 extends HttpServlet {

    private Map<String, ControllerV3> controllerV3Map = new HashMap<>();

    public FrontControllerServletV3() {
        controllerV3Map.put("/front-controller/v3/members/new-form",new MemberFormControllerV3()); // key(매핑된url) : value(호출된 컨트롤러 객체) 형태로 저장.
        controllerV3Map.put("/front-controller/v3/members/save",new MemberSaveControllerV3());
        controllerV3Map.put("/front-controller/v3/members",new MemberListControllerV3());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        ControllerV3 controller = controllerV3Map.get(requestURI);
        if(controller == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        //paramMap

        Map<String, String> paramMap = createParamMap(request);
        ModelView modelView = controller.process(paramMap); // modelView 반환까지했다.  FC < - Controller

        // viewResolver 호출
        String viewName = modelView.getViewName();// 논리이름 new-from
        MyView view = viewResolver(viewName); // 논리이름을 갖고 물리이름을 만든다.
        view.render(modelView.getModel(), request, response);

    }


    private static MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

    // 파라미터들을 다 뽑는다.
    private static Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String, String > paramMap = new HashMap<>();

        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName))); // key( new-from) : value:
        return paramMap;
    }
}
