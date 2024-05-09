package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


// 그냥 jpa로 테스트
@SpringBootTest
@Transactional
@Rollback(value = false) // commit
class MemberJpaRepositoryTest {

    @Autowired MemberJpaRepository memberRepository;

    @Test
    void testMember() {
        Member member = new Member("memberA");
        Member saveMember = memberRepository.save(member);
        Member findMember = memberRepository.find(saveMember.getId());
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

    @Test // 쿼리이름으로 쿼리생성
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

    @Test
    public void paging() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        // page 1( offset = 0, limit = 10)
        // page 2( offset = 11, limit = 10)...
        int age = 10;
        int offset = 0;
        int limit = 3;

        //when
        List<Member> members = memberRepository.findByPage(age, offset, limit);
        long totalCount = memberRepository.totalCount(age);

        // then
        assertThat(members.size()).isEqualTo(3);
        assertThat(totalCount).isEqualTo(5);
    }

    @Test
    public void bulkUpdate() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 29));
        memberRepository.save(new Member("member4", 39));
        memberRepository.save(new Member("member5", 49));

        // when
        int resultCount = memberRepository.bulkAgePlus(20); // 20살이상은 +1 (3개)
        assertThat(resultCount).isEqualTo(3);

    }


}