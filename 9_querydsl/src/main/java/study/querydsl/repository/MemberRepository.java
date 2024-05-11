package study.querydsl.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import study.querydsl.dto.MemberCond;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.dto.QMemberTeamDto;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.QTeam;

import java.util.List;
import java.util.Optional;

import static study.querydsl.entity.QMember.*;
import static study.querydsl.entity.QTeam.*;

// 순수 jpa + querydsl 로 repository 만들기
@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private final EntityManager em; // 순수 jpa
    private final JPAQueryFactory queryFactory; // querydsl

//    public MemberRepository(EntityManager em) { // 만약 이거 하기싫으면 bean등록(위에 참고)
//        this.em = em;
//        this.queryFactory = new JPAQueryFactory(em);
//    }

    public void save(Member member) {
        em.persist(member);
    }
    public Optional<Member> findById(Long id) {
        Member findMember = em.find(Member.class, id);
        return Optional.ofNullable(findMember);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findbyUsername(String username) {
        return em.createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", username)
                .getResultList();
    }

    // querydsl
    public List<Member> findAll_Querydsl() {
        return queryFactory
                .selectFrom(member)
                .fetch();
    }
    public List<Member> findbyUsername_Querydsl(String username) {
        return queryFactory
                .selectFrom(member)
                .where(member.username.eq(username))
                .fetch();
    }


    // 동적쿼리 - BooleanBuilder 사용
    public List<MemberTeamDto> searchByBuilder(MemberCond cond) {
        BooleanBuilder builder = new BooleanBuilder();

        // == null로 안하는건 null말고 ""도 자주 넘어오기떄문에 조건이 제대로 안걸릴수있어서.
        // -> 그래서 StringUtils를 사용.
        if (StringUtils.hasText(cond.getUsername())) {
            builder.and(member.username.eq(cond.getUsername()));
        }

        if (StringUtils.hasText(cond.getTeamName())) {
            builder.and(team.name.eq(cond.getTeamName()));
        }

        if(cond.getAgeGoe() != null) {
            builder.and(member.age.goe(cond.getAgeGoe()));
        }
        if(cond.getAgeLoe() != null) {
            builder.and(member.age.loe(cond.getAgeLoe()));
        }


        return queryFactory
                .select(new QMemberTeamDto(
                        member.id.as("memberId"),
                        member.username,
                        member.age,
                        team.id.as("teamId"),
                        team.name.as("teamName")
                ))
                .from(member)
                .leftJoin(member.team, team)
                .where(builder)
                .fetch();
    }

    // 동적쿼리 - where절 파라미터 사용
    public List<MemberTeamDto> searchByWhere(MemberCond cond) {
        System.out.println("cond = " + cond.getUsername());
        return queryFactory
                .select(new QMemberTeamDto(
                        member.id.as("memberId"),
                        member.username,
                        member.age,
                        team.id.as("teamId"),
                        team.name.as("teamName")
                ))
                .from(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(cond.getUsername()),
                        ageLoeEq(cond.getAgeLoe()),
                        teamNameEq(cond.getTeamName()),
                        ageGoeEq(cond.getAgeGoe())
//                        all(cond.getUsername(),
//                            cond.getTeamName(),
//                            cond.getAgeLoe(),
//                            cond.getAgeGoe())
                )
                .fetch();
    }
    private BooleanExpression usernameEq(String username) {
        return StringUtils.hasText(username) ? member.username.eq(username) : null;
    }
    private BooleanExpression teamNameEq(String teamName) {
        return StringUtils.hasText(teamName) ? team.name.eq(teamName) : null;
    }
    private BooleanExpression ageLoeEq(Integer ageLoe) {
        return ageLoe != null ? member.age.loe(ageLoe): null;
    }
    private BooleanExpression ageGoeEq(Integer ageGoe) {
        return ageGoe != null ? member.age.goe(ageGoe) : null;
    }

    private BooleanExpression all(String username, String teamName,
                                  Integer ageLoe, Integer ageGoe) {
        return usernameEq(username)
                .and(teamNameEq(teamName))
                .and(ageGoeEq(ageGoe))
                .and(ageLoeEq(ageLoe));
    }



}
