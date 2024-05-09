package hello.servlet.web.springmvc.V1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
@Controller // @Controller안에 @Component 있어서 컴포넌트 스캔 통해 스프링빈으로 등록해줌.
            // 그리고 SpringMVC에서 애노테이션 기반 컨트롤러로 인식.(RequestMappingHandlerMapping에서 Handler를 반환해서 받을 대상이됨)
            // 참고: RequestMappingHandlerMapping는 @RequestMapping, @Controller가 Class에 붙어있는경우 매핑정보를 인식한다.
public class SpringMemberFormControllerV1 {

    @RequestMapping("springmvc/v1/members/new-form")
    public ModelAndView process() { // url 요청하면 process() 호출 . // 메서드명은 자유롭게
        return new ModelAndView("new-form");
    }




}
