package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional // 기본적으로 rollback이 된다 .
class MemberServiceTest {

    @Autowired MemberRepository memberRepository;
    @Autowired MemberService memberService;

    @Autowired EntityManager em;

    @Test
    @Rollback(value = false) //rollback안하고 commit한다.
    public void 회원가입() throws Exception {
        // given
        Member member = new Member();
        member.setName("kim");

        // when
        Long saveId = memberService.join(member);

        // then
        em.flush();
        Assertions.assertEquals(member, memberRepository.findOne(saveId));

    }

    @Test
    public void 중복회원예외() throws Exception {
        // given
        Member member = new Member();
        member.setName("kim1");
        Member member2 = new Member();
        member2.setName("kim1");

        // when
        memberService.join(member);

        // 방법 1
//        try {
//            memberService.join(member2); // 여기서 예외 발생해야한다.
//        } catch (IllegalStateException e) {
//            return;
//        }

        // 방법 2
        Assertions.assertThrows(IllegalStateException.class, () -> {
            memberService.join(member2);
        });


        // then



    }


}