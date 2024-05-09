package jpabook.jpashop.service;

import jpabook.jpashop.api.MemberAPIController;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // 전체 메서드에 이 어노테이션이 붙는다., 읽기전용은 성능상 이점이있다.
//@AllArgsConstructor //lombok  : public MemberService(MemberRepository memberRepository)를 안적어줘도 만들어준다.
@RequiredArgsConstructor // final붙인거는 생성자(public MemberService(MemberRepository memberRepository) 만들어줌.
public class MemberService {


    private final MemberRepository memberRepository; // final을 붙이면 생성자에서 값을 안넣어주면 빨간줄로 알려줘서 붙이는거.

//    @Autowired // 생성자 인젝션  , 생성자 딱 1개이면 @Autowired 자동으로 붙여줌
//    public MemberService(MemberRepository memberRepository){
//        this.memberRepository = memberRepository;
//    }

    // 회원가입
    @Transactional // 조회할떄는 읽기전용이여도 되는데 쓰기 영역에서는 수정되야하므로 Transactional 따로 붙여줌.(따로붙인게 우선권을 갖는다.)
    public Long join(Member member) {
        validateDuplicateMember(member); //중복회원검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        // Exception
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원");
        }
    }

    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    // id로 하나 조회
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    // 회원 업데이트
    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);
        member.setName(name);
        // memberRepository.save(member) // 안해줘도되는게 변경감지떄문.
    }

}
