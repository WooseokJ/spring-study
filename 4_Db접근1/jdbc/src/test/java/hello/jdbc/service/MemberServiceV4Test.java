package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;


// 예외 누수 문제 해결.
@Slf4j
@SpringBootTest // spring을 띄우고 스프링빈을등록하고 의존관계도 주입받을수있다.
class MemberServiceV4Test {

    public static final String Member_A = "memberA";
    public static final String Member_B = "memberB";
    public static final String Member_EX = "ex";

    @Autowired private MemberRepository memberRepository;
    @Autowired private MemberServiceV4 memberService;

    @TestConfiguration
    static class TestConfig {
        private final DataSource dataSource;

        public TestConfig(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        @Bean
        MemberRepository memberRepositoryV4() {

//            return new MemberRepositoryV4(dataSource); // 스프링 예외 변환 사용전.
//            return new MemberRepositoryV4_2(dataSource); // 스프링 예외 변환 사용.
            return new MemberRepositoryV5(dataSource); // JDBC 템플릿 사용.
        }

        @Bean
        MemberServiceV4 memberServiceV4() {
            return new MemberServiceV4(memberRepositoryV4());
        }
    }

    @AfterEach // 테스트 끝나고 (테스트 메서드 하나씩 끝날떄마다 실행 - 여러테스트 영향안줌.
        // 참고: // 테스트 이전에 트랜잭션 시작하고 테스트이후에 롤백하는게 더 좋은방법.

    void after() throws SQLException {
        memberRepository.delete(Member_A);
        memberRepository.delete(Member_B);
        memberRepository.delete(Member_EX);
    }


    @Test
    @DisplayName("정상이체")
    void accountTransfer() throws SQLException {
        // given
        Member memberA = new Member(Member_A, 10000);
        Member memberB = new Member(Member_B, 10000);

        memberRepository.save(memberA);
        memberRepository.save(memberB);

        //when
        log.info("Start TX");
        memberService.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 2000);
        log.info("End TX");

        //then
        Member findMemberA = memberRepository.findById(memberA.getMemberId());
        Member findMemberB = memberRepository.findById(memberB.getMemberId());

        assertThat(findMemberA.getMoney()).isEqualTo(8000);
        assertThat(findMemberB.getMoney()).isEqualTo(12000);


    }

    @Test
    @DisplayName("이체중 예외 발생")
    void accountTransferEx() throws SQLException {
        // given
        Member memberA = new Member(Member_A, 10000);
        Member memberEx = new Member(Member_EX, 10000);

        memberRepository.save(memberA);
        memberRepository.save(memberEx);

        //when
        Assertions.assertThatThrownBy(() -> memberService.accountTransfer(memberA.getMemberId(), memberEx.getMemberId(), 2000))
                .isInstanceOf(IllegalStateException.class); // 예외 발생해야 테스트 성공.


        //then
        Member findMemberA = memberRepository.findById(memberA.getMemberId());
        Member findMemberB = memberRepository.findById(memberEx.getMemberId());
        assertThat(findMemberA.getMoney()).isEqualTo(8000);
        assertThat(findMemberB.getMoney()).isEqualTo(10000);
    }

    @Test
    void AopCheck() { // 프록시 잘 동작하는지 확인용.(스프링 aop 잘 적용되었는지 확인용)
        // class hello.jdbc.service.MemberServiceV3_3$$SpringCGLIB$$0 에서 $$SpringCGLIB$$0는 프록시 클래스(TransactionProxy)
        log.info("memberSerivce class = {}", memberService.getClass());
        log.info("memberRepository class = {}", memberRepository.getClass());
        Assertions.assertThat(AopUtils.isAopProxy(memberService)).isTrue();
        Assertions.assertThat(AopUtils.isAopProxy(memberRepository)).isFalse();
    }
}