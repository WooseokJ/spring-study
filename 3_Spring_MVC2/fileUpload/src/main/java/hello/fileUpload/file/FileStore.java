package hello.fileUpload.file;

import hello.fileUpload.domain.UploadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileStore { // 파일 저장과관련된 업무 처리하는 별도객체.
    @Value("${file.dir}")
    private String filedir;

    // fullpath 만들기
    public String getFullPath(String fileName) {
        return filedir + fileName ;
    }

    //파일 1개 저장
    public UploadFile storeFile(MultipartFile file) throws IOException {
        if(file.isEmpty()) {
            return null;
        }
        // mangom.png
        String originalFilename = file.getOriginalFilename(); // 클라가 보낸 파일명

        //서버에 저장하는 파일명
        String storeFileName = createStoreFileName(originalFilename); // 서버내부에저장할 파일명.
        file.transferTo(new File(getFullPath(storeFileName)));
        return new UploadFile(originalFilename, storeFileName);
    }

    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename); //확장자 뽑아내기
        String uuid = UUID.randomUUID().toString(); // ex) "abc-abc-abc"
        return uuid + "." + ext; // "abc-abc-abc.png"
    }

    // 확장자 꺼내는메서드 ex) png, jpg
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1); // ext가 확장자(png, jpg)
    }


    // 이미지 저장
    public List<UploadFile> storeFiles(List<MultipartFile> files) throws IOException {
        List<UploadFile> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : files) {
            if(!multipartFile.isEmpty()) { // 들어온 파일정보존재하면
                UploadFile uploadFile = storeFile(multipartFile);// 위에만든 파일1개 저장 메서드.
                storeFileResult.add(uploadFile);
            }
        }
        return storeFileResult;
    }


}
