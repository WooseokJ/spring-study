package hello.typeconverter.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
@Slf4j
public class StringToIntegerConverter implements Converter<String, Integer> {
    @Override
    public Integer convert(String source) { // 문자 -> Integer로 바꿈.
        log.info("convert source = {}", source);
        return Integer.valueOf(source);
    }
}
