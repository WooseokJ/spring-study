package hello.fileUpload.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UploadFile {
    private String uploadFileName; // 클라가 업로드한 파일명
    private String storeFileName; // 서버내부에서 관리하는 파일명.
    // 클라가 업로드한 파일명으로 서버내부에서 관리할 파일명으로 저장하면안된다.
    //이유: 다른 두사람이 같은파일명으로 저장할수도있으므로 storeFileName은 uuid로 만들어 저장되어 관리.
}
