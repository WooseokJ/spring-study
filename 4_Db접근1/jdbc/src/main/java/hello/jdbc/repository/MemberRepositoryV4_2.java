package hello.jdbc.repository;


import hello.jdbc.domain.Member;
import hello.jdbc.repository.ex.MyDbException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
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
public class MemberRepositoryV4_2 implements MemberRepository{
    private final DataSource dataSource;
    private final SQLExceptionTranslator exceptionTranslator;

    public MemberRepositoryV4_2(DataSource dataSource) {
        this.dataSource = dataSource;
        this.exceptionTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
    }

    // 등록
    public Member save(Member member){
        String sql = "insert into member(member_id, money) values(?,?)";
        Connection connection = null;
        PreparedStatement pstmt = null;
        try {
            connection = dataSource.getConnection();
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());
            pstmt.executeUpdate();
            return  member;

        }catch (SQLException e) {
            throw exceptionTranslator.translate("save", sql, e);
        }finally {
            close(connection, pstmt, null);
        }

    }


    // 조회
    public Member findById(String memberId) {
        String sql = "select * from member where member_id = ?";
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            connection = dataSource.getConnection();
             pstmt = connection.prepareStatement(sql);
             pstmt.setString(1,memberId);
             rs = pstmt.executeQuery();// 쿼리문 결과 반환
            // next를 한번 호출해줘야 실제데이터부터 시작한다.
            // rs는 데이터 아무것도 안가리키고 next를 호출해야 데이터 있는지없는지 확인한다.(있으면 true)
             if( rs.next()) {
                 Member member = new Member();
                 member.setMemberId(rs.getString("member_id"));
                 member.setMoney(rs.getInt("money"));
                 return member;
             } else { //데이터 없으므로
                 throw new NoSuchElementException("member not found memberId=" + memberId);
             }

        }
        catch (SQLException e) {
            throw exceptionTranslator.translate("findById", sql, e);
        }finally {
            close(connection, pstmt, rs);
        }
    }


    // 수정
    public void update(String memberId, int money) {
        String sql = "update member set money=? where member_id=?";
        Connection connection = null;
        PreparedStatement pstmt = null;
        try {
            connection = dataSource.getConnection();
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);

            int resultSize = pstmt.executeUpdate(); // data개수 100개이면 100 반환.
            log.info("resultSize= {}", resultSize);

        }catch (SQLException e) {
            throw exceptionTranslator.translate("update", sql, e);
        }finally {
            close(connection, pstmt, null);
        }

    }

    // 삭제
    public void delete(String memberId)  {
        String sql = "delete from member where member_id=?";
        Connection connection = null;
        PreparedStatement pstmt = null;
        try {
            connection = dataSource.getConnection();
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, memberId);

            int resultSize = pstmt.executeUpdate();
            log.info("resultSize= {}", resultSize);

        }catch (SQLException e) {
            throw exceptionTranslator.translate("delete", sql, e);
        }finally {
            close(connection, pstmt, null);
        }

    }


    private void close(Connection connection, Statement stmt, ResultSet rs) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        // 주의: 트랜잭션 동기화 사용하려면 DataSourceUtils 사용.
        DataSourceUtils.releaseConnection(connection, dataSource);
    }
    // 트랜잭션 동기화의 connection 가져오기.
    private Connection getConnection() throws SQLException{
        // 주의: 트랜잭션 동기화 사용하려면 DataSourceUtils 사용.
        Connection connection = DataSourceUtils.getConnection(dataSource);
        log.info("get connection= {}, class= {}", connection, connection.getClass());
        return connection;
    }

}

