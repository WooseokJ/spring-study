package hello.exception.servlet;


import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Slf4j
@Controller
public class ServletExController { // ( 오류 발생시키기 위한 controller)
    @GetMapping("/error-ex")
    public void errorEx() {
        throw  new RuntimeException("예외발생");
    }

    @GetMapping("/error-404")
    public void error404(HttpServletResponse response) throws IOException {
        response.sendError(404,"404오류 메세지"); //응답값으로 404, 404오류메세지 반환.
    }

    @GetMapping("/error-400")
    public void error400(HttpServletResponse response) throws IOException {
        response.sendError(400,"400오류 메세지");
    }

    @GetMapping("/error-500")
    public void error500(HttpServletResponse response) throws IOException{
        response.sendError(500);
    }
}
