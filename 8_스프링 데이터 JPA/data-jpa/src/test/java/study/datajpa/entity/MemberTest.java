package study.datajpa.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.repository.MemberRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberTest {
    @PersistenceContext
    EntityManager em;

    @Test
    public void testEntity() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamB);
        em.persist(teamA);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        em.flush(); // 강제로 쿼리 날림
        em.clear(); // 캐시 날림.

        // 확인
        List<Member> memebers = em.createQuery("select m from Member m", Member.class).getResultList();
        for (Member m : memebers) {
            System.out.printf("member = " + m);
            System.out.println("-> member.team =" + m.getTeam());
        }
    }

    @Autowired private MemberRepository memberRepository;
    @Test
    public void JpaEventBaseEntity() {
        Member member = new Member("member1");
        memberRepository.save(member);
        member.setUsername("member2");
        em.flush(); //@preupdate
        em.clear();

        //when
        Member findMember = memberRepository.findById(member.getId()).get();

        System.out.println("findMember.createDate = " + findMember.getCreateDate());
        System.out.println("findMember.updateDate = " + findMember.getLastModiriedDate());
        System.out.println("findMember.getCreateBy = " + findMember.getCreateBy());
        System.out.println("findMember.getLastModifiedBy() = " + findMember.getLastModifiedBy());

    }

}