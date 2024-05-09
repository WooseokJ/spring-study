package hello.jdbc.repository;

import com.zaxxer.hikari.HikariDataSource;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static hello.jdbc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
class MemberRepositoryV0Test2 {

    MemberRepositoryV1 repository;

    @BeforeEach // 테스트 전 실행
    void beforEach() {
        // 기본 DriverManager - 항상 새로운 커넥션 획득 (쿼리하나씩 날릴떄마다 커넥션을 맺는다. curd떄 쿼리하나씩 날림.) -> 성능이 느려진다.
//        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
//        repository = new MemberRepositoryV1(dataSource);

        // 커넥션 풀링 (하나의 커넥션을 사용한다) - > 성능이 빨라짐.
        // 여기선 HikariDataSource사용
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        repository = new MemberRepositoryV1(dataSource);

    }

    // crud 테스트 실행시
    // 오른쪽은 DriverManger (커넥션 여러개쓴다.(conn0,1,2,3,4,5)
    // 왼쪽은 커넥션풀링 (하나의 커넥션만사용(conn0)
    // 그런데 커넥션풀링에선 하나의 커넥션은 쓰지만 인스턴스 객체주소는 다르다.
    // 객체주소다른이유: ( 커넥션조회요청오면 반환시 hikariProxy객체를 만들어 실제 커넥션 객체와 래핑해서 반환한다.)
    // 다 종료되면 프록시객체는 종료되고 커넥션은 종료되지않고 그대로 들어온다.
    // 프록시 객체생성은 비용이 크지않다. 커넥션생성이 오래걸리는것.
    // 핵심은 같은커넥션을 재사용할수있다.(성능 빨라짐)


    @Test
    void curd() throws SQLException {
        // create
        Member memberV0 = new Member("memberV222", 1000);
        repository.save(memberV0);

        // read
        Member findMember = repository.findById(memberV0.getMemberId());
        log.info("findMember= {}", findMember);
        log.info("findMember == memberV0", memberV0 == findMember); // false(원래 다른 인스턴스생성이라 다르다.)

        //eqauls(메서드)는 필드들이 값 같으면 같은객체로 보는것.
        // 그런데 Member에 @Data안에 @EqualsAndHashCode 에의해 equals, hashCode (둘다 메서드 )를 다 만들어준다.
        log.info("findMember equals memberV0 {}", memberV0.equals(findMember)); // true
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