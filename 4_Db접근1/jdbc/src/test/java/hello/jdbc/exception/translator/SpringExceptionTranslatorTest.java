package hello.jdbc.exception.translator;

import hello.jdbc.connection.ConnectionConst;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static hello.jdbc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.*;

@Slf4j
public class SpringExceptionTranslatorTest {

    DataSource dataSource;
    @BeforeEach
    void init() {
        dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
    }

    // SQL ErrorCode를 직접 확인하는 방법(비추) -> 스프링이 제공하는 예외 변환기 사용.
    @Test
    void sqlExceptionErrorCode() {
        String sql = "select bad grammer";
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.executeQuery();
        }catch (SQLException e){
            assertThat(e.getErrorCode()).isEqualTo(42122); // sql 문법오류
            int errorCode = e.getErrorCode();
            log.info("errorCode= {}", errorCode);
            log.info("error= {}", e);
        }
    }


    // 스프링 예외 변환기
    @Test
    void exceptionTranslator() {
        String sql = "select bad grammer";
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.executeQuery();
        }catch (SQLException e){
            assertThat(e.getErrorCode()).isEqualTo(42122); // sql 문법오류
            SQLErrorCodeSQLExceptionTranslator exceptionTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource);

            // BadSQlGrammerException translate로 가져옴.
            DataAccessException resultEx = exceptionTranslator.translate("selct", sql, e); // 해석기
            log.info("resultEx ={}", resultEx);
            assertThat(resultEx.getClass()).isEqualTo(BadSqlGrammarException.class);
        }
    }

}
