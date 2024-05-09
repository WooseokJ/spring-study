package com.group.libraryapp.repository.bookV5;

import com.group.libraryapp.domain.book.loanhistory.UserLoanHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserLoanHistoryRepository extends JpaRepository<UserLoanHistory, Long> {

    // select * from user_loan_history Where book_name = ? And is_return = ?
    boolean existsByBookNameAndIsReturn(String name, boolean isReturn);


    Optional<UserLoanHistory> findByUserIdAndBookName(Long userId, String bookName);
}
