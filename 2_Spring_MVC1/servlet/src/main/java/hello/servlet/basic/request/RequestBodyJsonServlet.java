package hello.servlet.basic.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.servlet.basic.HelloData;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "requestBodyJsonServlet", urlPatterns = "/request-body-json")
public class RequestBodyJsonServlet extends HttpServlet {

    private ObjectMapper objectMapper = new ObjectMapper(); // spring boot에서 갖고있는 json 라이브러리
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // body에 실제 json 형식으로 전송
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);//spring이 제공, 어떤 인코딩 으로 바꿀지도같이 넣어줌
        System.out.println("messageBody = " + messageBody);


        // HelloData로 변환
        /*
            Json결과를 파싱해서 자바객체로 변환하려면 Jackson,Gson 같은 JSON 변환 라이브러리 추가해서사용해야한다.
            스프링 부트로 SpringMVC 선택하면 기본적으로 Jackson 라이브러리 ObjetMapper를 함께 제공해준다.
        */

        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
        System.out.println("helloData.getUsername() = " + helloData.getUsername());
        System.out.println("helloData.getAge() = " + helloData.getAge());

        response.getWriter().write("ok");


    }
}
