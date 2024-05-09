package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

// 그냥 jpa
@Repository
public class MemberJpaRepository {

    @PersistenceContext
    private EntityManager em;

    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }


    public void delete(Member member) {
        em.remove(member);
    }

    public List<Member> findAll() {
        // JPQL 사용.
        return em.createQuery("select m from Member m", Member.class).getResultList(); // Member 객체대상, Member.class는 반환타입.
    }

    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    public long count() {
        return em.createQuery("select count(m) from Member m", Long.class).getSingleResult();
    }


    // jpa는 entity 변경시 변경감지로 변경한다.(update 쿼리 알아서 한다.)


    // 회원이름과 나이가 몇살보다 많은 회원조회
    public List<Member> findByUsernameAndAgeGreaterThan(String username, int age) {
        return em.createQuery("select m from Member m where m.username = :username and m.age > :age")
                .setParameter("username", username)
                .setParameter("age", age)
                .getResultList();
    }

    public List<Member> findByUsername(String username) {
        return em.createNamedQuery("Member.findByUsername", Member.class)
                .setParameter("username", username)
                .getResultList();
    }
    // offset부터 몇개 가져오기
    public List<Member> findByPage(int age, int offset, int limit) {
        return em.createQuery("select m from Member m where m.age =:age order by m.username desc")
                .setParameter("age", age)
                .setFirstResult(offset) // 어디서부터 가져올거야?
                .setMaxResults(limit) // 개수를 몇개 가져올거야?
                .getResultList();

    }
    // 전체 개수
    public long totalCount(int age) {
        return em.createQuery("select count(m) from Member m where m.age = :age", Long.class)
                .setParameter("age", age)
                .getSingleResult();
    }

    // 회원 나이 한번에 변경
    public int bulkAgePlus(int age) {
        return em.createQuery("update Member m set m.age = m.age + 1 where m.age >= :age")
                .setParameter("age", age)
                .executeUpdate(); // executeUpdate: 응답값의 개수 나옴.
    }


}
