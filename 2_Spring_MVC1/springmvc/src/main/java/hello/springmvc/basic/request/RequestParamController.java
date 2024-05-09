package hello.springmvc.basic.request;

import hello.springmvc.basic.HelloData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

//@RestController // @Controller + @ResponseBody
@Controller
@Slf4j
public class RequestParamController {
    // 클라 -> 서버로 데이터 보내는 3가지 방법잇었다.

    // 이전에 getParameter로 1,2번 둘다 꺼냄.
    @RequestMapping("/request-param-v1")
    public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));
        log.info("usernae= {} , age =  {}", username, age);
        response.getWriter().write("ok");
    }

    /** @RequestParam */

    // 스프링에서 지원하는 방법(@RequestParam)
    @ResponseBody
    @RequestMapping("/request-param-v2")
    public String requestParamV2(
            @RequestParam String username, //@RequestParam("username") String memberName,
            @RequestParam("age") int memberAge
    ) {
        log.info("username = {} , age = {}", username, memberAge);
        return "ok";
    }

    // 3.스프링에서 지원하는 방법 축약
    @ResponseBody
    @RequestMapping("/request-param-v3")
    public String requestParamV3(String username, int age) { // 대신 HTTP 파라미터 명이 변수명같고 타입이 기본타입(String,Int,Integer) 이면 생략가능
        log.info("username = {} , age = {}", username, age);
        return "ok";
    }

    // 4. 필수 파라미터 줌.
    @ResponseBody
    @RequestMapping("/request-param-required")
    public String requestParamRequried(@RequestParam(required = true) String username, // (반드시 username 파라미터값있게 요청보내야함)
                                       @RequestParam(required = false) Integer age) { // int = null (x) int는 null 이없다 그래서 안적어도 요청이 성공하려면 Integer로 사용.(Integer는 객체라 null가능)
        log.info("username = {} , age = {}", username, age);
        return "ok";
    }

    // 5. defaultValue
    @ResponseBody
    @RequestMapping("/request-param-default")
    public String requestParamDefaultValue(@RequestParam(required = true, defaultValue = "guest") String username,
                                       @RequestParam(required = false, defaultValue = "-1") int age) {
        log.info("username = {} , age = {}", username, age);
        return "ok";
    }

    // 6. map
    @ResponseBody
    @RequestMapping("/request-param-map")
    public String requestParamMap(@RequestParam MultiValueMap<String, Object> paramMap) {
        log.info("username = {} , age = {}", paramMap.get("username"), paramMap.get("age"));
        return "ok";
    }



    /** @ModelAttribute */


    // 요청 파라미터 받아 -> custom 객체에 값넣어주는 방법
    // @ModelAttribute 사용 x
    @ResponseBody
    @RequestMapping("/model-attribute-v1")
    public String modelAttributeV1(@RequestParam String username,
                                   @RequestParam int age) {
        HelloData data = new HelloData();
        data.setAge(age);
        data.setUsername(username);
        log.info("helloData = {}", data); // @ToSting때문에 객체 출력시 자동으로 data.toString()( 이쁘게 출력).
        return "ok";
    }

    // @ModelAttribute 사용
    @ResponseBody
    @RequestMapping("/model-attribute-v2")
    public String modelAttributeV2(HelloData data) { //HelloData data로 생략가능
        log.info("helloData = {}", data); // @ToSting때문에 객체 출력시 자동으로 data.toString()( 이쁘게 출력).
        return "ok";
    }




}
