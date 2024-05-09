package study.querydsl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.Entity.Member;
import study.querydsl.Entity.QMember;
import study.querydsl.Entity.QTeam;
import study.querydsl.Entity.Team;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static study.querydsl.Entity.QMember.*;
import static study.querydsl.Entity.QTeam.*;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {
    @PersistenceContext
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    void before() {
        queryFactory = new JPAQueryFactory(em);

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member memberA = new Member("memberA", 10, teamA);
        Member memberB = new Member("memberB", 20, teamB);
        Member memberC = new Member("memberC", 30, teamA);
        Member memberD = new Member("memberD", 40, teamB);

        em.persist(memberA);
        em.persist(memberB);
        em.persist(memberC);
        em.persist(memberD);

    }

    @Test
    public void startJPQL() {
        // memberA 찾기
        Member findMember = em.createQuery("select m from Member as m where m.username = :username", Member.class)
                .setParameter("username", "memberA")
                .getSingleResult();
        assertThat(findMember.getUsername()).isEqualTo("memberA");
    }

    @Test
    public void startQuerydsl() {
//        QMember m = new QMember("m"); // 어떤 QMember인지 구분하기 위한 m
        Member findMember = queryFactory
                .select(member)
                .from(member)
                .where(member.username.eq("memberA")) // 파라미터 바인딩 처리.
                .fetchOne();
        assertThat(findMember.getUsername()).isEqualTo("memberA");

    }

    @Test
    public void search() {
        Member findMember = queryFactory.selectFrom(member)
                .where(
                        member.username.eq("memberA"),
                        member.age.eq(10)
                )
                .fetchOne();
        assertThat(findMember.getUsername()).isEqualTo("memberA");
    }

    @Test
    public void resultFetch() {
        // List
        List<Member> fetch = queryFactory
                .selectFrom(member)
                .fetch();
        // 하나
        Member member1 = queryFactory.selectFrom(member)
                .fetchOne();
        // 처음 하나
        Member member2 = queryFactory.selectFrom(member)
                .fetchFirst();
        //페이징에서 사용
        QueryResults<Member> results = queryFactory.selectFrom(member)
                .fetchResults();
        // totalcount
        long total1 = results.getTotal();// total count
        long total2 = queryFactory.selectFrom(member)
                .fetchCount(); // total count 만 뽑아냄.

        // 페이징 정보.
        List<Member> content = results.getResults();
    }
    // 나이 내림차순, 이름오름차순, 이름없으면 마지막에 null 출력
    @Test
    public void sort() {
        em.persist(new Member(null,100));
        em.persist(new Member("member5",100));
        em.persist(new Member("member6",100));

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(100))
                .orderBy(member.age.desc(),
                        member.username.asc().nullsLast()
                )
                .fetch();

        Member member5 = result.get(0);
        Member member6 = result.get(1);
        Member membernull = result.get(2);

        assertThat(member5.getUsername()).isEqualTo("member5");
        assertThat(member6.getUsername()).isEqualTo("member6");
        assertThat(membernull.getUsername()).isNull();

    }
    @Test
    public void paging1() {
        List<Member> result = queryFactory.selectFrom(member)
                .orderBy(member.username.desc())
                .offset(0) // 몇번쨰부터시작 0부터가 시작.
                .limit(2) // 2개 가져오기
                .fetch();

        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void paging2() {
        QueryResults<Member> results = queryFactory.selectFrom(member)
                .orderBy(member.username.desc())
                .offset(0) // 몇번쨰부터시작 0부터가 시작.
                .limit(2) // 2개 가져오기
                .fetchResults();
        assertThat(results.getTotal()).isEqualTo(4);
        assertThat(results.getLimit()).isEqualTo(2);
        assertThat(results.getOffset()).isEqualTo(0);
        assertThat(results.getResults().size()).isEqualTo(2);
    }

    @Test
    public void aggregation() {
        // tuple은 querydsl의 tuple이다. ( 실무에선 잘안쓰고 dto로 뽑아내는 방법을 더 자주사용)
        List<Tuple> result = queryFactory
                .select(
                        member.count(),
                        member.age.sum(),
                        member.age.avg(),
                        member.age.max(),
                        member.age.min()
                )
                .from(member)
                .fetch();

        Tuple tuple = result.get(0);
        assertThat(tuple.get(member.count())).isEqualTo(4);
        assertThat(tuple.get(member.age.sum())).isEqualTo(100);
        assertThat(tuple.get(member.age.avg())).isEqualTo(25);
        assertThat(tuple.get(member.age.max())).isEqualTo(40);
        assertThat(tuple.get(member.age.min())).isEqualTo(10);

    }
    @Test
    public void group() throws Exception {
        // 팀이름,각팀의 평균연령 구하기
        List<Tuple> result = queryFactory
                .select(team.name, member.age.avg())
                .from(member)
                .join(member.team, team)
                .groupBy(team.name)
                .fetch();

        Tuple teamA = result.get(0);
        Tuple teamB = result.get(1);

        assertThat(teamA.get(team.name)).isEqualTo("teamA");
        assertThat(teamA.get(member.age.avg())).isEqualTo(15); // (10 + 20 ) / 2

        assertThat(teamB.get(team.name)).isEqualTo("teamB");
        assertThat(teamB.get(member.age.avg())).isEqualTo(35); // (30+40) / 2

    }

    @Test
    public void join() {
        // teamA에 소속된 모든 회원
        List<Member> result = queryFactory
                .selectFrom(member)
                .join(member.team, team) // leftJoin, rightJoin 도둘다 가능.(지금은 innerJoin 쓴거)
                .where(team.name.eq("teamA"))
                .fetch();

        assertThat(result)
                .extracting("username")
                .containsExactly("memberA", "memberC");
    }

    @Test
    public void theta_join() { // 세타 조인(연관관계 없어도 조인가능)
        em.persist(new Member("teamA",10));
        em.persist(new Member("teamB",10));

        List<Member> result = queryFactory.select(member)
                .from(member, team)
                .where(member.username.eq(team.name))
                .fetch();
        assertThat(result)
                .extracting("username")
                .containsExactly("teamA","teamB");

    }
    // 회원과 팀 조인하면서 , 팀이름이 teamA인 팀만 조인 + 회원은 모두 조회.
    // select m , t from Member m left m.team t on t.name = 'teamA'


}
