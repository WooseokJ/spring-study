package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.argumentresolver.Login;
import hello.login.web.session.SessionConst;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
//    @GetMapping("/")
    public String home() {
        return "home";
    }


//    @GetMapping("/")
    public String homeLoginV1(@CookieValue(name = "memberId", required = false) Long memberId, Model model) { // required = false는 로그인안한 유저도 홈에 들어와야해서
        if( memberId == null) { // 로그인 안한 사용자면 home 보냄
            return "home"; // 이름안보이는 home화면
        }

        // DB에서 찾은 member 없으면 다시 Home, 있으면 통과
        Member loginMember = memberRepository.findById(memberId); // memberId는 쿠키안의 값.
        if(loginMember==null) { // 쿠키가 너무 옛날에 만들어진거라 없을수도있으므로 home으로 그냥보냄.
            return "home";// 이름안보이는 home화면
        }

        // 성공로직
        model.addAttribute("member", loginMember);
        return "loginHome"; // 로그인 정용 회원용 화면.(이름보이는 home화면)

    }



//    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        // 쿠키를 지워버림. (null로 만들어 클라로보냄)
        Cookie cookie = new Cookie("memberId", null);
        // setMaxAge: 인자는 유효기간을 나타내는 초 단위의 정수형이고 만일 유효기간을 0으로 지정하면 쿠키의 삭제를 의미합니다.
        cookie.setMaxAge(0); // 0 으로 하면 쿠키 넘기면서 제거.
        response.addCookie(cookie);
        return "redirect:/";

    }

    // V2 직접만든 세션 적용.
    private final SessionManager sessionManager;

//    @GetMapping("/")
    public String homeLoginV2(HttpServletRequest request,  Model model) { // required = false는 로그인안한 유저도 홈에 들어와야해서
        // 세션광리자에 저장된 회원정보 조회
        Member loginMember = (Member) sessionManager.getSession(request);


        // DB에서 찾은 member 없으면 다시 Home, 있으면 통과
        if(loginMember==null) { // 쿠키가 너무 옛날에 만들어진거라 없을수도있으므로 home으로 그냥보냄.
            return "home";// 이름안보이는 home화면
        }

        // 성공로직
        model.addAttribute("member", loginMember);
        return "loginHome"; // 로그인 정용 회원용 화면.(이름보이는 home화면)

    }

//    @PostMapping("/logout")
    public String logoutV2(HttpServletRequest request) {
        sessionManager.expire(request);
        return "redirect:/";
    }

    // V3: 서블릿 지원 세션적용.

//    @GetMapping("/")
    public String homeLoginV3(HttpServletRequest request,  Model model) {
        HttpSession session = request.getSession(false); //
        if(session==null) {
            return "home";// 이름안보이는 home화면
        }
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        // 세션에 회원데이터없으면 Home
        if(loginMember == null){
            return "home";
        }

        model.addAttribute("member", loginMember);
        return "loginHome"; // 로그인 정용 회원용 화면.(이름보이는 home화면)

    }

    @PostMapping("/logout")
    public String logoutV3(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session != null) {
            session.invalidate(); // 세션이랑 그안의 데이터까지 날라감.
        }
        return "redirect:/";
    }

    // V3_2: @SessionAttribute적용
    @GetMapping("/")
    public String homeLoginV3_2(
            // Member loginMember = (Member) session.getAttribute(SeesionConst.LOGIN_MEMBER); 이거 자동생성.(이미 로그인된 사용자 찾을떄 사용)
            // 참고: 세션을 생성하지않는다.
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
            Model model) {
        // 세션에 회원데이터없으면 Home
        if(loginMember == null){
            return "home";
        }

        model.addAttribute("member", loginMember);
        return "loginHome"; // 로그인 정용 회원용 화면.(이름보이는 home화면)

    }

    // V4:@Login (ArgumentResolver 활용)
    @GetMapping("/")
    public String homeLoginV4(@Login Member loginMember, Model model) {
        // 세션에 회원데이터없으면 Home
        if(loginMember == null){
            return "home";
        }

        model.addAttribute("member", loginMember);
        return "loginHome"; // 로그인 정용 회원용 화면.(이름보이는 home화면)

    }



}