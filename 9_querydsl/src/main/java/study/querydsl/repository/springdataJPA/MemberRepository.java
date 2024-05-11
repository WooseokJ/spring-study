package study.querydsl.repository.springdataJPA;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import study.querydsl.entity.Member;

import java.util.List;

//public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
//    // select m from Member m where musername = ?
//    List<Member> findByUsername(String username);
//}

// QuerydslPredicateExecutor 사용
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom , QuerydslPredicateExecutor {
    // select m from Member m where musername = ?
    List<Member> findByUsername(String username);
}


