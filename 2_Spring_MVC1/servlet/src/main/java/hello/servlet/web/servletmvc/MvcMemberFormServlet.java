package hello.servlet.web.servletmvc;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

// 회원 form 작성
@WebServlet(name = "mvcMemberFormServlet", urlPatterns = "/servlet-mvc/members/new-form")
public class MvcMemberFormServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String viewPath = "/WEB-INF/views/new-form.jsp"; // WEB-INF가 있는이유는 Controller 한번 거쳐서 .jsp파일을 호출하고싶어서 있는것.(WAS서버에서 룰로 정해져있는것) ex) http://localhost:8080/WEB-INF/views/new-form.jsp 브라우저에 이 url로 직접적으로 접근이 불가능해., 내부forward통해서만 호출가능.
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath); // controller -> view로 이동위한 준비.
        dispatcher.forward(request, response); // 이것을하면 Servlet에서 JSP를 호출할수있다., 다른 서블릿이나 JSP로 이동할수있는기능. 서버 내부에서 호출이 다시 발생한다.(redirect는 안됨)
        // redirect , forward 차이
        // redirect: 클라이언트 응답 나갔다가 클라이언트에서 redirect 경로로 다시 서버에 요청한다.)
        // forward: 서버내부에 일어나는 호출이라 클라이언트에서는 응답 1번 받고 인지 못함.



    }
}
