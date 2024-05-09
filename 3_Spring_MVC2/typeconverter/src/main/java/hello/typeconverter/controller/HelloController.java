package hello.typeconverter.controller;

import hello.typeconverter.type.IpPort;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    // 스프링이 타입변환 안해줌.(직접해야함)
    @GetMapping("/hello-v1")
    public String helloV1(HttpServletRequest request) {
        // http 요청 파라미터는 모두 문자로 처리된다.

        String username = request.getParameter("data");// data라는 파라미터 오면 문자타입으로 조회
        Integer intTypeUsername = Integer.valueOf(username); // 문자 -> 숫자
        System.out.println("intTypeUsername = " + intTypeUsername);
        return "ok v1";

    }

    // 스프링이 타입변환 해줌
    @GetMapping("/hello-v2") // /hello-v2?data=10  ,
    public String helloV2(@RequestParam Integer data){ // 10은 문자인데 @RequestParam + 변환할타입 하면 10이 타입이 변환된다.(스프링이 타입을 변환해준다)
        System.out.println("data = " + data);
        return "ok v2 ";
    }
    // 아래 2개도 스프링이 중간에 타입컨버터통해 타입변환해줌.
    // @ModelAttribute UserData data
    // @PAthVariable("data") Integer data


    @GetMapping("/ip-port") // /ip-port?ipPort=127.0.0.1:8080
    public String ipPort(@RequestParam IpPort ipPort) {
        System.out.println("ip = " + ipPort.getIp());
        System.out.println("port = " + ipPort.getPort());
        return "ok ipport";
    }
}
