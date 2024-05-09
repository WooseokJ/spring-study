package hello.servlet.basic.request;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


// 1. get방식의 쿼리 파라미터
// localhost:8080/request-param?username=hello&age=20 로 요청

// 2. post 방식의 html form 방식
// localhost:8080/basic/hello-form.html 에서 form에 값넣고 전송버튼눌러서 요청.

// 3. RequestBodyStringServlet 클래스 참고

@WebServlet(name = "requestParamServlet", urlPatterns = "/request-param")
public class RequestParamServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        printAllParam(request); // 전체 파라미터 조회
        printOneParam(request); // 단일 파라미터 조회
        printDuplicate(request); // 이름이 같은 복수 파라미터 조회
        response.getWriter().write("ok"); // 브라우저에 ok 텍스트 띄움.


        // request.getParameter 는 파라미터1개에대해 값이 1개인경우 쓰기 (만약 값이 여러개이면 첫번쨰꺼가 출력)
        // reqeust.getParameterValues는 파라미터 1개에대해 값이 여러개인경우 쓰기
    }

    //이름이 같은 복수 파라미터(파라미터 1개인데 값이 여러개일 경우)
    private static void printDuplicate(HttpServletRequest request) {
        System.out.println("[이름이 같은 복수 파라미터]"); // username=hello&username=hello2 같은거
        String[] usernames = request.getParameterValues("username"); // 파라미터 값이 여러개일떄는 values로 꺼내기
        for( String name: usernames) {
            System.out.println("name = " + name);
        }
        System.out.println();

    }

    // 단일 파라미터 조회
    private static void printOneParam(HttpServletRequest request) {
        System.out.println("[단일 파라미터 조회]");
        String username = request.getParameter("username");
        String age = request.getParameter("age");
        System.out.println("username = " + username);
        System.out.println("age = " + age);
        System.out.println();
    }

    // 전체 파라미터 조회
    private static void printAllParam(HttpServletRequest request) {
        System.out.println("[전체 파라미터 조회]");

        request.getParameterNames().asIterator()
                        .forEachRemaining(paramName -> System.out.println(paramName + " = " + request.getParameter(paramName)));

        System.out.println();
    }
}
