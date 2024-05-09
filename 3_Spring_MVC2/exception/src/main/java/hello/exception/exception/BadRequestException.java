package hello.exception.exception;



import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// 원래 BadRequestException 예외발생시 500 에러 난다.
// 하지만 500 에러나면 400으로 바꿔버릴거임.
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "error.bad")
public class BadRequestException extends RuntimeException{
}
