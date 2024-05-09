package hello.core.order;

import hello.core.config.AppConfig;
import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class OrderServiceTest {
    AppConfig appConfig = new AppConfig();

    MemberService memberService;
    OrderSerivce orderSerivce;
    @BeforeEach
    void beforeEach() {
        memberService = appConfig.memberService();
        orderSerivce = appConfig.orderSerivce();
    }


    @Test
    void createOrder() {
        Long memberId = 1L;
        Member memberA = new Member(memberId, "memberA", Grade.VIP);
        memberService.join(memberA);

        Order order = orderSerivce.createOrder(memberId, "itemA", 10000);
        assertThat(order.getDiscountPrice()).isEqualTo(1000); // 할인된 가격

    }
}
