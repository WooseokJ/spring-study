package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;
import study.datajpa.repository.team.TeamRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

// 스프링 데이터 jpa로 테스트
@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {
    @Autowired MemberRepository memberRepository;

    @BeforeEach
    public void 이전() {
        memberRepository.deleteAll();
    }

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member saveMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(saveMember.getId()).get();
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    void basicCRUD() {
        Member member1 = new Member("member11");
        Member member2 = new Member("member22");
        memberRepository.save(member1);
        memberRepository.save(member2);

        // 단건 조회
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        // 리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // 카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        long recentCount = memberRepository.count();
        assertThat(recentCount).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThen() {
        Member m1 = new Member("AA", 10);
        Member m2 = new Member("AA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AA", 15);//
        assertThat(result.get(0).getUsername()).isEqualTo("AA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void findHelloBy() {
        List<Member> helloBy = memberRepository.findHelloBy();
    }

    @Test // namedQuery 테스트
    public void testNamedQuery() {
        Member m1 = new Member("AA", 10);
        Member m2 = new Member("AA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);
        List<Member> result = memberRepository.findByUsername("AA");
        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(m1);
    }

    @Test // @query 쿼리 직접 정의
    public void testQuery() {
        Member m1 = new Member("AA", 10);
        Member m2 = new Member("AA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);
        List<Member> result = memberRepository.findUser("AA",10);
        assertThat(result.get(0)).isEqualTo(m1);

    }

    @Test
    public void findUsernameList() {
        Member m1 = new Member("AA", 10);
        Member m2 = new Member("AA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);
        List<String> result = memberRepository.findUsernameList();
        for (String s : result) {
            System.out.println("s = " + s);
        }
    }

    @Autowired
    TeamRepository teamRepository;
    @Test
    public void findMemberDto() {
        Team teamA = new Team("teamA");
        teamRepository.save(teamA);

        Member m1 = new Member("AA", 10);
        memberRepository.save(m1);


        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto);
        }
    }
    @Test
    public void findByNames() {
        Member m1 = new Member("AA", 10);
        Member m2 = new Member("BB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);
        List<String> list = Arrays.asList("AA", "BB");
        List<Member> result = memberRepository.findByNames(list);
        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }

    @Test
    public void returnType() {
        Member m1 = new Member("AA", 10);
        Member m2 = new Member("BB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);
        List<Member> listByUsername = memberRepository.findListByUsername("AA"); // 만약 AA없어도 빈 컬렉션 타입반환받음(실무에서 null로 조건문하지마)
        Member member = memberRepository.findMemberByUsername("AA");
        Optional<Member> optionalMember = memberRepository.findOptionalByUsername("AA");
    }

    //Page<Member> findByAge(int age, Pageable pageable);
    @Test
    public void pagingPage() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2",10));
        memberRepository.save(new Member("member3",10));
        memberRepository.save(new Member("member4",10));
        memberRepository.save(new Member("member5",10));

        int age = 10;
        // page 0 ( offset = 0, limit = 3)
        // page 1 ( offset = 4, limit = 3)
        // page 2 ( offset = 7, limit = 3)...
        // 스프링 데이터 jpa는 페이지를 0 부터 시작.(1부터 아님), 0 페이지에서 3개 가져와.(usernamer 기준으로 내림차순)
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
        // when
        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        // 실제로 Member대신 DTO로 변환해서 써야한다.
        Page<MemberDto> dtoPage = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));

        // then
        List<MemberDto> content = dtoPage.getContent(); // 0 번쨰 페이지 요소 가져옴.(3개)
        assertThat(content.size()).isEqualTo(3); // 페이징된 요소의 개수

        assertThat(page.getTotalElements()).isEqualTo(5); // 전체 요소 개수(totalCount)
        assertThat(page.getNumber()).isEqualTo(0); // 현재 페이지 번호
        assertThat(page.getTotalPages()).isEqualTo(2); // 총 페이지 번호
        assertThat(page.isFirst()).isTrue(); // 현재 페이지가 첫번쨰 페이지인지?
        assertThat(page.hasNext()).isTrue(); // 다음페이지있냐?
    }

    @Test
    public void pageingSlice() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2",10));
        memberRepository.save(new Member("member3",10));
        memberRepository.save(new Member("member4",10));
        memberRepository.save(new Member("member5",10));
        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
        // when
        // Slice는 totalCount 안준다.(total관련이없다), slice는 요청시 3개 요청이아닌 4개를 요청해봄.
        // 참고: 만약 3개만 가져오기만하려면 List로 해줘도됨.
        Slice<Member> page = memberRepository.findByAge(age, pageRequest); // +1해서 더보기같은 기능시 편리. , List는 페이징없이 가져올떄 쓴다.
        Slice<MemberDto> dtoPage = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));

        // 실제로 Member대신 DTO로 변환해서 써야한다.
        List<MemberDto> content1 = dtoPage.getContent(); // 0 번쨰 페이지 요소 가져옴.(3개)
        assertThat(content1.size()).isEqualTo(3); // 페이징된 요소의 개수
        assertThat(page.getNumber()).isEqualTo(0); // 페이지 번호
        assertThat(page.isFirst()).isTrue(); // 첫번쨰 페이지
        assertThat(page.hasNext()).isTrue(); // 다음페이지있냐?
    }

    @PersistenceContext private EntityManager em;

    @Test
    public void bulkUpdate() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 29));
        memberRepository.save(new Member("member4", 39));
        memberRepository.save(new Member("member5", 49));

        // when
        int resultCount = memberRepository.bulkAgePlus(20); // 20살이상은 +1 (3개)
