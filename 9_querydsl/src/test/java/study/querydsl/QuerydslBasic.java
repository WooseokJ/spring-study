package study.querydsl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.Team;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static study.querydsl.entity.QMember.*;
import static study.querydsl.entity.QTeam.team;

@SpringBootTest
@Transactional

public class QuerydslBasic {
    @Autowired
    EntityManager em;

    @BeforeEach
    public void before() {
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

        // 초기화
        em.flush();
        em.clear();
    }

    @Test
    public void startJPQL() {
        // when
        Member findMember = em.createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", "member1")
                .getSingleResult();
        // then

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void startQuerydsl() {
        // give
        JPQLQueryFactory queryFactory = new JPAQueryFactory(em);
        QMember m = new QMember("m"); // 어떤 QMember인지 구분하기 위한 m
        // when
        Member findMember = queryFactory
                .select(m)
                .from(m)
                .where(m.username.eq("member1")) // 파라미터 바인딩 처리.
                .fetchOne();
        //then
        assertThat(findMember.getUsername()).isEqualTo("member1");

    }

    // 검색조건 쿼리
    @Test
    public void search() {
        JPQLQueryFactory queryFactory = new JPAQueryFactory(em);
        Member findMember = queryFactory
                .selectFrom(member) // qMember.member
                .where(member.username.eq("member1")
                        .and(member.age.between(10, 30 )))
                .fetchOne();
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void search2() { // 위 search()와 동일.
        JPQLQueryFactory queryFactory = new JPAQueryFactory(em);
        Member findMember = queryFactory
                .selectFrom(member) // qMember.member
                .where(
                        member.username.eq("member1"),
                        member.age.between(10, 30 )
                )
                .fetchOne();
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }
    @Test
    public void resultFetch() {
        JPQLQueryFactory queryFactory = new JPAQueryFactory(em);

        // List
        List<Member> fetch = queryFactory
                .selectFrom(member)
                .fetch();
        // 단건조회 (where절없으면 여러개가 되므로 오류발생, 즉 조건절 필요.)
        Member fetchOne = queryFactory
                .selectFrom(member)
                .where(member.age.eq(10))
                .fetchOne();
        // 다건이지만, 처음하나만 가져오기.
        Member fetchFirst = queryFactory.selectFrom(member)
                .fetchFirst();

        // totalcount, paging에서 사용.
        QueryResults<Member> results = queryFactory.selectFrom(member)
                .fetchResults();

        // totalcount( select count(member_id) from Member 와 동일.)
        long totalcount1 = results.getTotal();// total count
        long totalcount2 = queryFactory.selectFrom(member)
                .fetchCount(); // total count 만 뽑아냄.

        // 안의 정보
        List<Member> content = results.getResults();
    }
    /*
     * 회원 정렬 순서
     * 1. 회원 나이 desc
     * 2. 회원 이름 asc
     * 단 2에서 회원이름 없으면(null) 마지막에 출력
     *
     * */
    @Test
    public void sort() {
        em.persist(new Member(null, 100));
        em.persist(new Member("member5", 100));
        em.persist(new Member("member6", 100));

        JPQLQueryFactory queryFactory = new JPAQueryFactory(em);
        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(100))
                .orderBy(
                        member.age.desc(),
                        member.username.asc().nullsLast()
                )
                .fetch();
        Member member5 = result.get(0);
        Member member6 = result.get(1);
        Member memberNull = result.get(2);
        assertThat(member5.getUsername()).isEqualTo("member5");
        assertThat(member6.getUsername()).isEqualTo("member6");
        assertThat(memberNull.getUsername()).isNull();
    }
    // 페이징
    @Test
    public void paging1() {
        // 일부 가져오기 1부터 2개 가져옴
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        List<Member> result = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc()) // 이걸넣어줘야 페이징 잘작동하나 확인가능
                .offset(1) // 기본 0 부터 시작., 여기선 1부터 시작
                .limit(2) // 2개 가져옴
                .fetch();
        assertThat(result.size()).isEqualTo(2);

    }
    // 만약 전체 조회수가 필요하면? fetchResults 로 가져옴. (fetchResults를 쓰면 위에 result.size()도 구할수있다)
    @Test
    public void paging2() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QueryResults<Member> queryResults = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc()) // 이걸넣어줘야 페이징 잘작동하나 확인가능
                .offset(1) // 기본 0 부터 시작. 여기선 1부터 시작
                .limit(2) // 2개 가져옴
                .fetchResults();
        assertThat(queryResults.getTotal()).isEqualTo(4);
        assertThat(queryResults.getLimit()).isEqualTo(2);
        assertThat(queryResults.getOffset()).isEqualTo(1);
        assertThat(queryResults.getResults().size()).isEqualTo(2); // result.size()와 동일
    }
    // 집합 함수
    @Test
    public void aggregation() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        // Tuple은 querydsl에서 제공하는 타입.(실무에선 잘안쓰고 실제론 dto로 직접뽑아서 씀)
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
    /*
     * group by
     * 팀의 이름과 각 팀의 평균연령 구하기
     */
    @Test
    public void group() throws Exception{
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        List<Tuple> result = queryFactory
                .select(
                        team.name,
                        member.age.avg()
                )
                .from(member)
                .join(member.team, team)
                .groupBy(team.name)
//                .having(member.age.avg().gt(0)) // having절 사용가능. AVG(age) > 0
                .fetch();
        Tuple teamA = result.get(0);
        Tuple teamB = result.get(1);
        assertThat(teamA.get(team.name)).isEqualTo("teamA");
        assertThat(teamA.get(member.age.avg())).isEqualTo(20); // (10 + 30) / 2

        assertThat(teamB.get(team.name)).isEqualTo("teamB");
        assertThat(teamB.get(member.age.avg())).isEqualTo(30); // (20 + 40) / 2
    }


    // 조인 - 기본
    /*
        join(조인대상, 별칭으로사용할 Q타입)
     * TeamA 에 속한 모든회원 조회
     */
    @Test
    public void join() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        List<Member> result = queryFactory
                .selectFrom(member)
                .join(member.team, team)
                .where(team.name.eq("teamA"))
                .fetch();
        assertThat(result)
                .extracting("username")
                .containsExactly("member1", "member3");
    }
    // 세타조인(그냥막조인) - 연관관계 없어도 조인가능  ( 모든 회원 , 모든 팀 다 가져와서 조인하고 where절로 필터 ), 이렇게 세타조인하면 DB가 성능최적화해준다.
    /* 회원의 이름이 팀이름과 같은회원조회 **/
    // 주의: 외부조인(outer join)은 그냥은 불가능 -> on을 이용하면 외부조인가능.
    @Test
    public void theta_join() {
        em.persist(new Member("teamA"));;
        em.persist(new Member("teamB"));;

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        List<Member> result = queryFactory
                .select(member)
                .from(member, team) // from 절에 여러 엔티티 선택해서 세타조인시도.
                .where(member.username.eq(team.name))
                .fetch();
        assertThat(result)
                .extracting("username")
                .containsExactly("teamA", "teamB");
    }

}