package hello.exception.api;

import hello.exception.exception.BadRequestException;
import hello.exception.exception.UserException;
import hello.exception.exhandler.ErrorResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
public class ApiExceptionController2 {

//    // 중요: 방법 1( api 예외처리시 사용)
//    // 해당 controller에서 예외발생시 아래 메서드가 예외를 잡아 처리.
//    @ResponseStatus(HttpStatus.BAD_REQUEST) //이거없으면 아래처럼 200 OK 가 나오는걸 확인할수있다. 이거해주면 400으로 상태코드 반환된다.
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ErrorResult illgalExHandler(IllegalArgumentException e) {// IllegalArgumentException 자식까지도 예외잡아줌.
//        log.error("IllegalArgumentException 예외 잡음 :", e);
//        return new ErrorResult("베드야", e.getMessage());
//    }
//
//    // 중요: 방법 2 (api 예외처리시 사용)
////    @ExceptionHandler(UserException.class) //UserException가 파라미터와 동일하면 생략가능.
//    public ResponseEntity<ErrorResult> userExHandler(UserException e) {
//        log.info("UserException 예외 잡아", e);
//        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
//        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST)
//
//    }
//
//    // 방법 3
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ExceptionHandler
//    public ErrorResult exHandler(Exception e) { //Exception 최상위라서 위에 Illegal, UserException 이외 예외는 공통으로 다 처리
//        log.info("Exception 예외 잡아", e);
//        return new ErrorResult("Ex", "내부 오류");
//    }



    @GetMapping("/api2/members/{id}")
    public MemberDto getMember(@PathVariable("id") String id) {

        if (id.equals("ex")) { // 예외랑 같으면
            throw new RuntimeException("잘못된 사용자");
        }
        if (id.equals("bad")) {
            throw new IllegalArgumentException("잘못된 입력값");
        }
        if(id.equals("user-ex")) {
            throw  new UserException("사용자 오류");
        }
        return new MemberDto(id, "이름" + id);
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String memberId;
        private String name;
    }

}