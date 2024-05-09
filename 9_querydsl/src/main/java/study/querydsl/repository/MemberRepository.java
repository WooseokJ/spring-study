package study.querydsl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.querydsl.Entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> , MemberRepositoryCustom {

    // 스프링데이터 jpa가 간단한 crud, 정적쿼리는 자동으로 만들어줌.
    // select m from Member m where musername = ?
    List<Member> findByUsername(String username);

}
