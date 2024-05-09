package hello.servlet.web.servlet;

import hello.servlet.domain.Member;
import hello.servlet.domain.MemberRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "memberSaveServlet", urlPatterns = "/servlet/members/save")
public class MemberSaveServlet extends HttpServlet {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username"); // 1.get 쿼리스트링, 2.post html form방식이든 꺼낼수있다.
        String ageString = request.getParameter("age"); // getParameter은 String 으로 꺼냄.

        // 문자 -> int 변경
        int age = Integer.parseInt(ageString);

        Member member = new Member(username, age);
        memberRepository.save(member);


        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");
        PrintWriter w = response.getWriter();
        htmlPrint(w, member);


    }

    // 자바로 html 코드 작성.-> 회원저장 성공하면 웹브라우저에 띄움.
    private static void htmlPrint(PrintWriter w, Member member) {
        // 동적인 html 생성
        w.write("<html>\n" +
                "<head>\n" +
                " <meta charset=\"UTF-8\">\n" + "</head>\n" +
                "<body>\n" +
                "성공\n" +
                "<ul>\n" +
                "    <li>id="+ member.getId()+"</li>\n" +
                "    <li>username="+ member.getUsername()+"</li>\n" +
                " <li>age="+ member.getAge()+"</li>\n" + "</ul>\n" +
                "<a href=\"/index.html\">메인</a>\n" + "</body>\n" +
                "</html>");
    }
}
