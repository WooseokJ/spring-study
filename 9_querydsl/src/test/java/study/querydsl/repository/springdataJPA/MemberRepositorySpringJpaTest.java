package study.querydsl.repository.springdataJPA;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.dto.MemberCond;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.Team;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static study.querydsl.entity.QMember.member;


@SpringBootTest
@Transactional
class MemberRepositorySpringJpaTest {
    @Autowired
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    // 스프링 데이터 jpa 로 만든 메서드 동작 확인.
    @Test
    public void basicJPQLTest() {
        // save 메서드 동작 확인. (spring data JPA가 제공).
        Member member = new Member("member1", 10);
        memberRepository.save(member);

        // findbyId 메서드 동작확인 (spring data JPA가 제공).
        Member findMember = memberRepository.findById(member.getId()).get();
        assertThat(findMember).isEqualTo(member);

        // findAll 메서드 동작확인(spring data JPA가 제공).
        List<Member> allMember = memberRepository.findAll();
        assertThat(allMember).containsExactly(member);

        // findByUser메서드 동작확인
        List<Member> findMembers = memberRepository.findByUsername(member.getUsername());
        assertThat(findMembers).containsExactly(member);
    }
    // 동적쿼리 만든거 where절 파라미터로 메서드 동작 확인.
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
        List<MemberTeamDto> result = memberRepository.search(cond);
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

    /**스프링 데이터 jpa가 제공하는 Page이용해 만든 쿼리*/
    // 최적화 이전
    @Test
    public void searchPageSimple() {
        //given
        insertData();
        //when
        MemberCond cond = new MemberCond();
        PageRequest pageRequest = PageRequest.of(0, 3);


        // then
        Page<MemberTeamDto> result = memberRepository.searchSimple(cond, pageRequest);
        assertThat(result.getSize()).isEqualTo(3);
        assertThat(result.getContent()).extracting("username").containsExactly("member1", "member2", "member3");
    }
    // 최적화 이후
    @Test
    public void searchPageComplex() {
        //given
        insertData();
        //when
        MemberCond cond = new MemberCond();
        PageRequest pageRequest = PageRequest.of(0, 3);


        // then
        Page<MemberTeamDto> result = memberRepository.searchComplex(cond, pageRequest);
        assertThat(result.getSize()).isEqualTo(3);
        assertThat(result.getContent()).extracting("username").containsExactly("member1", "member2", "member3");
    }

    /**querydsl이 지원하는 기능들*/
    // QuerydslPredicateExecutor 이용
    // 한계점:
    // 1.묵시적조인은 가능하지만 left join불가
    // 2. querydsl에 의존적.
    // 3. 실무에서 사용하기에 한계있음.(left join이 안되는게 크다)
    @Test
    public void querydslPredicateExecutor() {
        insertData();
        Iterable<Member> result = memberRepository.findAll(
                member.age.between(10, 40)
                        .and(member.username.eq("member3"))
        );
        for (Member member1 : result) {
            System.out.println("member3 = " + member1);
        }
        //member3 = Member(id=3, username=member3, age=30)
    }

}