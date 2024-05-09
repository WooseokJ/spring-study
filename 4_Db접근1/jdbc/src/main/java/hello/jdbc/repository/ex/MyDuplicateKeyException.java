package hello.jdbc.repository.ex;

// DB 중복일떄만 예외 던져.(DB 관련 예외 계층 만듬)
// 직접 만들거라 JDBC, JPA에 종속적이지 않고 서비스 계층 순수성 유지가능.
public class MyDuplicateKeyException extends MyDbException{

    public MyDuplicateKeyException() {
    }

    public MyDuplicateKeyException(String message) {
        super(message);
    }

    public MyDuplicateKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyDuplicateKeyException(Throwable cause) {
        super(cause);
    }
}
