package com.group.libraryapp.domain.user;

import com.group.libraryapp.domain.book.loanhistory.UserLoanHistory;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

// 실제 유저를 DB에 저장되게 하기위한 뼈대
@Entity   // : 스프링이 User객체와 user 테이블을 같은것으로 바라본다, DB쪽에 저장되고 관리되어야할 데이터라는 의미.
@Getter
public class User {

    protected User() {}

    @Id // 이 필드는 primary key라는것을 Db애 알려줌.
    @GeneratedValue(strategy = GenerationType.IDENTITY) //  auto_increment라는것을 DB에 알려줌.
    private Long id = null;

    @Column(nullable = false, length = 20, name = "name") // name varchar(20)
    private String name;
//    @Column(name = "age") 완전히 DB필드와 객체 필드 동일하면 @Column 자체 생략가능. (int, Integer는 동일하게 취급)
    private Integer age;



    //User 입장에선 1명인데  UserLoanhistory 는 여러개의 대출기록 갖을수있으므로 배열만든다.

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY) // 연관관계 주인 변수명, casCade로인해 연관된 테이블의 정보도 같이 변경.
    private List<UserLoanHistory> userLoanHistories = new ArrayList<>(); //처음엔 비워둠.





    public User(String name, Integer age) throws IllegalArgumentException{
        if (name == null || name.isBlank()) { // 이름 비어있을떄
            String message = String.format("잘못된 name(%s)가 들어왓씁니다.", name);
            throw new IllegalArgumentException(message);
        }
        this.name = name;
        this.age = age;
    }

    // 이름변경 메서드
    public void updateName(String name) {
        this.name = name;
    }

    //대출기능
    public void loanBook(String bookName) {
        UserLoanHistory userLoanHistory = new UserLoanHistory(this, bookName);
        this.userLoanHistories.add(userLoanHistory);
    }
    //반납기능
    public void returnBook(String bookName) {
        UserLoanHistory userLoanHistory = this.userLoanHistories.stream()
                .filter(history -> history.getBookName().equals(bookName)) // 조건충족한거 찾는다.
                .findFirst() // 첫번쨰꺼 찾는다.
                .orElseThrow(IllegalArgumentException::new);
        userLoanHistory.chageIsReturn();

    }


}
