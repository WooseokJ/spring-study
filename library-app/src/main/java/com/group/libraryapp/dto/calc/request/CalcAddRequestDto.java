package com.group.libraryapp.dto.calc.request;

import lombok.Getter;

@Getter
public class CalcAddRequestDto {
    private final int num1;
    private final int num2;

    public CalcAddRequestDto(int num1, int num2) {
        System.out.println("calcAddRequestDto 생성자 호출");
        this.num1 = num1;
        this.num2 = num2;
    }
}
