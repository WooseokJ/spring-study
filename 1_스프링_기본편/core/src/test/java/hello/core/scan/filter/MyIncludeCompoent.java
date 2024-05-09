package hello.core.scan.filter;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Indexed;

import java.lang.annotation.*;
// compoent스캔에 추가할거야 (@COmpoent안에있다)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyIncludeCompoent {

}
