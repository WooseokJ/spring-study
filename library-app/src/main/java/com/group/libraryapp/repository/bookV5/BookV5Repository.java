package com.group.libraryapp.repository.bookV5;

import com.group.libraryapp.domain.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookV5Repository extends JpaRepository<Book, Long> {
    Optional<Book> findByName(String name);
}
