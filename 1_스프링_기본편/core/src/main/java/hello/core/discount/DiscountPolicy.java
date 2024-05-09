package hello.core.discount;

import hello.core.member.Member;
import org.springframework.stereotype.Component;

@Component
public interface DiscountPolicy {
    // return이  할인대상 금액
    int discount(Member member, int price);


}
