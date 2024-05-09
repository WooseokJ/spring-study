package study.querydsl.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.querydsl.Entity.Member;
import study.querydsl.Entity.QMember;
import study.querydsl.dto.MemberSearchConfition;
import study.querydsl.dto.MemberTeamDto;

import java.util.List;
import java.util.Optional;

import static study.querydsl.Entity.QMember.*;

@Repository
@RequiredArgsConstructor
public class MemberJpaRepository {
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

//    public MemberJpaRepository(EntityManager em) {
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
    // jpql
    public List<Member> findAll() {
        return em.createQuery("select  m from Member  as m ")
                .getResultList();
    }
    public List<Member> findByUsername(String username) {
        return em.createQuery("select m from Member  as m where m.username = :username", Member.class)
                .setParameter("username", username)
                .getResultList();
    }

    // querydsl (위와 동일한거다)
    public List<Member> findAll_querydsl() {
        return queryFactory.selectFrom(member).fetch();
    }
    public List<Member> findByUserName_querydsl(String username) {
        return  queryFactory.selectFrom(member)
                .where(member.username.eq(username))
                .fetch();
    }



}
