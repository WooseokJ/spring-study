package hello.core.config;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.member.MemoryMemberRepository;
import hello.core.order.OrderSerivce;
import hello.core.order.OrderSerivceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // 애플리케이션 구성 정보를 갖고있는 설정정보.
public class AppConfig { // 공연기획자

    // 2번 호출되었는데 싱글턴이 꺠지는거 아닌가? 싱글턴 컨테이너는 어떻게 이 문제를 해결할까?
    // -> 해답: 스프링컨테이너가 memberRepository가 한번만 호출되게 만들어져있다.
    // @Bean memberService호출 -> new MemoryMemberRepo()
    // @Bean orderService호출 -> new MemoryMemberRepo()

    @Bean // 이게 붙으면 spring 컨테이너에 등록된다.
    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }
    @Bean
    public OrderSerivce orderSerivce() {
        return new OrderSerivceImpl(memberRepository(), discountPolicy());
    }
    @Bean
    public DiscountPolicy discountPolicy() {
//        return new FixDiscountPolicy();
        return new RateDiscountPolicy();
    }
    @Bean
    public MemoryMemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }


}
