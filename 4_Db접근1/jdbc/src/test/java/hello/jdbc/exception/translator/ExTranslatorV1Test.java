package hello.jdbc.exception.translator;

import hello.jdbc.connection.ConnectionConst;
import hello.jdbc.domain.Member;
import hello.jdbc.repository.ex.MyDbException;
import hello.jdbc.repository.ex.MyDuplicateKeyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

import static hello.jdbc.connection.ConnectionConst.*;

@Slf4j
public class ExTranslatorV1Test {
    Repository repository;
    Serivce serivce;

    @BeforeEach
    void init() {
        DriverManagerDataSource datasource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        repository = new Repository(datasource);
        serivce = new Serivce(repository);
    }

    @Test
    void duplicateKeySave() {
        serivce.create("myid");
        serivce.create("myid"); // 같은 id 회원가입 시도
    }

    @RequiredArgsConstructor
    static class Serivce {
        private final Repository repository;
        public void create(String memberId) {
            try {
                Member member = new Member(memberId, 0);
                repository.save(member);
                log.info("saveId= {}", memberId);
            }catch (MyDuplicateKeyException e) {
                    log.info("키 중복 복구 시도.", e);
                String retryId = generateNewId(memberId);
                log.info("retryId= {}", retryId);
                Member newMember = new Member(retryId, 0);
                repository.save(newMember);
            } catch (MyDbException e) {
                log.info("데이터 접근 계층 예외", e);
            }


        }

        private String generateNewId(String memberId) {
            return memberId + new Random().nextInt(10000); // memerId+랜덤숫자.
        }
    }



    @RequiredArgsConstructor
    static class Repository {
        private final DataSource dataSource;

        public Member save(Member member) {
            String sql = "insert into member(member_id, money) values(?,?)";
            Connection connection = null;
            PreparedStatement pstmt = null;
            try {
                connection = dataSource.getConnection();
                pstmt = connection.prepareStatement(sql);
                pstmt.setString(1,member.getMemberId());
                pstmt.setInt(2, member.getMoney());
                pstmt.executeUpdate();
                return member;
           } catch (SQLException e) {
                // h2 db
                if( e.getErrorCode() == 23505) { // 키 중복
                    throw new MyDuplicateKeyException(e);
                }
                throw new MyDbException(e); // 다른 예외들은 이거 로 던짐
            } finally {
                JdbcUtils.closeStatement(pstmt);
                JdbcUtils.closeConnection(connection);
            }

        }



    }

}
