package hello.login.web.session;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component // spring bean 등록
public class SessionManager {
    private Map<String, Object> sessionStroe = new ConcurrentHashMap<>(); // 동시에 여러 요청들어오는 동시성문제해결위해 사용.
    private final String SESSION_COOKIE_NAME = "mysessionID";
    /*
        세션생성
        sessionID생성(추적불가)
        세션저장소 -> sessionID : 보관할값 저장
        응답에 sessionID로 쿠키 생성해 전달.
    * */
    // 세션 생성.
    public void createSession(Object value, HttpServletResponse response) {
        // sessionId생성, 값 세션 저장.
        String sessionID = UUID.randomUUID().toString(); // 임의의값.(전우주적으로 하나의 아아디, Universal Unique Identifier)
        sessionStroe.put(sessionID, value);

        // 쿠키 생성
        Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionID);
        response.addCookie(mySessionCookie);

    }



    // 세션조회
    public Object getSession(HttpServletRequest request) {
        // 방법 1
        // 쿠키찾아
//        Cookie[] cookies = request.getCookies(); // 쿠키가 배열로 받아진다.
//        if(cookies == null) {return null;}
//        for (Cookie cookie : cookies) {
//            if(cookie.getName().equals(SESSION_COOKIE_NAME)) {
//                return sessionStroe.get(cookie.getValue());
//            }
//        }
//        return null;


        // 방법 2
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if(sessionCookie == null) {return null;}
        return sessionStroe.get(sessionCookie.getValue());
    }
    // 쿠키가 배열로 받아져서 정확히 쿠키하나찾는 로직.
    public Cookie findCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if(cookies == null) {return null;}
        Cookie findCookie = Arrays.stream(cookies) // 배열의값 루프 돌림.
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findFirst()// findAny는 순서상괎없이 하나 출력, findFist는 순서대로 루프돌릴떄 하나 출력.
                .orElse(null);
        return findCookie;
    }

    // 쿠키 종료.
    public void expire(HttpServletRequest request) {
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if( sessionCookie != null) {
            sessionStroe.remove(sessionCookie.getValue());
        }
    }
}
