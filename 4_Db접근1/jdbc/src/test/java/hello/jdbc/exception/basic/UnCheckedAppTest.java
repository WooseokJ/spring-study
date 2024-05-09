package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.*;

public class UnCheckedAppTest {

    static class RuntimeConnectException extends RuntimeException {
        public RuntimeConnectException(String message) {
            super(message);
        }
    }
    static class RuntimeSQLException extends RuntimeException {
        public RuntimeSQLException(Throwable cause) { // 예외의 Throwable 최상위 클래스(언체크,체크 포함)
            super(cause);
        }
    }

    static class Repository {
        public void call() {
            try {
                runSQL();
            } catch (SQLException e) {
                // 런타임 예외 던질떄 기존예외도 같이넣어줘야해
                throw new RuntimeSQLException(e);
            }
        }

        public void runSQL() throws SQLException {
            throw new SQLException("ex");
        }
    }

    static class NetworkClient {
        public void call() {
            throw  new RuntimeConnectException("연결 실패");
        }
    }

    static class Service {
        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();
        void logic() {
            repository.call();
            networkClient.call();
        }
    }
    static class Controller {
        Service service = new Service();

        void request() {
            service.logic();
        }
    }

    // 테스트 시작

    @Test
    void unChecked() {
        Controller controller = new Controller();
        assertThatThrownBy(() -> controller.request())
                .isInstanceOf(RuntimeSQLException.class);
    }
}
