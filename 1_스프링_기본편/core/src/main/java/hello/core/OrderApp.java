package hello.core;

import hello.core.config.AppConfig;
import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemberService;
import hello.core.order.Order;
import hello.core.order.OrderSerivce;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class OrderApp {
    public static void main(String[] args) {

        /*
        기존에 AppConfig직접 생성(new Appconfig)하고DI 했지만
        이제부터는 스프링 컨테이너 통해 사용.

        스프링 컨테이너에 객체를 스프링빈으로 등록하고 스프링 컨테이너에서 스프링빈 찾아서 사용하게 변경.
        */

        // ApplicationContext: spring 컨테이너 (객체들을 다 관리해줌.)  ,
        // AnnotationConfigApplicationContext // 스프링 컨테이너는 AppConfig에있는 설정정보를 통해 @Bean이라 적힌 메서드 모두호출해 객체 생성해 spring 컨테이너에 등록.
        // 스프링 빈 : spring 컨테이너에 등록된 객체

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        // 스프링 빈은 @Bean이 붙은 메서드명을 스프링빈 이름으로 한다. 이름,타입으로 넣으면 객체 꺼내진다.
        MemberService memberService = applicationContext.getBean("memberService", MemberService.class);
        OrderSerivce orderService = applicationContext.getBean("orderService", OrderSerivce.class);

        Member memberA = new Member(1L, "memberA", Grade.VIP);
        memberService.join(memberA);
        Order order = orderService.createOrder(1L, "itemA", 10000);

    }
}
