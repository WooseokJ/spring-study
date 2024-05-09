package study.querydsl.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import study.querydsl.Entity.Member;
import study.querydsl.dto.MemberSearchConfition;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.dto.QMemberTeamDto;

import java.util.List;

import static study.querydsl.Entity.QMember.*;
import static study.querydsl.Entity.QTeam.*;
import static org.springframework.util.StringUtils.isEmpty;

@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    //회원명, 팀명, 나이(ageGoe, ageLoe)
    @Override
    public List<MemberTeamDto> search(MemberSearchConfition condition) {
        return queryFactory
                .select(new QMemberTeamDto(member.id,
                        member.username,
                        member.age,
                        team.id,
                        team.name))
                .from(member)
                .leftJoin(member.team, team)
                .where(usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe()))
                .fetch();
    }

    private BooleanExpression usernameEq(String username) {
        return isEmpty(username) ? null : member.username.eq(username);
    }
    private BooleanExpression teamNameEq(String teamName) {
        return isEmpty(teamName) ? null : team.name.eq(teamName);
    }
    private BooleanExpression ageGoe(Integer ageGoe) {
        return ageGoe == null ? null : member.age.goe(ageGoe);
    }
    private BooleanExpression ageLoe(Integer ageLoe) {
        return ageLoe == null ? null : member.age.loe(ageLoe);
    }




    @Override
    public Page<MemberTeamDto> searchPageSimple(MemberSearchConfition confition, Pageable pageable) {
        QueryResults<MemberTeamDto> results = queryFactory
                .select(new QMemberTeamDto(member.id,
                        member.username,
                        member.age,
                        team.id,
                        team.name))
                .from(member)
                .leftJoin(member.team, team)
                .where(usernameEq(confition.getUsername()),
                        teamNameEq(confition.getTeamName()),
                        ageGoe(confition.getAgeGoe()),
                        ageLoe(confition.getAgeLoe()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()) // 한페이지에 몇개 가져올꺼야
                .fetchResults();

        List<MemberTeamDto> content = results.getResults();
        long totalCount = results.getTotal();
        return new PageImpl<>(content, pageable, totalCount);
    }

    @Override
    public Page<MemberTeamDto> searchPageComplex(MemberSearchConfition confition, Pageable pageable) {
        List<MemberTeamDto> content = queryFactory
                .select(new QMemberTeamDto(member.id,
                        member.username,
                        member.age,
                        team.id,
                        team.name))
                .from(member)
                .leftJoin(member.team, team)
                .where(usernameEq(confition.getUsername()),
                        teamNameEq(confition.getTeamName()),
                        ageGoe(confition.getAgeGoe()),
                        ageLoe(confition.getAgeLoe()))
                .fetch();


        // 직접 totalCount 쿼리를 날림.
//        long totalCount = queryFactory.selectFrom(member)
//                .leftJoin(member.team, team)
//                .where(
//                        usernameEq(confition.getUsername()),
//                        teamNameEq(confition.getTeamName()),
//                        ageGoe(confition.getAgeGoe()),
//                        ageLoe(confition.getAgeLoe())
//                )
//                .fetchCount();
        // countQuery 선언까지는 쿼리문 안날라감.
        // countQeury.fetchCount() 선언해야 쿼리문날라감
        JPAQuery<Member> countQuery = queryFactory.selectFrom(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(confition.getUsername()),
                        teamNameEq(confition.getTeamName()),
                        ageGoe(confition.getAgeGoe()),
                        ageLoe(confition.getAgeLoe())
                );


        // 성능 최적화
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
//        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetchCount());
//        return new PageImpl<>(content, pageable, totalCount);

    }
}
