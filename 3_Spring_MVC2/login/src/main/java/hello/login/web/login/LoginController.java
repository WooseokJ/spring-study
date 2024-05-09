package hello.login.web.login;

import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import hello.login.web.session.SessionConst;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm loginForm) {
        return "login/loginForm";
    }

//    @PostMapping("/login")
    public String loginV1(@Validated @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response) {
        if(bindingResult.hasErrors()) {
            return "login/loginForm";
        }
        // login 시도
        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if(loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비번 일치 안해");
            return "login/loginForm";
        }

        // 로그인 성공시

        // 쿠키 생성 (쿠키에 시간정보 안주면 -> 세션쿠기 생성.)
        String DbId = String.valueOf(loginMember.getId());
        Cookie idCookie = new Cookie("memberId", DbId);// key=value 형태 ex) 클라는 memberId=1 의 쿠키값를 받음.
        // 이쿠키를 클라로 보내줘야해.
        response.addCookie(idCookie);


        return "redirect:/";
    }
    // V2: 직접만든 세션 사용.
    private final SessionManager sessionManager;  // @Component해둔것만 자동의존성 주입.
//    @PostMapping("/login")
    public String loginV2(@Validated @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response) {
        if(bindingResult.hasErrors()) {
            return "login/loginForm";
        }
        // login 시도
        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if(loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비번 일치 안해");
            return "login/loginForm";
        }

        // 로그인 성공시

        // 세션 관리자 통해 세션생성하고, 회원데이터 보관.
        sessionManager.createSession(loginMember, response);

        return "redirect:/";
    }
    // V3: 서블릿 지원 세션사용


//    @PostMapping("/login")
    public String loginV3(@Validated @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletRequest request) {
        if(bindingResult.hasErrors()) {
            return "login/loginForm";
        }
        // login 시도
        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if(loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비번 일치 안해");
            return "login/loginForm";
        }

        // 로그인 성공시

        // 세션은 메모리 사용하는거라 꼭 필요할떄만 생성해야한다.
        /* getSession
            true(default): 세션있으면 세션반환(재사용), 없으면 new 세션생성.반환.
            false:세션있으면 세션반환(재사용), 없으면 세션생성x, null 반환.

        */
        HttpSession session = request.getSession(true); // default가 true
        // 세션에 로그인 회원정보 보관.(세션에 데이터보관)
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        // 세션 관리자 통해 세션생성하고, 회원데이터 보관.

        return "redirect:/";
    }

    // V4: 서블릿 필터 적용 + 로그인후에 홈화면아닌 상품관리로 넘어가게 하기위함.


    @PostMapping("/login")
    public String loginV4(@Validated @ModelAttribute LoginForm form, BindingResult bindingResult,
            @RequestParam(defaultValue = "/") String redirectURL,
            HttpServletRequest request) {
        if(bindingResult.hasErrors()) {
            return "login/loginForm";
        }
        // login 시도
        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if(loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비번 일치 안해");
            return "login/loginForm";
        }

        // 로그인 성공시

        // 세션은 메모리 사용하는거라 꼭 필요할떄만 생성해야한다.
        /* getSession
            true(default): 세션있으면 세션반환(재사용), 없으면 new 세션생성.반환.
            false:세션있으면 세션반환(재사용), 없으면 세션생성x, null 반환.

        */
        HttpSession session = request.getSession(true); // default가 true
        // 세션에 로그인 회원정보 보관.(세션에 데이터보관)
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        // 세션 관리자 통해 세션생성하고, 회원데이터 보관.

        return "redirect:" + redirectURL;
    }


}
