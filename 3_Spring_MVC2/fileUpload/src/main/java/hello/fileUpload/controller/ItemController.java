package hello.fileUpload.controller;

import hello.fileUpload.domain.Item;
import hello.fileUpload.domain.UploadFile;
import hello.fileUpload.dto.ItemFormDto;
import hello.fileUpload.file.FileStore;
import hello.fileUpload.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemRepository itemRepository;
    private final FileStore fileStore;

    @GetMapping("/items/new")
    public String newItem(@ModelAttribute ItemFormDto requestDto) {
        return "item-form";
    }

    // 상품 업로드 기능(피알1개, 이미지여러개 업로드 가능.)
    @PostMapping("/items/new")
    public String saveItem(@ModelAttribute ItemFormDto requestDto,
                           RedirectAttributes redirectAttributes) throws IOException {
        // 파일1개 업로드
        MultipartFile attachFile = requestDto.getAttachFile();
        UploadFile uploadFile = fileStore.storeFile(attachFile);

        //이미지여러개 업로드
        List<MultipartFile> imageFiles = requestDto.getImageFiles();
        List<UploadFile> imageUploadFiles = fileStore.storeFiles(imageFiles);

        // DB에 저장.(지금은 Hashmap에 저장)
        Item item = new Item();
        System.out.println("item = " + requestDto.getName());
        item.setName(requestDto.getName());
        item.setAttachFile(uploadFile);
        item.setImageFiles(imageUploadFiles);
        System.out.println("item = " + item.getName());
        System.out.println("item = " + item.getId());
        System.out.println("item = " + item.getAttachFile());
        System.out.println("item = " + item.getImageFiles());
        itemRepository.save(item);

        // 리다이렉트
        redirectAttributes.addAttribute("itemId", item.getId());
        return "redirect:/items/{itemId}";
    }

    // 파일을 웹에서 화면보기.
    @GetMapping("/items/{id}")
    public String items(@PathVariable Long id, Model model) {
        Item findItem = itemRepository.findByItem(id);
        model.addAttribute("item", findItem);
        return "item-view";

    }

    // 파일 다운로드
    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException { // 주의: Resource는 Spring꺼.
        // file:/User/wooseok~~~.png
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }

    // 파일1개 올린거 다운로드
    @GetMapping("/attach/{itemId}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable Long itemId) throws MalformedURLException {
        Item findItem = itemRepository.findByItem(itemId);
        String storeFileName = findItem.getAttachFile().getStoreFileName();
        String uploadFileName = findItem.getAttachFile().getUploadFileName();
        UrlResource resource = new UrlResource("file:" + fileStore.getFullPath(storeFileName));

        // 이거만들어서 header에 넣어줘야 파일다운로드가됨(없으면 그냥 웹브라우저에 보여지기만함)
        String encodedUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }


}
