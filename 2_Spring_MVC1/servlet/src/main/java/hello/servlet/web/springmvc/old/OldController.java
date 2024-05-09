package hello.servlet.web.springmvc.old;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

@Component("/springmvc/old-controller") // spring bean 이름이 /springmvc/old-controller가 된다.
public class OldController implements Controller { // 지금은 안쓴다. Controller interface를

    // url (localhost:8080/springmvc/old-controller)로 요청하면 handlerRequest 호출된다.

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("==========  호출이 된다. ==========");

        return new ModelAndView("new-form");
    }


    // oldController 가 호출되려면 handlerMapping, handlerAdapter 두가지가 필요하다.
    // 1. url 요청하면 handlerMapping에서 OldController 가져옴.(spring bean 이름으로 handler 찾을수있는 handlerMapping 필요)
    // 2. 인터페이스 Controller를 호출할수있는 HandlerAdapter를 찾고 실행 ( HandlerMapping에서 찾은 Handler를 실행할 HandlerAdapter 필요)
}
