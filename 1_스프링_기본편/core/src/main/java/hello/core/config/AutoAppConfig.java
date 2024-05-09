package hello.core.config;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration // 내부에 @Component가 붙어있다.
@ComponentScan( // 이거를해주면 @Configuration 붙은 설정정보도 자동으로 등록되서 실행되버린다. (그래서 제외시켜줌)
        // Configuration.class가 붙은 애노테이션은 스캔안함.
        // 실무는 그냥하는데 AppConfig 예제코드 적용안되게하려고 적어준거.
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class),
        basePackages = "hello.core"
) // @Component가 붙은 클래스를 찾아서 스캔해서 스프링빈으로 등록한다.
public class AutoAppConfig {



}
