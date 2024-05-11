package study.querydsl.repository.support;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;
import study.querydsl.dto.MemberCond;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;

import java.util.List;

import static study.querydsl.entity.QMember.*;
import static study.querydsl.entity.QTeam.team;

public class MemberTestRepository extends Querydsl4RepositorySupport{
    public MemberTestRepository() {
        super(Member.class);
    }

    public List<Member> basicSelect() {
        return select(member)
                .from(member)
                .fetch();
    }
    public List<Member> basicSelectFrom() {
        return selectFrom(member)
                .fetch();
    }
    // searchSimple
    public Page<Member> searchSimple(MemberCond cond, Pageable pageable) {
        JPAQuery<Member> query = selectFrom(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(cond.getUsername()),
                        teamNameEq(cond.getTeamName()),
                        ageGoeEq(cond.getAgeGoe()),
                        ageLoeEq(cond.getAgeLoe())
                );
        List<Member> content = getQuerydsl().applyPagination(pageable, query).fetch();
        return PageableExecutionUtils.getPage(content, pageable, query::fetchCount);

        // 람다를 이용해 더 간결하게 표현가능
//        return applyPagination(pageable, query ->
//                query.selectFrom(member)
//                        .leftJoin(member.team, team)
//                        .where(
//                                usernameEq(cond.getUsername()),
//                                teamNameEq(cond.getTeamName()),
//                                ageGoeEq(cond.getAgeGoe()),
//                                ageLoeEq(cond.getAgeLoe())
//                        ));
    }
    // searchComplex
    public Page<Member> searchComplex(MemberCond cond, Pageable pageable) {
        return applyPagination(pageable,
                contentQuery -> contentQuery
                        .selectFrom(member)
                        .leftJoin(member.team, team)
                        .where(
                                usernameEq(cond.getUsername()),
                                teamNameEq(cond.getTeamName()),
                                ageGoeEq(cond.getAgeGoe()),
                                ageLoeEq(cond.getAgeLoe())
                        ),
                countQuery -> countQuery
                        .select(member.id)
                        .from(member)
                        .leftJoin(member.team, team)
                        .where(
                                usernameEq(cond.getUsername()),
                                teamNameEq(cond.getTeamName()),
                                ageGoeEq(cond.getAgeGoe()),
                                ageLoeEq(cond.getAgeLoe())
                        )
                );
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

}
