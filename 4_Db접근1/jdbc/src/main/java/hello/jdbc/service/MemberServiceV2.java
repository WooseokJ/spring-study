package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV1;
import hello.jdbc.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

// V2: 트랜잭션 적용 (파라미터 연동 , 커넥션풀을 고려한 종료)
@RequiredArgsConstructor
@Slf4j
public class MemberServiceV2 {
    private final DataSource dataSource;
    private final MemberRepositoryV2 memberRepository;

    // 계좌이체 수행
    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        // 트랜잭션 시작하려면 Connection 필요.
        Connection connection = dataSource.getConnection();
        try {
            connection.setAutoCommit(false); // 트랜잭션 시작(기본값은 true)
            bizLogic(fromId, toId, money, connection); // 비즈니스 로직
            connection.commit(); // 트랜잭션 종료(커밋 혹은 롤백)
        }catch (Exception e) {
            //예외발생
            connection.rollback();
            throw new IllegalStateException(e);
        }finally {
            if(connection != null) {
                try {
                    connection.setAutoCommit(true); // 커넥션풀 고려해서 commit(트랜잭션종료)
                    connection.close(); // 커넥션 풀에 돌아감.
                } catch (Exception e) {
                    log.info("error", e);
                }
            }

        }
    }

    private void bizLogic(String fromId, String toId, int money, Connection connection) throws SQLException {
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);
        memberRepository.update(fromId, fromMember.getMoney() - money);
        if (toMember.getMemberId().equals("ex")) { // toId가 ex이면 예외발생.
            throw new IllegalStateException("계좌이체 실패-이체중 예외발생");
        }
        memberRepository.update(toId, toMember.getMoney() + money);
    }


}
