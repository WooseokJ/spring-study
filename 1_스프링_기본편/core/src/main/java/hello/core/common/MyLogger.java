package hello.core.common;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS) // http 요청당 하나씩 생성. 요청끝나면 소멸.
@Setter // 생성시점에 requestURL 알수없으므로 외부에서 setter로 입력받기위함.
public class MyLogger {// 로그출력 클래스
    private String uuid;
    private String requestURL;
    public void log(String message) {
        System.out.println("[" + uuid + "]" + "["+ requestURL+"]"+message);
    }

    @PostConstruct
    public void init() {
        uuid = UUID.randomUUID().toString();
        System.out.println("[" + uuid + "]" + " request score bean create" + this);
    }
    @PreDestroy
    public void close() {
        System.out.println("[" + uuid + "]" + " request score bean close" + this);
        System.out.println();
    }
}
