package hello.core.member;

import hello.core.config.AppConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class MemberSerivceTest {
    AppConfig appConfig = new AppConfig();

    MemberService memberService;
    @BeforeEach
    void beforeEach() {
         memberService = appConfig.memberService();
    }
    @Test
    void Join() {

        // given
        Member memberA = new Member(1L, "memberA", Grade.VIP);

        // when
        memberService.join(memberA);
        Member findmember = memberService.findMember(1L);

        // then
        assertThat(findmember).isEqualTo(memberA);
    }
}