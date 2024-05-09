package hello.jdbc.connection;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


@Slf4j
public class DBConnectionUtil {

    public static Connection getConnection() { // Connection: JDBC 인터페이스에서 제공
        try {
            // JDBC에서 DriverManager는 제공한다.(이를통해 라이브러리에있는 DB드라이버(구현체)를 찾아서 커넥션으로 반환해주고 DB연결)
            Connection connection = DriverManager.getConnection(ConnectionConst.URL, ConnectionConst.USERNAME, ConnectionConst.PASSWORD);
            log.info("get connection={}", connection);
            // class=class org.h2.jdbc.JdbcConnection(h2사용할떄, 이게 h2 드라이버가 제공하는 H2전용 커넥션)
            // 이는 java.sql.Connection 인터페이스를 구현하고있다.
            log.info("class={}", connection.getClass());
            return connection;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }

    }
}
