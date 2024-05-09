package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV0;
import hello.jdbc.repository.MemberRepositoryV1;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;

// V1: 트랜잭션 적용 X
@RequiredArgsConstructor
public class MemberServiceV1 {
    private final MemberRepositoryV1 memberRepository;

    // 계좌이체 수행
    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        // member 꺼내기(보내는자, 받는자)
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        // 보내는사람, 받는자 돈 로직
        memberRepository.update(fromId, fromMember.getMoney() - money);
        // 계좌이체 성공여부 검증과정.
        if (toMember.getMemberId().equals("ex")) { // toId가 ex이면 예외발생.
            throw new IllegalStateException("계좌이체 실패-이체중 예외발생");
        }
        memberRepository.update(toId, toMember.getMoney() + money);

    }


}
