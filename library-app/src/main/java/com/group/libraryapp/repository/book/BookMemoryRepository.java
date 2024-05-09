package com.group.libraryapp.repository.book;

import org.springframework.stereotype.Repository;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BookMemoryRepository implements BookRepository{
    // 메모리에 저장하기위함.
//    private final List<Book> books = new ArrayList<>();

    public void saveBook() {
//        books.add(new Book());
    }
}
