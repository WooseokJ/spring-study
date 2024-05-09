package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.SQLException;

// V3_3: @Transactional aop
@RequiredArgsConstructor
@Slf4j
public class MemberServiceV3_3 {

    private final MemberRepositoryV3 memberRepository;


    @Transactional
    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        bizLogic(fromId, toId, money);
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
