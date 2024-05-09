package hello.core.single;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.*;

class StatueFulServiceTest {

    @Test
    void StatueFulServiceSingleton() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatueFulService statusService1 = ac.getBean(StatueFulService.class);
        StatueFulService statusService2 = ac.getBean(StatueFulService.class);

        // ThreadA: A사용자가 10000원 주문
//        statusService1.order("userA", 10000);
        // ThreadB: B사용자가 20000원 주문
//        statusService2.order("userB", 20000);

        // ThreadA: 사용자 A가 주문고객 금액 조회
        // 20000 (같은인스턴스라 수정됨.)
//        assertThat(statusService1.getPrice()).isEqualTo(20000);

    }


    @Test
    @DisplayName("무상태성 적용.")
    void StatueFulServiceSingleton2() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatueFulService statusService1 = ac.getBean(StatueFulService.class);
        StatueFulService statusService2 = ac.getBean(StatueFulService.class);

        // 지역변수라 userAPirce, userBPirce는 달라진다.
        int userAPirce = statusService1.order("userA", 10000);
        int userBPirce = statusService2.order("userB", 20000);
        assertThat(userAPirce).isNotEqualTo(20000);

    }


    @Configuration
    static class TestConfig {
        @Bean
        public StatueFulService statueFulService() {
            return new StatueFulService();
        }
    }
}