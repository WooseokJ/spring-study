package hello.servlet.basic.response;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "responseHeaderServlet", urlPatterns = "/response-header")
public class ResponseHeaderServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 해더나 상태코드에대한 내용

        // status 설정
        response.setStatus(HttpServletResponse.SC_OK); // HttpServletResponse 인터페이스에 이미 정의되어있다. (직접 200 적는것보다 이게더 좋다.)

        // header 설정
//        responseHeaders(response); // 수동으로 하나하나 Header 세팅


        // header의 편의메서드 (responseHeaders(response) 보다 더 편리하다. )
        content(response); // Content 편의 메서드
        cookie(response); // cookie 편의 메서드
        redirect(response); // redirect 편의 메서드

        PrintWriter writer = response.getWriter();
        writer.write("ok");

    }

    // 수동으로 하나하나 Header 세팅
    private static void responseHeaders(HttpServletResponse response) {
        response.setHeader("Content-Type", "text/plain;charset=utf-8");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Prama", "no-cache");
        response.setHeader("my-header", "hello");
    }

    // Content 편의 메서드
    private static void content(HttpServletResponse response) {
        //Content-Type: text/plain;charset=utf-8
        //Content-Length: 2
        //response.setHeader("Content-Type", "text/plain;charset=utf-8");

        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");
        response.setContentLength(2); //(생략시 자동 생성)
    }

    // cookie 편의 메서드
    private static void cookie(HttpServletResponse response) {
        //Set-Cookie: myCookie=good; Max-Age=600;
        // response.setHeader("Set-Cookie", "myCookie=good; Max-Age=600");

        Cookie cookie = new Cookie("myCookie", "good");
        cookie.setMaxAge(600); //600초동안 유효하다.
        response.addCookie(cookie); // response에 cookie를 넣어준다.
    }

    // redirect 편의 메서드
    private static void redirect(HttpServletResponse response) throws IOException {
        //Status Code를  302로 만들거고
        //Location header를  /basic/hello-form.html로 보낼거다.

        // 방법 1.
//            response.setStatus(HttpServletResponse.SC_FOUND); //302
//            response.setHeader("Location", "/basic/hello-form.html");

        // 방법 2.(이게더 편리) , statusCode 알아서 설정.
        response.sendRedirect("/basic/hello-form.html");

    }


}
