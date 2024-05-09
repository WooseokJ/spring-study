package com.group.libraryapp.controller.book;

import com.group.libraryapp.dto.book.request.BookCreateReqeustDto;
import com.group.libraryapp.dto.book.request.BookLoanRequestDto;
import com.group.libraryapp.dto.book.request.BookReturnRequestDto;
import com.group.libraryapp.service.book.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping("/book")
    public void saveBook(@RequestBody BookCreateReqeustDto reqeustDto) {
        bookService.saveBook(reqeustDto);
    }



    @PostMapping("/book/loan")
    public void saveLoan(@RequestBody BookLoanRequestDto requestDto) {
        bookService.saveLoan(requestDto);
    }

    @PutMapping("/book/return")
    public void returnBook(@RequestBody BookReturnRequestDto requestDto) {
        bookService.returnLoan(requestDto);

    }
}
