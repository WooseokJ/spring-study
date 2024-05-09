package hello.fileUpload.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/spring/")
public class SpringUploadController {
    @Value("${file.dir}")
    private String filedir;

    @GetMapping("/upload")
    public String newFile() {
        return "upload-form";
    }

    @PostMapping("/upload")
    public String saveFile(@RequestParam String itemName,
                           @RequestParam MultipartFile file) throws IOException { // @ModelAttribute에서도 가능.
        log.info("itemName= {}",itemName);
        log.info("multipartfile= {}",file);
        if(!file.isEmpty()){ // 파일안들어있으면
            String fullPath = filedir + file.getOriginalFilename(); // getOriginalFilename(): 업로드할 파일명.
            log.info("파일 저장 fullPath = {}", fullPath);
            file.transferTo(new File(fullPath)); // transferTo() : 파일저장.
        }
        return "upload-form";

    }
}
