package study.datajpa.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.custom.MemberRepositoryCustom;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

// 스프링 데이터 jpa
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    // ( 간단한거 사용시 좋아.)
    // 메서드 이름으로 쿼리 생성. Username And Age
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);
    List<Member> findHelloBy();

    // (실무 x, 잘안써)
    // selct m from Member m where m.username =:username 시 @Param 필요.
//    @Query(name = "Member.findByUsername")
    // 주석해도되는건 관례가있다. (Entity명.메서드명) 으로 네임드쿼리 애노테이션부터 찾는다.
    List<Member> findByUsername(@Param("username") String username);


    // 1. @Query로 쿼리 직접 정의(실무) - 2의 파라미터 바인딩과 동일.
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);



    // 2. @Query사용해 값(string), dto 조회(실무, 위에보다 많이씀.)

    // String으로 원하는것만 (실무)
    @Query("select m.username from Member m")
    List<String> findUsernameList();

    // DTO 조회
    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    // 파라미터 바인딩
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);




    // 스프링 데이터 jpa는 유연한 반환타입 지원
    List<Member> findListByUsername(String username); // 컬렉션
    Member findMemberByUsername(String username); // 단건
    Optional<Member> findOptionalByUsername(String username); // 단건(+옵셔널로 반환)



    // 페이징
//    Page<Member> findByAge(int age, Pageable pageablne);
//    Slice<Member> findByAge(int age, Pageable pageable);

    // totalcount가 많아질떄 사용.
    @Query(value = "select m from Member m left join  m.team t",
        countQuery = "select count(m.username) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);


    // 벌크업(모든직원 연봉 10% 인상이면 하나하나적용이아닌 한방쿼리로 적용)
    @Modifying(clearAutomatically = true) // excuteUpdate같이 동작.(안넣으면 오류)
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    // fetch join 필요한이유
    // fetch join하면 연관된거 까지 다가져옴.
    @Query("select m from Member m left join fetch m.team") // member조회시 team도 같이 조회
    List<Member> findMemberFetchJoin();

    // 쿼리짜기싫을떄 사용.
    @Override
    @EntityGraph(attributePaths = "team") // member조회시 team도 같이 조회
    List<Member> findAll();

    // Entitygraph와 쿼리동시
    @EntityGraph(attributePaths = "team")
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    // 직접 만들기
//    @EntityGraph(attributePaths = "team")
//    List<Member> findEntityGraphByUsername(@Param("username") String username); // find ~ by 사이는 아무거나


    @EntityGraph("Member.all")
    List<Member> findEntityGraphByUsername(@Param("username") String username); // find ~ by 사이는 아무거나


    // @queyhint 와 @Lock
    @QueryHints(
            value = @QueryHint(name = "org.hibernate.readOnly", value = "true")
    )
    Member findReadOnlyByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);






}
