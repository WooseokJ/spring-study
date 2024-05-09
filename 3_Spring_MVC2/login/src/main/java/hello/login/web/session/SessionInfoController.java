package hello.login.web.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Slf4j
@RestController
public class SessionInfoController {
    @GetMapping("/session-info")
    public String sessionInfo(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session ==null) {
            return "세션 없음.";
        }

        // 세션데이터들 출력
        session.getAttributeNames().asIterator()
                .forEachRemaining(name -> log.info("session name ={}, value={}", name, session.getAttribute(name)));

        log.info("sessionID = {}", session.getId());
        log.info("getMaxInactiveInterval = {}", session.getMaxInactiveInterval()); // 비홣성화시키는 최대 간격
        log.info("creationTime = {}", new Date(session.getCreationTime())); //세션 생성시간.
        log.info("lastAccessTime = {}", new Date(session.getLastAccessedTime())); // 사용자가 마지막 세션접근시간.
        log.info("isNew= {}", session.isNew()); // 새로생성된 세션인지(true) 기존세션인지(false)(sessionId('JSESSIONID')요청해서 조회된 세션인지)

        return "세션 출력";
    }
}
