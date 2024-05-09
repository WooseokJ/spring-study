package hello.springmvc.basic.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.springmvc.basic.HelloData;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


@Slf4j
@RestController
public class RequestBodyJsonController {
    /**
     *  request 요청내용 가정.
     *  {"username" : :"hello", "age" : 20}
     *  content-type: application/json
     * */

    private ObjectMapper objectMapper = new ObjectMapper();

    // 1. 기존
    @PostMapping("/request-body-json-v1")
    public void reqeustBodyJsonV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class); // messagebody읽어서 HelloData 객체로 반환.

        log.info("messageBody = {}", messageBody);
        log.info("username= {}, age= {} ", helloData.getUsername(), helloData.getAge());
        response.getWriter().write("ok");
    }

    // 2. @RequestBody로 만들기
    @PostMapping("/request-body-json-v2")
    public String reqeustBodyJsonV2(@RequestBody String messageBody) throws IOException {
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class); // messagebody읽어서 HelloData 객체로 반환.

        log.info("messageBody = {}", messageBody);
        log.info("username= {}, age= {} ", helloData.getUsername(), helloData.getAge());
        return "ok";
    }

    // 3. 객체를 파라미터로
    @PostMapping("/request-body-json-v3")
    public String reqeustBodyJsonV3(@RequestBody HelloData data) throws IOException { // 주의: @ReqeustBody는 생략하면안된다. (@ModelAttribute가 적용되서)
        log.info("username= {}, age= {} ", data.getUsername(), data.getAge());
        return "ok";
    }

    // 4.반환값 객체
    @PostMapping("/request-body-json-v4")
    public HelloData reqeustBodyJsonV4(@RequestBody HelloData data) throws IOException {
        log.info("username= {}, age= {} ", data.getUsername(), data.getAge());
        return data;
    }

    // 5. HttpEntity사용
    @PostMapping("/request-body-json-v5")
    public String reqeustBodyJsonV5(HttpEntity<HelloData> data) throws IOException {
        log.info("username= {}, age= {} ", data.getBody().getUsername(), data.getBody().getAge());
        return "ok";
    }

}
