package com.group.libraryapp.domain.book;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = null;

//    @Column(nullable = false, length = 255, name = "name")
    @Column(nullable = false)
    private String name;

    public Book(String name) { // id 는 자동생성이라서
        if(name == null || name.isBlank()) {
            String str = String.format("%s는 잘못된 이름이다.", name);
            throw new IllegalArgumentException(str);
        }
        this.name = name;
    }

//    // jpa는 기본생성자 필요
//    protected Book() {}
}
