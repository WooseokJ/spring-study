package hello.login.web.filter;

import hello.login.web.Interceptor.LoginInterCeptor;
import hello.login.web.Interceptor.logInterceptor;
import hello.login.web.argumentresolver.LoginMenberArgumentResolver;
import hello.login.web.filter.LogFilter;
import hello.login.web.filter.LoginCheckFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new logInterceptor())
                .order(1)
                .addPathPatterns("/**")  // "/*" 와 동일(서블릿필터의 url 패턴과는  다른거.)
                .excludePathPatterns("/css/**", "/*.ico", "/error"); // 이경로는 스프링 인터셉터 적용 x

        registry.addInterceptor(new LoginInterCeptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/members/add", "/login", "/logout", "/css/**", "/*.ico", "/error");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMenberArgumentResolver());
    }

    //    @Bean
    public FilterRegistrationBean logFilter() { // 스프링부트 사용시 was도 같이 등록되는데 그떄 필터도 등록하기위함.
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter()); // 만든 logFilter 넣어줌.
        filterRegistrationBean.setOrder(1); // 필터는 chain으로 동작하는데 순서필요.(낮을수록 먼저동작)
        filterRegistrationBean.addUrlPatterns("/*"); // 모든 url에 들어간다., 서블릿필터의 url 패턴
        return filterRegistrationBean;
        /* 참고
          @ServletComponentScan, @WebFilter(filterName="logFilter",urlPatters="/*")로 필터등록되지만
          필터순서 설정못해
          -> FilterREgistratinBean 쓰자.
         */

    }

//    @Bean
    public FilterRegistrationBean loginCheckFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LoginCheckFilter());
        filterRegistrationBean.setOrder(2);  // 위에 logFilter1번 필터체크후 2번인 loginCheckFilter사용.
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
        // 이거 메서드 한번더 호출한다고 성능저하없다.
        // 오히려 db쿼리 더 호출, 외부네트워크 더 호출 이런거에서 성능저하 더많이 발생.
    }
}
