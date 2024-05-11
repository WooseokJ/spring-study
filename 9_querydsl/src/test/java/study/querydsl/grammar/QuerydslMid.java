package study.querydsl.grammar;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.annotations.QueryProjection;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.dto.MemberDto;
import study.querydsl.dto.QMemberDto;
import study.querydsl.dto.UserDto;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.Team;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.BDDAssertions.and;
import static study.querydsl.entity.QMember.*;

@SpringBootTest
@Transactional

public class QuerydslMid {
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

    /***프로젝션 결과 반환 - 기본 */
    // 프로젝션 결과 1개인경우
    @Test
    public void simpleProjection() {
        JPQLQueryFactory queryFactory = new JPAQueryFactory(em);
        List<String> result = queryFactory
                .select(member.username)
                .from(member)
                .fetch();
        for (String s : result) {
            System.out.println("s = " + s);
        }
        //s = member1
        //s = member2
        //s = member3
        //s = member4
    }

    // 프로젝션 결과 2개이상인경우 - 튜플
    // 설꼐상 Tuple타입은 repository계층에서만 쓰는게 좋음. (service, controller계층까진 비추)
    @Test
    public void tupleProjection() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        List<Tuple> result = queryFactory
                .select(member.username, member.age)
                .from(member)
                .fetch();
        for (Tuple tuple : result) {
            String username = tuple.get(member.username);
            Integer age = tuple.get(member.age);

        }
    }

    // 프로젝션 결과 2개이상인경우 - dto

    // jpql로 dto가져오려면? - package이름 다 적어줘야하고지저분, 생성자가 꼭 있어야해.
    @Test
    public void findDtoByJPQL() {
        // 그냥 select m.username, m.age from Member m , MemberDto.class 로 만들순없다.(꼭 new 로 만들어야해)
        List<MemberDto> resultList = em.createQuery("select new study.querydsl.dto.MemberDto(m.username, m.age)" +
                        " from Member m ", MemberDto.class)
                .getResultList();
        for (MemberDto memberDto : resultList) {
            System.out.println("memberDto = " + memberDto);
        }


    }
    // 위 jpql문제를 querydsl은 지원.
    // 방법이 3가지 있다.
    // 1. Setter방식
    @Test
    public void findDtoBySetter() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        List<MemberDto> result = queryFactory
                .select(Projections.bean(MemberDto.class, // 주의: MemberDto 기본생성자 필수.( setter를 통해 값이들어가는것이라 setter필수)
                        member.username,
                        member.age
                ))
                .from(member)
                .fetch();
        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    // 2. field방식
    @Test
    public void findDtoByFiled() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        List<MemberDto> result = queryFactory
                .select(Projections.fields(MemberDto.class, // 얘도 기본생성자 필수, setter없어도 가능.
                        member.username,
                        member.age
                ))
                .from(member)
                .fetch();
        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    // 3. 생성자 접근방법
    @Test
    public void findDtoByConstructor() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        List<MemberDto> result = queryFactory
                .select(Projections.constructor(MemberDto.class, // 얘도 기본생성자 필수
                        member.username,
                        member.age
                ))
                .from(member)
                .fetch();
        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    // 주의
    @Test
    public void findUserDtoProblem() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        List<UserDto> result = queryFactory
                .select(Projections.fields(UserDto.class, // 얘도 기본생성자 필수
                        member.username,
                        member.age
                ))
                .from(member)
                .fetch();

        for (UserDto userDto : result) {
            System.out.println("userDto = " + userDto);
        }
        // 다음과같이 null로 뜬다.
        //userDto = UserDto(name=null, age=10)
        //userDto = UserDto(name=null, age=20)
        //userDto = UserDto(name=null, age=30)
        //userDto = UserDto(name=null, age=40)
    }
    // 해법 1 - 필드 별칭 적용
    @Test
    public void findUserDto1() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        List<UserDto> result = queryFactory
                .select(Projections.fields(UserDto.class,
                        member.username.as("name"),
                        member.age
                ))
                .from(member)
                .fetch();

        for (UserDto userDto : result) {
            System.out.println("userDto = " + userDto);
        }
        // 정상
//        userDto = UserDto(name=member1, age=10)
//        userDto = UserDto(name=member2, age=20)
//        userDto = UserDto(name=member3, age=30)
//        userDto = UserDto(name=member4, age=40)

    }
    // 해법 2 - 서브쿼리 별칭 적용.
    @Test
    public void findUserDto2() {
        QMember memberSub = new QMember("memberSub");

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        List<UserDto> result = queryFactory
                .select(Projections.fields(UserDto.class, // 얘는 기본생성자 필요
                        member.username.as("name"),
                        ExpressionUtils.as(JPAExpressions
                                .select(memberSub.age.max())
                                .from(memberSub), "age")
                ))
                .from(member)
                .fetch();

        for (UserDto userDto : result) {
            System.out.println("userDto = " + userDto);
        }

    }
    //해법3 - 생성자로 해결.
    @Test
    public void findUserDto3() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        List<UserDto> result = queryFactory
                .select(Projections.constructor(UserDto.class, // UserDto(username, age) 생성자 만들어주면됨.
                        member.username,
                        member.age
                ))
                .from(member)
                .fetch();
        for (UserDto userDto : result) {
            System.out.println("userDto = " + userDto);
        }
    }

    // @QueryProjection 를 이용해서 dto를 Qclass만들기
    @Test
    public void findDtoByQueryProjection() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        List<MemberDto> result = queryFactory
                .select(new QMemberDto(member.username, member.age))
