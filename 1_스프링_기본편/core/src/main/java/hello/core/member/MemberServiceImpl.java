package hello.core.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component // 컴포넌트 대상이되어 스프링빈에 등록이된다. (문제는  memberRepository 의존관계 주입을 해줘야하는데 못해줘서 @autoWired를 사용.)
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    // 마치 ac.getBean(MemberRepository.class) 처럼 동작)
    @Autowired // 자동 으로 의존관계 주입시켜줌
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }

    // 테스트 용도
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
