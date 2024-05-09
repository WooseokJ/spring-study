package hello.core.scan.filter;

import java.lang.annotation.*;

// compoent스캔에 추가할거야 (@COmpoent안에있다)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyExcludeCompoent {
}
