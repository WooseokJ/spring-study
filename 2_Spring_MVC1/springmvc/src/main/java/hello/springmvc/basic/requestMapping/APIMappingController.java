package hello.springmvc.basic.requestMapping;


import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mapping/users")
public class APIMappingController {

    // 회원 목록조회
    @GetMapping
    public String users() {
        return "get users";
    }

    // 회원등록
    @PostMapping
    public String addUser(){
        return "post user";
    }

    // 회원조회
    @GetMapping("/{userId}")
    public String findUser(@PathVariable String userId ) {
        return "get find user" + userId;
    }

    // 회원수정
    @PatchMapping("/{userId}")
    public String updateUser(@PathVariable String userId ) {
        return "update find user";
    }
    // 회원삭제
    @DeleteMapping("{userId}")
    public String deleteUser(@PathVariable String userId) {
       return "delete user";
    }
}
