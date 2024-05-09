package hello.core.scan.filter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

public class CompoentFilterAppConfigTest {
    @Test
    void filterScan() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(CompoentFilterAppConfig.class);
        BeanA_Include beanAInClude = ac.getBean("beanA_Include", BeanA_Include.class);
        assertThat(beanAInClude).isNotNull();

        // 여기선 스캔대상 아니라 가져올떄 없으니까 예외 나와야해
//        assertThrows(
//                NoSuchBeanDefinitionException.class,
//                () -> ac.getBean("beanB_Exclude", BeanB_Exclude.class);
//        )
    }

    @Configuration
    @ComponentScan(
            includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = MyIncludeCompoent.class),
            excludeFilters = {
                    @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = MyExcludeCompoent.class),
                    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = BeanA_Include.class)
            }
    )
    static class CompoentFilterAppConfig {

    }
}
