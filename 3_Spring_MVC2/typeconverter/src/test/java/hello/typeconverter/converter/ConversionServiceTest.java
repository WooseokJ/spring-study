package hello.typeconverter.converter;


import hello.typeconverter.type.IpPort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.support.DefaultConversionService;

import static org.assertj.core.api.Assertions.*;

public class ConversionServiceTest {
    @Test
    void conversionService() {
        // 등록(실제로는 스프링빈에 등록해두고 conversionService를 가져와서 사용하면된다)
        DefaultConversionService conversionService = new DefaultConversionService();
        conversionService.addConverter(new StringToIntegerConverter());
        conversionService.addConverter(new IntegerToStringConverter());
        conversionService.addConverter(new IpPortToStringConverter());
        conversionService.addConverter(new StringToIpPortConverter());

        // 사용
        Integer result = conversionService.convert("10", Integer.class);// 문자 10 -> 숫자 10
        assertThat(result).isEqualTo(10);

        String result2 = conversionService.convert(new IpPort("127.0.0.1", 8080), String.class);
        assertThat(result2).isEqualTo("127.0.0.1:8080");



    }
}
