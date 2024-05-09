package hello.springmvc.basic.requestMapping;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class MappingController {
    // 참고: /hello-basic  과 /hello-basic/ 은 서로다른 요청이지만 스프링은 같은요청으로 매핑

    // 1. url 요청오면 RequestMapping 붙은 메서드 실행된다.
    @RequestMapping({"/hello-basic", "hello-go"}) // 배열로 다중설정가능.
    public String helloBasic() {
        log.info("helloBasic");;
        return "ok";
    }
    // 2.
    @GetMapping("hello-get-v2") // 만약 Post로 요청을 보내면 스프링은 405  상태로코를 반환한다.
    public String mappingGetV2() {
        return "ok2";
    }

    // 최근 HTTP API는 리소스 경로에 식별자 넣는 스타일 많음. ex) mapping/userA

    // 3. PathVariable(경로변수) 다중 사용.(많이사용)
    @GetMapping("/mapping/{userId}/order/{orderId}")
    public String mappingPath(@PathVariable String userId, @PathVariable Long orderId) { // @PathVariable("userId") String id 와 동일(변수명다르게할떄 쓰기)
        log.info("mappingPath userid = {}, orderid = {}", userId, orderId);
        return "ok MultiPath";
    }


    /** params 조건
     * params = "mode"  // mode가 포함되야함
     * params = "!mode" //mode가 안들어가야한다.
     * params = "mode=debug" // ?mode=debug 요청 포함되야함.
     * params = "mode!=debug"
     * params = {"mode=debug", "data=good"} mode,data파라미터가 값들이 debug,good이여야하는경우.
     * */

    // 4. 특정파라미터 조건 매핑(자주사용x) : url: localhost:8008/mapping-param?mode-debug
    @GetMapping(value = "/mapping-param", params = "mode=debug") // mode-debug가 꼭있어야 요청가능.(없으면 400에러)
    public String mappingParam() {
        return "ok mappingParam";
    }

    // 5. headers 조건 매핑
    @GetMapping(value = "/mapping-header", headers = "mode=debug")  // key: value 형태
    public String mappingString() {
        return "ok header";
    }

    /**
     * Content-type 헤더 기반 추가 매핑 미디어 타입
     * consumes = "application/json"
     * consumes = "!application/json"
     * consumes = "application/*"
     * consumes = "*\/*"
     * consumes = MediaType.APPLICATION_JSON_VALUE

     * consumes = {"text/plain", "application/*"} 여러개 가능 .
     * */

    // HTTP요청중 Content-type 헤더를 기반으로 미디어타입을 매핑한다. 안맞으면 415
    // 6. 미디어 타입 조건을 매핑 - Http요청중 Contrent-type, headers대신 consume 사용.
    @PostMapping(value = "/mapping-consume", consumes = "application/json") // Content-Type: application/json
    public String mappingConsumes() {
        return "ok consume";
    }

    // HTTP요청중 Accept 헤더를 기반으로 미디어타입을 매핑한다. 안맞으면 406
    // 7. 미디어 타입 조건을 매핑 - Http 요청중 Accept 제약줄수있다., headers대신 produce 사용.
    @PostMapping(value = "/mapping-produce", produces = "text/html") // Accept : text/html
    public String mappingProduces() {
        return "ok produce";
    }

    // ex) produces = {text/plain;charset=UTF-8, "application/*"}
}
