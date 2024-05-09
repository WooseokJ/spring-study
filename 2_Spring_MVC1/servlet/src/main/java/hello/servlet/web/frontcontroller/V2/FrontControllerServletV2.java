package hello.servlet.web.frontcontroller.V2;

import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.V2.controller.MemberFormControllerV2;
import hello.servlet.web.frontcontroller.V2.controller.MemberListControllerV2;
import hello.servlet.web.frontcontroller.V2.controller.MemberSaveControllerV2;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@WebServlet(name = "frontControllerServletV2", urlPatterns = "/front-controller/v2/members/*")
public class FrontControllerServletV2 extends HttpServlet {
    private Map<String, ControllerV2> controllerV2Map = new HashMap<>();

    public FrontControllerServletV2() {
        controllerV2Map.put("/front-controller/v2/members/new-form",new MemberFormControllerV2()); // key(매핑된url) : value(호출된 컨트롤러 객체) 형태로 저장.
        controllerV2Map.put("/front-controller/v2/members/save",new MemberSaveControllerV2());
        controllerV2Map.put("/front-controller/v2/members",new MemberListControllerV2());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        ControllerV2 controller = controllerV2Map.get(requestURI);
        if(controller == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        MyView view = controller.process(request, response); // new MyView("/WEB-INF/views/members.jsp"); 같은거 받아.
        view.render(request, response);
    }
}
