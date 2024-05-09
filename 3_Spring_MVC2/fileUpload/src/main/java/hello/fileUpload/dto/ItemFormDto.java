package hello.fileUpload.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ItemFormDto { // 상품 저장용 폼
    private Long itemId;
    private String name;
    private MultipartFile attachFile;
    private List<MultipartFile> imageFiles; // 이미지는 다중업로드위해 MultipartFile사용.

}
