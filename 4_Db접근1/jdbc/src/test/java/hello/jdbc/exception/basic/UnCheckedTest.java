package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@Slf4j
public class UnCheckedTest {

    // RuntimeException을 상속받은 예외는 언체크 예외가된다.
    static class MyUnchecktedException extends RuntimeException {
        public MyUnchecktedException(String message) {
            super(message);
        }
    }

    static class Service {
        // 언체크예외는 예외잡거나,던지지않아도됨(예외안잡으면 자동으로 던짐)
        Repository repository = new Repository();
        //예외 잡아서 처리
        public void callCatch() {
            try {
                repository.call();
            }catch (MyUnchecktedException e) {
                log.info("예외처리 message = {}", e.getMessage(), e);
            }
        }


        // 예외 던지기(언체크라 던지는 코드 안적어도됨)
        public void callThrow() {
            repository.call();
        }

    }
    static class Repository {
        public void call() { // 언체크 예외라서 throw 안써줘도 자동으로 예외 던짐.
            throw new MyUnchecktedException("ex");
        }
    }

    // 테스트 시도

    Service service = new Service();
    @Test
    void unchecked_catch() {
        service.callCatch();
    }

    @Test
    void unchecked_throw() {

        assertThatThrownBy(() -> service.callThrow())
                .isInstanceOf(MyUnchecktedException.class);
    }
}