//                .distinct() // querydsl에서 지원.
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }


    /**동적 쿼리 문제 해결 방안 2가지 방법 */

    // 1. booleanBuilder 사
    @Test
    public void dynamicQuery_BooleanBuilder() {
        String usernameParam = "member1";
        Integer ageParam = 10;
        List<Member> result = searchMember1(usernameParam, ageParam);
        assertThat(result.size()).isEqualTo(1);
    }
    private List<Member> searchMember1(String usernameCond, Integer ageCond) {
        BooleanBuilder builder = new BooleanBuilder();
        // 각 조건을 builder에 넣어줌.
        if(usernameCond != null) {
            builder.and(member.username.eq(usernameCond));
        }
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        if(ageCond != null) {
            builder.and(member.age.eq(ageCond));
        }


        return queryFactory.
                    selectFrom(member)
                .where(builder)
                .fetch();
    }
    // 2. where 문 다중파라미터 사용(권장)
    @Test
    public void dynamicQuery_WhereParam() {
        String usernameParam = "member1";
        Integer ageParam = 10;
        List<Member> result = searchMember2(usernameParam, ageParam);
        assertThat(result.size()).isEqualTo(1);
    }
    private List<Member> searchMember2(String usernameCond, Integer ageCond) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory
                .selectFrom(member)
                .where(
                        usernameEq(usernameCond), // where절에 null이오면 무시됨.
                        ageEq(ageCond)
//                        allEq(usernameCond, ageCond) // 이거 하나로도 대체도 가능.

                )
                .fetch();
    }

    private BooleanExpression usernameEq(String usernameCond) {
        if(usernameCond == null) return null;
        return member.username.eq(usernameCond);
    }

    private BooleanExpression ageEq(Integer ageCond) {
        if(ageCond==null) return null;
        return member.age.eq(ageCond);
    }
    // 그런데 여기서 조건절을 하나로 만들수가있다. -> 조건을 여러개를 하나를 만들수가있다.
    private BooleanExpression allEq(String usernameCond, Integer ageCond) {
        return usernameEq(usernameCond).and(ageEq(ageCond));
    }

    /*** 수정, 삭제 를 한방에 처리하는 배채쿼리(벌크연산)*/
    // 벌크 연산 - 문제점
    @Test
    @Commit
    public void bulkUpdate1() {
        // bulk연산 이전 (영속성 컨텍스트 상태)
        // member1(나이 10) 의 이름 -> member1
        // member2(나이 20) 의 이름 -> member2
        // member3(나이 30) 의 이름 -> member3
        // member4(나이 40) 의 이름 -> member4

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        long count = queryFactory
                .update(member)
                .set(member.username, "비회원")
                .where(member.age.lt(25)) // 25살이하는 이름 비회원으로 변경
                .execute();

        // bulk연산 이후 (bulk연산시 바로 DB에 적용)
        // member1(나이 10) 의 이름 -> 비회원
        // member2(나이 20) 의 이름 -> 비회원
        // member3(나이 30) 의 이름 -> member3
        // member4(나이 40) 의 이름 -> member4

        // 즉, 영속성 컨텍스트 상태와 DB상태가 서로 달라

        // 이떄 조회같은거하면 안맞아.
        List<Member> result = queryFactory.selectFrom(member).fetch();
//        result.forEach(l -> System.out.println(l));

    }

    // 벌크 연산 - 해결책 (em.flush, em.clear)
    @Test
    @Commit
    public void bulkUpdate2() {
        // bulk연산 이전 (영속성 컨텍스트 상태)
        // member1(나이 10) 의 이름 -> member1
        // member2(나이 20) 의 이름 -> member2
        // member3(나이 30) 의 이름 -> member3
        // member4(나이 40) 의 이름 -> member4

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        long count = queryFactory
                .update(member)
                .set(member.username, "비회원")
                .where(member.age.lt(25)) // 25살이하는 이름 비회원으로 변경
                .execute();

        // bulk연산 이후 (bulk연산시 바로 DB에 적용)
        // member1(나이 10) 의 이름 -> 비회원
        // member2(나이 20) 의 이름 -> 비회원
        // member3(나이 30) 의 이름 -> member3
        // member4(나이 40) 의 이름 -> member4

        // 즉, 영속성 컨텍스트 상태와 DB상태가 서로 달라
        // 그래서 db에 적용.
        em.flush();
        em.clear();

        // 이떄 조회같은거하면 안맞아.
        List<Member> result = queryFactory.selectFrom(member).fetch();
        result.forEach(l -> System.out.println(l));

    }

    // 예제1. 모든나이 * 2 하기
    @Test

    public void bulkAdd() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        // 이렇게하면 바로 DB에 반영
        queryFactory
                .update(member)
                .set(member.age, member.age.multiply(2))
                .execute();
    }
    //예제2. 20 이상 지워
    @Test
    @Commit
    public void bulkDelete() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        // 이렇게하면 바로 DB에 반영
        queryFactory
                .delete(member)
                .where(member.age.gt(20))
                .execute();
    }

    /**SQL function 호출*/
    // member -> M으로 변경
    @Test
    public void sqlFun() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        // member라는 단어를 M으로 바꿔서 조회
        List<String> result = queryFactory
                .select(Expressions.stringTemplate("function('replace', {0}, {1}, {2})",
                        member.username,
                        "member",
                        "M")
                )
                .from(member)
                .fetch();
        result.forEach(l -> System.out.println("l = " + l));
    }

    // 모두 소문자로 바꿔서 조회하고싶어.
    @Test
    public void sqlFun2() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        List<String> result = queryFactory
                .select(member.username)
                .from(member)
//                .where(member.username.eq(
//                        Expressions.stringTemplate("function('lower', {0})", member.username)
//                ))

                .where(member.username.eq(member.username.lower())) //위와 동일.(이미 querydsl이 만들어둔거)
                .fetch();
        result.forEach(l -> System.out.println("l = " + l));

    }




}
