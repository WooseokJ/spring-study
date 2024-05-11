package study.querydsl.repository;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.dto.MemberCond;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.entity.Member;
import study.querydsl.entity.Team;

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
    // 동적쿼리 - booleanBuilder, where절 동작확인.
    @Test
    public void searchTest() {
        //given
        insertData();
        //when
        MemberCond cond = new MemberCond();
        cond.setAgeGoe(35);
        cond.setAgeLoe(40);
        cond.setTeamName("teamB");

        // then
        List<MemberTeamDto> result = memberRepository.searchByBuilder(cond);
//        List<MemberTeamDto> result = memberRepository.searchByWhere(cond);
        assertThat(result).extracting("username").containsExactly("member4");
    }
    private void insertData(){
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamB);
        Member member3 = new Member("member3", 30, teamA);
        Member member4 = new Member("member4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }
}