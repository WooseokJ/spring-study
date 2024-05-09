package hello.servlet.basic.response;




import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;


// 서블릿으로 html를 응답 생성
// localhost:8080/reseponse-html url 요청
@WebServlet(name = "responseHtmlServlet", urlPatterns = "/reseponse-html")
public class ResponseHtmlServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // content-Type을 text/html;charset=utf-8로

        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");

        PrintWriter writer = response.getWriter();
        htmlSetting(writer);

    }

    private static void htmlSetting(PrintWriter writer) {
        // 동적으로 html 파일 만들수있다.
        writer.println("<html>");
        writer.println("<body>");
        writer.println("<div> 안녕 ? </div>");
        writer.println("</html>");
        writer.println("</body>");
    }
}
