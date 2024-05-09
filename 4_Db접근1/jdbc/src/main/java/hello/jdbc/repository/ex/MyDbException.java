package hello.jdbc.repository.ex;

public class MyDbException extends RuntimeException{ // 체크예외 -> 런타임예외로 변환용.
    public MyDbException() {
    }

    public MyDbException(String message) {
        super(message);
    }

    public MyDbException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyDbException(Throwable cause) {
        super(cause);
    }
}
