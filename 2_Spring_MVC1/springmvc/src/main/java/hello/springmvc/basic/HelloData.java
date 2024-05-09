package hello.springmvc.basic;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

@Data // @Getter, @Setter , @ToString, @EqualsAndHashCode, @RequiredArgsConstructor 를 다 자동적용.
public class HelloData {
    private String username;
    private int age;
}
