package hello.core.beanfind;

import hello.core.config.AppConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class InfoTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @DisplayName("모든 빈 출력")
    void findAllBean() {
        String[] beanDefinitionNames = ac.getBeanDefinitionNames(); // 스프링에 등록된 모든 빈이름 조회
        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = ac.getBean(beanDefinitionName); // 빈 이름으로 빈객체(인스턴스) 조회.
            System.out.println(" name = " + beanDefinitionName + " object = " + bean);
        }
    }


    @Test
    @DisplayName("애플리케이션 등록된 빈 출력")
    void findApplicationBean() {
        String[] beanDefinitionNames = ac.getBeanDefinitionNames(); // 스프링에 등록된 모든 빈이름 조회
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName); // 빈에대한 메타 데이터 정보.

            // ROLE_APPLICATION: 사용자가 정의한 애플리케이션 빈
            // ROLE_InFRASTRUCTURE: 스프링이 내부에서 사용하는 빈.

            if (beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) {
                Object bean = ac.getBean(beanDefinitionName); // 빈 이름으로 빈객체(인스턴스) 조회.
                System.out.println(" name = " + beanDefinitionName + " object = " + bean);
            }
        }
    }
}
