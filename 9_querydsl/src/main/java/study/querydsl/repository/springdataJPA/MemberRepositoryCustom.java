package study.querydsl.repository.springdataJPA;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.querydsl.dto.MemberCond;
import study.querydsl.dto.MemberTeamDto;

import java.util.List;

// 동적쿼리 작성위한 인터페이스 생성. 주의: Entity명+RepositoryCutom으로 꼭 만들어야함.
public interface MemberRepositoryCustom {
    List<MemberTeamDto> search(MemberCond cond);

    Page<MemberTeamDto> searchSimple(MemberCond cond, Pageable pageable);
    Page<MemberTeamDto> searchComplex(MemberCond cond, Pageable pageable);

}
