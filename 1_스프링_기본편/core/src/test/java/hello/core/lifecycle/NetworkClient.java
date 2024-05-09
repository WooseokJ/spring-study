package hello.core.lifecycle;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Setter;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

@Setter
public class NetworkClient  {
    private String url;

    public NetworkClient() {
        System.out.println("생성자 호출 url = " + url);
    }

    // 서비스 시작시 호출
    public void connect() {

        System.out.println("connect = " + url);
    }

    public void call(String message) {

        System.out.println("call: "+ url + " message = " + message);
    }

    // 서비스 종료시 호출
    public void disconnect() {

        System.out.println("연결끊김" + url);
    }

    // 의존관계 주입끝나면 호출됨.
    @PostConstruct
    public void init() {
        connect();
        call("초기화 연결 메세지");
    }
    // 소멸시 호출.
    @PreDestroy
    public void close() {
        disconnect();
    }
}
