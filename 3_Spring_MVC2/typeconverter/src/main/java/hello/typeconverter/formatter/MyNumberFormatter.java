package hello.typeconverter.formatter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.Formatter;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
@Slf4j
public class MyNumberFormatter implements Formatter<Number> {// String은 기본으로 변환되서 Number 타입으로 변환

    //Number는 integer,Double, Float의 부모 클래스 (스프링이 제공)

    @Override
    public Number parse(String text, Locale locale) throws ParseException {
        log.info("test = {}, local ={}", text, locale);
        // "1,000" -> 1000
        NumberFormat format = NumberFormat.getInstance(locale);
        return format.parse(text); // "1,000" -> 1000
    }

    @Override
    public String print(Number object, Locale locale) {
        log.info("object={}, local={}", object,locale);
        NumberFormat format = NumberFormat.getInstance(locale);
        return format.format(object);
    }
}
