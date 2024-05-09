package study.querydsl.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.querydsl.dto.MemberSearchConfition;
import study.querydsl.dto.MemberTeamDto;

import java.util.List;

public interface MemberRepositoryCustom {
    List<MemberTeamDto> search(MemberSearchConfition confition);
    Page<MemberTeamDto> searchPageSimple(MemberSearchConfition confition, Pageable pageable);
    Page<MemberTeamDto> searchPageComplex(MemberSearchConfition confition, Pageable pageable);
}
