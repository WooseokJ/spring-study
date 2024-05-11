package study.querydsl.repository;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {
    @Autowired
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    // jpql 로 만든 repository 메서드 확인.
    @Test
    public void basicJPQLTest() {
        // save 메서드 동작 확인.
        Member member = new Member("member1", 10);
        memberRepository.save(member);

        // findbyId 메서드 동작확인.
        Member findMember = memberRepository.findById(member.getId()).get();
        assertThat(findMember).isEqualTo(member);

        // findAll 메서드 동작확인
        List<Member> allMember = memberRepository.findAll();
        assertThat(allMember).containsExactly(member);

        // findByUser메서드 동작확인 (이름동일한게 여러개일수있으니 List배열)
        List<Member> findMembers = memberRepository.findbyUsername(member.getUsername());
        assertThat(findMembers).containsExactly(member);
    }
    // querydsl로 만든 repository메서드 확인.
    @Test
    public void basicQuerydslTest() {
        Member member = new Member("member1", 10);
        memberRepository.save(member);

        // findAll 메서드 동작확인
        List<Member> allMember = memberRepository.findAll();
        assertThat(allMember).containsExactly(member);

        // findByUser메서드 동작확인 (이름동일한게 여러개일수있으니 List배열)
        List<Member> findMembers = memberRepository.findbyUsername(member.getUsername());
        assertThat(findMembers).containsExactly(member);
    }

}