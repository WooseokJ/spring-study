package com.group.libraryapp.dto.calc.request;

import lombok.Getter;

@Getter
public class CalcMutiplyRequestDto {
    private int num1;
    private int num2;

    public CalcMutiplyRequestDto(int num1, int num2) {
        this.num1 = num1;
        this.num2 = num2;
    }
}
