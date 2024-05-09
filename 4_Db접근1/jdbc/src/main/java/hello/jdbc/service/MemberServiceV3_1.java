package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV2;
import hello.jdbc.repository.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

// V3_1: 트랜잭션 매니저
@RequiredArgsConstructor
@Slf4j
public class MemberServiceV3_1 {

    // PlatformTransactionManager는 인터페이스
    // 현재 JDBC 쓰므로 transactionManager는 new DataSourceTransactionManager를 외부주입받음
    // (JPA이면 JPATransactionManger)
    private final PlatformTransactionManager transactionManager;
    private final MemberRepositoryV3 memberRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        // 트랜잭션 시작
        // DefaultTransactionDefinition은 트랜잭션 관련 옵션 지정가능.
        // status는 트랜잭션 종료시 사용.
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            bizLogic(fromId, toId, money); // 비즈니스 로직
            transactionManager.commit(status); // 트랜잭션 성공
        }catch (Exception e) {
            transactionManager.rollback(status); // 트랜잭션 실패
            throw new IllegalStateException(e);
        }
    }

    private void bizLogic(String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);
        memberRepository.update(fromId, fromMember.getMoney() - money);
        if (toMember.getMemberId().equals("ex")) { // toId가 ex이면 예외발생.
            throw new IllegalStateException("계좌이체 실패-이체중 예외발생");
        }
        memberRepository.update(toId, toMember.getMoney() + money);
    }


}
