package com.group.libraryapp.controller.calc;

import com.group.libraryapp.dto.calc.request.CalcAddRequestDto;
import com.group.libraryapp.dto.calc.request.CalcMutiplyRequestDto;
import org.springframework.web.bind.annotation.*;

// 하나의 Controlelr안에 여러 API를 추가할수있다.
@RestController
public class CalcController {

    // 쿼리로 받을떄 , add?num1=10&num2=20
    @GetMapping("/add")
    public int onePlusOne(
            @RequestParam int num1, // @RequestParam: 쿼리 파라미터
            @RequestParam int num2
    ) {
        return num1 + num2;
    }
    // dto로 변환해서 받기
    @GetMapping("/adddto") // 요청들어오면 dto의 필드들에 생성자 호출해 값넣어줌.
    public int dtoTransfer(CalcAddRequestDto requestDto) {
        return requestDto.getNum1() + requestDto.getNum2();
    }

    // post http://localhost:8080/multiply , body: {"num1":10, "num2": 33}
    // @RequestBody: http body로 들어오는 json데이터를 customDto로 바꾼다. (단, json의 키와 dto의 변수가 같아야한다)
    @PostMapping("/multiply")
    public int calcMultiply(@RequestBody CalcMutiplyRequestDto requestDto) {
        return requestDto.getNum1() * requestDto.getNum2();
    }

}
