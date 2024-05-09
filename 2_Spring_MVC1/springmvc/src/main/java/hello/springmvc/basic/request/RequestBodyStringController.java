package hello.springmvc.basic.request;

// 3번 Http message Body에 데이터 담아 전송(Text, Json)

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

/** 단순 Text */
@RestController
@Slf4j
public class RequestBodyStringController {

    //1 . 스프링 기능사용안한것.
    @PostMapping("/request-body-string-v1") // 참고: get도 body 넣을수있지만 실무에선 잘안써
    public void requestBodyString(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);// 항상 byte -> String 변환시는 어떤 인코딩으로 바꿀지 지정해줘야해.
        log.info("messageBody = {}", messageBody);
        response.getWriter().write("ok");
    }

    // 2. 스프링 기능 사용
    // InputStream(Reader):  Http 요청 메세지 body의 내용 직접 조회
    // OutputStream(Writer): Http 응답 메세지 body의 결과 직접 출력
    @PostMapping("/request-body-string-v2")
    public void requestBodyStringV2(InputStream inputStream, Writer responseWrite) throws IOException {
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        log.info("messageBody = {}", messageBody);
        responseWrite.write("ok");
    }

    // 3. HttpEntity 사용
    @PostMapping("/request-body-string-v3")
    public HttpEntity<String> requestBodyStringV3(HttpEntity<String> httpEntity) throws IOException { // String은 HttpBody의 있는것을 문자로 변환
        String messageBody = httpEntity.getBody(); // http 메세지의 body 꺼냄.
        HttpHeaders headers = httpEntity.getHeaders(); // http 해더 정보 꺼냄
        log.info("messageBody = {}", messageBody);
        return new HttpEntity<>("ok"); //  <> 는 제네릭의미로 제네릭으로 전송할 데이터 타입을 명시가능 , ()는 생성자.
    }

    // 4. requestEntity, ResponseEntity 사용
    @PostMapping("/request-body-string-v4")
    public ResponseEntity<String> requestBodyStringV4(RequestEntity<String> httpEntity) throws IOException {
        String messageBody = httpEntity.getBody();
        HttpHeaders headers = httpEntity.getHeaders();
        log.info("messageBody = {}", messageBody);
        return new ResponseEntity<String>("ok",HttpStatus.CREATED);
    }

    // 5. @RequestBody사용
    @ResponseBody
    @PostMapping("/request-body-string-v5")
    public String requestBodyStringV5(@RequestBody String messageBody) throws IOException { // @ResponseBody가있으면 @ReqeustBody도 쓸수있다.
        log.info("messageBody = {}", messageBody);
        return "ok";
    }





}

