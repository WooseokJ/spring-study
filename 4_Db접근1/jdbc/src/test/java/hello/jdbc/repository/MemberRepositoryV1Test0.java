package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;

@Slf4j
class MemberRepositoryV1Test0 {

    MemberRepositoryV0 repository = new MemberRepositoryV0();



    @Test
    void curd() throws SQLException {
        // create
        Member memberV0 = new Member("memberV222", 1000);
        repository.save(memberV0);

        // read
        Member findMember = repository.findById(memberV0.getMemberId());
//        log.info("findMember= {}", findMember);
//        log.info("findMember == memberV0", memberV0 == findMember); // false(원래 다른 인스턴스생성이라 다르다.)

        //eqauls(메서드)는 필드들이 값 같으면 같은객체로 보는것.
        // 그런데 Member에 @Data안에 @EqualsAndHashCode 에의해 equals, hashCode (둘다 메서드 )를 다 만들어준다.
//        log.info("findMember equals memberV0 {}", memberV0.equals(findMember)); // true
        assertThat(findMember).isEqualTo(memberV0); // true.



        // update : money 1000 -> 2000
        repository.update(memberV0.getMemberId(), 2000);
        Member updateMember = repository.findById(memberV0.getMemberId());
        assertThat(updateMember.getMoney()).isEqualTo(2000);


        // delete
        repository.delete(memberV0.getMemberId());
        // repository.findById해서 member조회하면 지웠기떄문에 호출되면안된다. (NoSuchElementException으로 member not found memberId=memberId가 뜬다.)
        // 그래서 NoSuchElementException 에러 뜨면 성공하게 만들자
        assertThatThrownBy( () -> repository.findById(memberV0.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);
    }




}