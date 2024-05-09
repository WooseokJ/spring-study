package hello.springmvc.basic;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // return시 String이 반환 (api만들떄는 이거사용)
// @Controller는 return시 view이름을 반환하고 ViewResolver과정거친다.
@Slf4j
public class LogTestController {

//    private final Logger log = LoggerFactory.getLogger(getClass());  // @slf4쓰면 안써줘도된다. (자동으로제공)

    @GetMapping("/log-test")
    public String logTest() {
        String name = "spring";
        System.out.println(); // 이건 로그레벨 상관없이 무조건 로그에남아

        // log 단계 (trace , debug, info, warn, error 순서)
        log.trace("trace log = {}", name); //  추적
        log.debug("debug log = {}", name); // 개발서버에서 보는거
        log.info("로그기록(시간, 프로세스id, 실행한쓰레드, 나의Controller이름) 출력 = {}", name); // 중요정보
        log.warn("warn log = {}", name); // 경고
        log.error("error log = {}", name); // 에러야
        return "ok";
    }

}