package hello.springmvc.basic.response;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ResponseViewController {
    // 응답데이터중 viewTeamplete 설명

    // 1. ModelAndView 반환

    @RequestMapping("response-view-v1")
    public ModelAndView resonseViewV1() {
        ModelAndView modelAndView = new ModelAndView("response/hello");
        ModelAndView mav = modelAndView.addObject("data", "hello!");
        return mav;
    }

    // 2 String 반환.
//    @ResponseBody // response/hello가 view로 안가고 그냥 문자로 반환.
    @RequestMapping("response-view-v2")
    public String  resonseViewV2(Model model) {
        model.addAttribute("data", "hello!");
        return "response/hello";
    }

    // 3. 권장하지않음
    @RequestMapping("/response/hello") // 경로가 url이랑 동일할떄
    public void resonseViewV3(Model model) {
        model.addAttribute("data", "hello!");
    }
}
