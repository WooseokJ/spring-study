package hello.servlet.basic;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

import java.io.IOException;

@WebServlet(name = "helloServlet", urlPatterns = "/hello") //name은 아무거나 넣어도됨(서블릿이름), url은 주소값(url매핑), 주의: name, url 겹치면안된다.
public class HelloServlet extends HttpServlet {

    @Override
    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        //
        // http요청을 통해 매핑된 url이 호출되면(localhost:8080/hello) 서블릿 컨테이너가 service메서드 실행하고 request, response 객체 만들어서 servlet에 던져준다.
        // ServletRequest, ServletResponse 는 인터페이스로 WAS서버가 구현해준다.
        // tomcat, jetty, undertow 다 WAS 서버의 종류이다.


        // request
        System.out.println("request = " + request);
        String username = request.getParameter("username"); // /hello"?username=kim
        System.out.println("username = " + username);

        //response
        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write("안녕 " + username); // 웹페이지에 안녕 + username 이 뜬다.




    }

}
