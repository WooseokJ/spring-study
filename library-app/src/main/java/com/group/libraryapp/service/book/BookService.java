package com.group.libraryapp.service.book;

import com.group.libraryapp.domain.book.Book;
import com.group.libraryapp.domain.book.loanhistory.UserLoanHistory;
import com.group.libraryapp.domain.user.User;
import com.group.libraryapp.dto.book.request.BookCreateReqeustDto;
import com.group.libraryapp.dto.book.request.BookLoanRequestDto;
import com.group.libraryapp.dto.book.request.BookReturnRequestDto;
import com.group.libraryapp.repository.book.BookMemoryRepository;
import com.group.libraryapp.repository.book.BookRepository;
import com.group.libraryapp.repository.bookV5.BookV5Repository;
import com.group.libraryapp.repository.bookV5.UserLoanHistoryRepository;
import com.group.libraryapp.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
@Transactional
public class BookService {

    private final BookV5Repository bookV5Repository;
    private final UserLoanHistoryRepository userLoanHistoryRepository;
    private final UserRepository userRepository;

    public  void saveBook(@RequestBody BookCreateReqeustDto reqeustDto) {
        Book book = new Book(reqeustDto.getName());
        bookV5Repository.save(book);
    }

    public void saveLoan(@RequestBody BookLoanRequestDto requestDto) {
        // 1. 책정보 가져옴(이름기준)
        Book book = bookV5Repository.findByName(requestDto.getBookName())
                .orElseThrow(IllegalArgumentException::new);
        // 2. 대출중인지 확인,  대출중이면 예외발생
        if (userLoanHistoryRepository.existsByBookNameAndIsReturn(book.getName(), false)) { // 대여중이면 false
            throw new IllegalArgumentException("대축중인 책");
        }

        // 3.user정보 가져옴
        User user = userRepository.findByName(requestDto.getUserName())
                .orElseThrow(IllegalArgumentException::new);

        // 4. 유저 정보와 책정보 기반으로 UserLoanHistory를 저장.


//        UserLoanHistory userLoanHistory = new UserLoanHistory(user, book.getName());
//        userLoanHistoryRepository.save(userLoanHistory);

        // 4. 유저 정보와 책정보 기반으로 UserLoanHistory를 저장.( 리팩토링)
        user.loanBook(book.getName());

    }


    public void returnLoan(@RequestBody BookReturnRequestDto requestDto) {
         // 요청받는건 유저이름, 책이름 인데 UserLoanHistory에서는 유저id, 책이름으로 대출기록테이블가져옴.   그래서 유저이름으로 유저찾아서 유저id가져오자
        User user = userRepository.findByName(requestDto.getUserName())
                .orElseThrow(IllegalArgumentException::new);



        // 대출기록 반납하기
//        UserLoanHistory userLoanHistory = userLoanHistoryRepository.findByUserIdAndBookName(user.getId(), requestDto.getBookName())
//                .orElseThrow(IllegalArgumentException::new);

//        userLoanHistory.chageIsReturn(); // true로 바꿈.(대출중 아님 으로 바꿈)
//        userLoanHistoryRepository.save(userLoanHistory); // 안써줘도됨. @트랜잭션을 사용해서 영속성컨특스트가 존재하고 이는 변경감지에의해 자동저장한다.

        // 대출기록 반납하기 (리팩토링)
        user.returnBook(requestDto.getBookName());

    }
}
