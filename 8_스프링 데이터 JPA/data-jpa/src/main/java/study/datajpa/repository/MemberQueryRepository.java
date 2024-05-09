package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;

import java.util.List;
// MemberQueryRepository 라는 복잡한 쿼리만 담는 로직두고 service에서 가져다 사용해도된다.
@Repository
@RequiredArgsConstructor
public class MemberQueryRepository {
    private final EntityManager em;

    List<Member> findAllMember() {
        return em.createQuery("select m from Member m").getResultList();
    }

}
