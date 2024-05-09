package hello.typeconverter.converter;

import hello.typeconverter.type.IpPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

@Slf4j
public class StringToIpPortConverter implements Converter<String, IpPort> {

    @Override
    public IpPort convert(String source) {
        log.info("convert source= {}", source); // "127.0.0.01:8080" 문자가 들어올거다.
        String[] split = source.split(":");
        String  ip = split[0]; // 127.0.0.01
        Integer port = Integer.parseInt(split[1]); // 8080
        return new IpPort(ip, port);
    }
}
