package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepository;
import hello.jdbc.repository.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

// V4: 예외누수 해결
// SQLException 제거
// MEmberRepository 의존.
@RequiredArgsConstructor
@Slf4j
public class MemberServiceV4 {

    private final MemberRepository memberRepository;


    @Transactional
    public void accountTransfer(String fromId, String toId, int money) {
        bizLogic(fromId, toId, money);
    }

    private void bizLogic(String fromId, String toId, int money) {
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);
        try {
            memberRepository.update(fromId, fromMember.getMoney() - money);

            if (toMember.getMemberId().equals("ex")) { // toId가 ex이면 예외발생.
                throw new IllegalStateException("계좌이체 실패-이체중 예외발생");
            }
            memberRepository.update(toId, toMember.getMoney() + money);
        }catch (DuplicateKeyException e) {// 키중복
            // 키중복시 복구 로직 .
        }

    }


}
