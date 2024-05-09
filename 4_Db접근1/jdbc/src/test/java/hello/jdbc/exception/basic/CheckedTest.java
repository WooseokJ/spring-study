package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class CheckedTest {

    // Exception을 상속받은 예외는 체크예외된다.(만약 RuntimeException 상속받으면 언체크예외됨 - 문법으로정함.)
    // 커스텀 예외
    static class MyCheckedException extends Exception {
        public MyCheckedException(String message) { // 생성자 통해 해당기능 그대로 사용.(예외가 제공하는 여러기능중 오류 메세지 보관기능 사용)
            super(message);
        }
    }



    // 예외는 예외를 잡아서 처리하거나 던지거나 둘중하나로 필수선택해야함.
    static class Service {
        Repository repository = new Repository();
        // 예외 잡아서 처리하는 코드
        public void callCatch(){
            try {
                repository.call();
            }catch (MyCheckedException e ) {
                //MyCheckedException가 Exception 자식이라서 더 부모인 Exception으로 바꿔도된다.(상위꺼 잡으면 하위꺼도 같이 잡혀)
                // 그런데 보통 Exception으로 하면 다른예외도 잡으므로 정확한 자식 예외(MyCheckedException) 잡는게 좋다.

                // 예외 처리 로직.
                log.info("예외처리 message = {}", e.getMessage(), e); //마지막 e는 스택트레이스 해주는것 출력..
//                e.printStackTrace();
            }
        }


        // 예외를 던질거
        // 체크 예외는 예외잡지않고 밖으로 던질려면 throws 예외를 메서드에 필수 선언해야함.
        public void callThrow() throws MyCheckedException { // Exception으로 던져도되지만 다른예외들도 던져져서 안좋은코드이다.
            repository.call();
        }

    }

    static class Repository {
        // throws MyCheckedException가 없으면 컴파일오류가 나와서 컴파일러가 체크해줌.
        public void call() throws MyCheckedException{
            throw new MyCheckedException("ex"); // 커스텀 예외 발생
        }
    }

    // 테스트 실행
    Service service = new Service();

    @Test // 예외 잡을거
    void checked_catch() {
        service.callCatch();
    }

    @Test // 예외 던질거
    void checked_throw() { // 메서드에 throws MyCheckedException안적고 확인하려면 assertThatThrownBy활용.
        Assertions.assertThatThrownBy(() -> service.callThrow()) // 예외발생시 MyCheckedException이면 통과.(MyCheckedException은 체크예외라 컴파일 오류 낸다)
                .isInstanceOf(MyCheckedException.class);


    }
}