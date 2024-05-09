package hello.fileUpload.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

@Slf4j
@Controller
@RequestMapping("/servlet/v2")
public class ServletUploadControllerV2 {

    @Value("${file.dir}")
    private String filedir;

    @GetMapping("/upload")
    public String newFile() {
        return "upload-form";
    }

    @PostMapping("/upload")
    public String saveFileV1(HttpServletRequest request) throws ServletException, IOException {
        log.info("request= {} ", request);

        String username = request.getParameter("itemName");
        log.info("username= {}", username);

        Collection<Part> parts = request.getParts(); // multipart-form로 요청오면 http body의 각데이터들.
        log.info("parts= {}", parts);

        for (Part part : parts) {
            log.info("====================");
            log.info("name= {}", part.getName());
            Collection<String> headerNames = part.getHeaderNames(); // part들 각각 header가있다.
            for (String headerName : headerNames) {
                // headerName은 content-disposition
                // part.getHeader(headerName)은 해더정보(form-data; name="file"; filename="mangom.png")
                log.info("header1  {} {}", headerName, part.getHeader(headerName) );

            }
            //편의 메서드 제공
            // content-disposition: form-data; name="file", filename="image.png"
            // 위에서 filename을 꺼내기가 불편해서 편의메서드 제공.
            log.info("filename= {}", part.getSubmittedFileName()); // 클라가 전달한 파일명
            log.info("size={}", part.getSize()); // part body size

            // 데이터읽기
            InputStream inputStream = part.getInputStream(); // part의 전송데이터 읽을수있다.
            String body = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8); // 바이너리데이터 <-> 문자 일떄 항상 캐릭터셋 정해줘야해.
            log.info("body = {}",body);

            // 파일에 저장하기
            if(StringUtils.hasText(part.getSubmittedFileName())) { //  해당 문자열이 공백을 포함해 비어있지 않은지 검사함(공백 문자열이면 false)
                String fullPath = filedir + part.getSubmittedFileName(); // 디렉토리+파일명 , /user/wooseok/~~~/파일명
                log.info("fullPath={}",fullPath);
                part.write(fullPath);// 실제 파일 저장, part통해 전송된 데이터 저장가능.
            }
        }


        return "upload-form";
    }
}