//        em.clear();

        // 주의점 ( 영속성컨텍스트는 모르고 그냥 DB에서 가져옴.), 그래서 clear를 한다.,
        List<Member> result = memberRepository.findByUsername("member5");
        Member member5 = result.get(0);
        System.out.println("member5 = " + member5); // 여기서는 아직 49이다.( clear없을떄 )


        // then
        assertThat(resultCount).isEqualTo(3);

    }

    @Test
    public void findMemberLazy() {
        // given
        // member1 -> teamA를 참조
        // member2 -> teamB를 참조
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamB);
        teamRepository.save(teamA);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        // (fetch join 사용전 ) - findall도 override 되기전
        // member만 다 가져옴.(team은 프록시 객체만 가져옴), N+1문제: 쿼리 1번(select) 날렷는데 결과가 n번(team이름 가져오는 쿼리)
        List<Member> members1 = memberRepository.findAll();
        for (Member member : members1) {
            System.out.println("member1.getUsername = " + member.getUsername());
            System.out.println("member1.class = " + member.getTeam().getClass()); // 프록시 객체 가져옴
            System.out.println("member1.team = " + member.getTeam().getName()); // getTeam으로 해서 데이터 실제 가져오려할떄 sql문 날려서 데이터(팀이름) 가져옴.
        }

        // (fetch join 사용후 ) N+1문제 해결.
        List<Member> members2 = memberRepository.findMemberFetchJoin();
        for (Member member : members2) {
            System.out.println("member2.getUsername = " + member.getUsername());
            System.out.println("member2.class = " + member.getTeam().getClass()); // 프록시 객체 가져옴
            System.out.println("member2.team = " + member.getTeam().getName());
        }

        // (EntityGraph 사용후 한번에 가져옴.) ,findAll은 override함
        List<Member> members3 = memberRepository.findAll();
        for (Member member : members3) {
            System.out.println("member3.getUsername = " + member.getUsername());
            System.out.println("member3.class = " + member.getTeam().getClass()); // 진짜 객체 가져옴
            System.out.println("member3.team = " + member.getTeam().getName());
        }


        List<Member> members4 = memberRepository.findEntityGraphByUsername("member1");
        for (Member member : members4) {
            System.out.println("member4.getUsername = " + member.getUsername());
            System.out.println("member4.class = " + member.getTeam().getClass()); // 진짜 객체 가져옴
            System.out.println("member4.team = " + member.getTeam().getName());
        }

    }

    @Test
    public void queryHint() {
        Member member = new Member("member1", 10);
        memberRepository.save(member);
        em.flush(); // 영속성 컨텍스트 , DB 동기화
        em.clear(); // 영속성 캐시 지움

        // 단순 조회만하는데 snapshot을 만든다.
//        Member findMember = memberRepository.findById(member.getId()).get();
        // 단순 조회만하는데 snapshot을 안만든다.
        Member findMember = memberRepository.findReadOnlyByUsername("member1");

        findMember.setUsername("member2");
        em.flush();// update쿼리 나감
    }

    @Test
    public void lock() {
        Member member = new Member("member1", 10);
        memberRepository.save(member);
        em.flush(); // 영속성 컨텍스트 , DB 동기화
        em.clear(); // 영속성 캐시 지움


        List<Member> result = memberRepository.findLockByUsername("member1");

    }

    @Test
    public void callCustom() {
        List<Member> result = memberRepository.findMemberCustom();
    }
}