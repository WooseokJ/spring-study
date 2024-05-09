package study.datajpa.repository.custom;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import study.datajpa.entity.Member;
import study.datajpa.repository.custom.MemberRepositoryCustom;

import java.util.List;

// 여기서 커스텀한 메서드 구현해서 사용(주로 querydsl 엮어서 사용.)
@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final EntityManager em;


    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member ").getResultList();
    }
}
