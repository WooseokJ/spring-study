package hello.jdbc.exception.basic;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

public class CheckedAppTest {

    static class Repository {
        public void call() throws SQLException {
            throw new SQLException("ex");
        }
    }

    static class NetworkClient {
        public void call() throws ConnectException {
            throw new ConnectException("연결 실패");
        }
    }

    static class Service {
        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();

        public void login() throws SQLException, ConnectException { // 처리 못해서 던짐.

            repository.call();
            networkClient.call();


        }
    }

    static class Controller {
        Service service = new Service();
        public void request() throws SQLException, ConnectException { // 처리 못해서 던짐.
            service.login();
        }
    }

    // 테스트 실행
    @Test
    void checked() {
        Controller controller = new Controller();
        Assertions.assertThatThrownBy(() -> controller.request())
                .isInstanceOf(SQLException.class); // Service안의 login을 보면 SQLException 에러가 가장먼저 나서 성공(ConnectionException으로 하면 테스트실패)
    }
}
