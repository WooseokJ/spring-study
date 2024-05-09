package hello.jdbc.connection;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static hello.jdbc.connection.ConnectionConst.*;

@Slf4j
public class ConnectionTest {

    // DriverManager와 DriverManagerDataSource 사용
    @Test
    void driverManager() throws SQLException {
        // 일반 DriverManager (Datasource사용x)
//        Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
//        Connection connection2 = DriverManager.getConnection(URL, USERNAME, PASSWORD); // 커넥션 2개 얻게됨.
//        log.info("conntion = {}", connection);
//        log.info("conntion = {}", connection2);

        // spring제공하는 DriverManagerDataSource(datasoruce사용) - 항상 새로운 커넥션획득
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        useDataSource(dataSource);

    }

    private void useDataSource(DataSource dataSource) throws SQLException {
        Connection connection = dataSource.getConnection(); // 커넥션 1
        Connection connection2 = dataSource.getConnection(); // 커넥션 2
        log.info("conntion = {}", connection);
        log.info("connection class = {}", connection.getClass());
        log.info("conntion = {}", connection2);
        log.info("connection class = {}", connection2.getClass());
    }
    // 커넥션풀 사용
    @Test
    void dataSourceConnectionPoll() throws SQLException, InterruptedException {
        // 커넥션 풀링
        // spring에서 JDBC쓰면 자동으로 Hikari 를 가져오고 Hikari의 데이터소스 가져온거
        // HikariDataSource는 Datasource 구현체
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(URL);
        hikariDataSource.setUsername(USERNAME);
        hikariDataSource.setPassword(PASSWORD);
        hikariDataSource.setMaximumPoolSize(10); // 풀개수
        hikariDataSource.setPoolName("MyPool"); // 지정안하면 default 이름 나옴.

        useDataSource(hikariDataSource);
        Thread.sleep(3000);

    }
}
