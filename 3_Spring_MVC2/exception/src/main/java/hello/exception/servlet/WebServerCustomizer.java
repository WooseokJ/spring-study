package hello.exception.servlet;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

//@Component // 스프링 지원하는 오류페이지쓸꺼면 주석처리해줘야함.
public class WebServerCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

    // 기본오류페이지를 커스텀해서 보여주기위함.
    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/error-page/400"); // 400 에러나면 여기 페이지로 가라.
        ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error-page/500");
        ErrorPage errorPageRuntime = new ErrorPage(RuntimeException.class, "/error-page/500"); // runtimeExcpetion 및 자식예외들도 발생시 여기 페이지로 가라.

        // 서블릿 컨테이너에 등록
        factory.addErrorPages(errorPage404, errorPage500, errorPageRuntime);

    }
}
