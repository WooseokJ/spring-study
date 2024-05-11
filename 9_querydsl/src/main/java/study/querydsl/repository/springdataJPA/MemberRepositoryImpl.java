package study.querydsl.repository.springdataJPA;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;
import study.querydsl.dto.MemberCond;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.dto.QMemberTeamDto;
import study.querydsl.entity.Member;

import java.util.List;

import static study.querydsl.entity.QMember.member;
import static study.querydsl.entity.QTeam.team;

// 주의: Entity명 + RepositoryImpl 로 하자. (안그럼 오류남)
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MemberTeamDto> search(MemberCond cond) {
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

    @Override
    public Page<MemberTeamDto> searchSimple(MemberCond cond, Pageable pageable) {
        QueryResults<MemberTeamDto> results = queryFactory
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
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        List<MemberTeamDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);

    }

    @Override
    public Page<MemberTeamDto> searchComplex(MemberCond cond, Pageable pageable) {
        // 쿼리 자체를 분리.
        List<MemberTeamDto> content = queryFactory
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
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        //직접 totalcount를 구함.(count 쿼리 최적화전)
//        long totalcount = queryFactory
//                .select(member)
//                .from(member)
//                .leftJoin(member.team, team)
//                .where(
//                        usernameEq(cond.getUsername()),
//                        ageLoeEq(cond.getAgeLoe()),
//                        teamNameEq(cond.getTeamName()),
//                        ageGoeEq(cond.getAgeGoe())
//                )
//                .fetchCount();
//
//        return new PageImpl<>(content, pageable, totalcount);



        // 최적화 후
        // countQuery 선언까지는 쿼리문 안날라감.
        // countQeury.fetchCount() 선언해야 쿼리문 날라가 DB에 반영
        JPAQuery<Member> countQuery = queryFactory
                .select(member)
                .from(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(cond.getUsername()),
                        ageLoeEq(cond.getAgeLoe()),
                        teamNameEq(cond.getTeamName()),
                        ageGoeEq(cond.getAgeGoe())
                );
        // 다음 2가지 경우에는 totalcount쿼리 날릴필요가없음.(1,2만족 안하면 countQuery.fetchCount() 메서드 호출안함.)
        // 1.페이지 시작이면서 contentsize < pageSize일떄   (전체가 3개인데 pageSize는 10개면 그냥 전체를 pageSize로 사용하면됨.)
        // 2. 마지막 페이지일떄 (offset + contentsize = totalCount)
//        return PageableExecutionUtils.getPage(content, pageable, () ->  countQuery.fetchCount());
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount); // 위와 동일)
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
