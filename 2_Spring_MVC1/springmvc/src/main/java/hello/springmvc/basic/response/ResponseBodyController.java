package hello.springmvc.basic.response;

import hello.springmvc.basic.HelloData;
import jakarta.servlet.ServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

//@Controller
@RestController
public class ResponseBodyController {

    // 3. 응답: HTTP text 데이터로 전달 .

    @GetMapping("response-body-string-v1")
    public void responseBodyV1(ServletResponse response) throws IOException {
        response.getWriter().write("ok");
    }

    @GetMapping("response-body-string-v2")
    public ResponseEntity<String> responseBodyV2() throws IOException {
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

//    @ResponseBody
    @GetMapping("response-body-string-v3")
    public String responseBodyV3() {
        return "ok";
    }

    // 3.응답: HTTP json 데이터로 전달 .
    @GetMapping("response-body-json-v1")
    public ResponseEntity<HelloData> responseBodyJsonV1() {
        HelloData helloData = new HelloData();
        helloData.setUsername("userA");
        helloData.setAge(10);
        return new ResponseEntity<>(helloData, HttpStatus.OK);
    }

    // 실전에서 많이씀.
    @ResponseStatus(HttpStatus.OK) // 상태코드 데이터도 같이 전달.
//    @ResponseBody
    @GetMapping("response-body-json-v2")
    public HelloData responseBodyJsonV2() {
        HelloData helloData = new HelloData();
        helloData.setUsername("userA");
        helloData.setAge(10);
        return helloData;
    }

}
