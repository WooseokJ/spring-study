package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.SQLException;

// V3_2: 트랜잭션 템플릿
@RequiredArgsConstructor
@Slf4j
public class MemberServiceV3_2 {

    private final TransactionTemplate transactionTemplate;
    private final MemberRepositoryV3 memberRepository;

    public MemberServiceV3_2(PlatformTransactionManager transactionManager, MemberRepositoryV3 memberRepository) {
        // 밖에서 transaction 템플릿 주입안받는건 transactionTemplate가 클래스라서 다형성을 고려해서 transactionManager를 주입받음.
        this.transactionTemplate = new TransactionTemplate(transactionManager); // transaction 템플릿쓰려면 transaction 매니저 필요.
        this.memberRepository = memberRepository;
    }


    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        // executeWithoutResult 안에서 트랜잭션 시작되고 예외 유무로 종료된다.
        // 정상이면 commit
        // 언체크 예외면 롤백, 그이외는 commit(체크예외도 commit)
        transactionTemplate.executeWithoutResult((staus) -> {

            try {
                bizLogic(fromId, toId, money); // 예외발생시: SQLException 예외 발생.
            }catch (SQLException e) { // SQLException은 체크예외(람다밖으로 체크예외 던질수없어서 IllegalStateException(언체크예외)로 바꿈)
                throw new IllegalStateException(e);
            }

        });

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
