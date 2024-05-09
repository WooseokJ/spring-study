package com.group.libraryapp.domain.book.loanhistory;

import com.group.libraryapp.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserLoanHistory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = null; // 기본이 null

//    private Long userId;

    @ManyToOne // 대출기록은 여러개, 사용자는 1명
    private User user;
    private String bookName;
    private boolean isReturn;

    public UserLoanHistory(User user, String bookName) {
        this.user = user;
        this.bookName = bookName;
        this.isReturn = false; // 대출중으로 바꿔야하므로 false
    }

    // 반납처리
    public void chageIsReturn() {
        this.isReturn = true;
    }
}
