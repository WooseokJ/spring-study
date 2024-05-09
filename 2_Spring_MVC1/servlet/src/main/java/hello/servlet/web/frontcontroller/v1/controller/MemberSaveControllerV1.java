package hello.servlet.web.frontcontroller.v1.controller;

import hello.servlet.domain.Member;
import hello.servlet.domain.MemberRepository;
import hello.servlet.web.frontcontroller.v1.ControllerV1;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class MemberSaveControllerV1 implements ControllerV1 {

    private MemberRepository memberRepository = MemberRepository.getInstance();
    @Override
    public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username"); // 1.get 쿼리스트링, 2.post html form방식이든 꺼낼수있다.
        String ageString = request.getParameter("age"); // getParameter은 String 으로 꺼냄.

        // 문자 -> int 변경
        int age = Integer.parseInt(ageString);

        Member member = new Member(username, age);
        memberRepository.save(member);

        // Model에 데이터 보관한다.
        request.setAttribute("member", member); // request객체 내부에 임시저장소에 저장
        String viewpath = "/WEB-INF/views/save-result.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewpath);
        dispatcher.forward(request, response);

    }
}
