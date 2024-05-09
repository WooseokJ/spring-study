package hello.jdbc.repository;


import hello.jdbc.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/*
SQLExceptionTranslator 추가
*/
@Slf4j
@RequiredArgsConstructor
public class MemberRepositoryV5 implements MemberRepository{
    private final JdbcTemplate template;

    public MemberRepositoryV5(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    // 등록
    public Member save(Member member){
        String sql = "insert into member(member_id, money) values(?,?)";
        // excuteupdate라서 update로 라임 맞춰서 만들어진거.
        template.update(sql, member.getMemberId(), member.getMoney()); // sql문, 파라미터들 넘겨줘(sql 파라미터 순서 대로 )
        return member;
    }


    // 조회
    public Member findById(String memberId) {
        String sql = "select * from member where member_id = ?";
        // 조회떄는 Mapping하는 코드만 넣어줌.
        return template.queryForObject(sql, memberRowMapper(), memberId);
    }


    // 수정
    public void update(String memberId, int money) {
        String sql = "update member set money=? where member_id=?";
        template.update(sql, money, memberId); // 순서주의: sql 순서 대로
    }

    // 삭제
    public void delete(String memberId)  {
        String sql = "delete from member where member_id=?";
        template.update(sql, memberId);
    }

    private RowMapper<Member> memberRowMapper() {
        return (resultSet, rowNum) ->  { // resultSet: sql 결과
            Member member = new Member();
            member.setMemberId(resultSet.getString("member_id"));
            member.setMemberId(resultSet.getString("money"));
            return member;
        };
    }
}

