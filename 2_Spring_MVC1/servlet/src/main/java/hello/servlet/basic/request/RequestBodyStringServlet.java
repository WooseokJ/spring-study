package hello.servlet.basic.request;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

//
@WebServlet(name = "requestBodyStringServlet", urlPatterns = "/request-body-string")
public class RequestBodyStringServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // body에 단순텍스트로 전송
        ServletInputStream inputStream = request.getInputStream();// request body부분을 byte코드로 얻음.
        // byte -> String 변환
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);//spring이 제공, 어떤 인코딩 으로 바꿀지도같이 넣어줌
        System.out.println("messageBody = " + messageBody);
        response.getWriter().write("ok");

        // body에 실제 json 형식으로 전송 (RequestBodyJson 참고)



    }
}
