package hello.jdbc.repository;


import hello.jdbc.domain.Member;
import hello.jdbc.repository.ex.MyDbException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/*
예외 누수 문제 해결하기
체크예외 -> 런타임예외
throw SQLException 제외
*/
@Slf4j
@RequiredArgsConstructor
public class MemberRepositoryV4 implements MemberRepository{
    private final DataSource dataSource;

    // 등록
    public Member save(Member member){
        // db등록위해 insert sql
        String sql = "insert into member(member_id, money) values(?,?)";
        Connection connection = null;
        // 파라미터들(member_id,money) ? 통해 바인딩(values의 값들)가능하게해줌., Statement(sql문)를 상속받음.
        // SQL Injection 공격예방위해 꼭 사용.
        PreparedStatement pstmt = null;

        try {
            connection = dataSource.getConnection();
            // DB에 전달할 sql과 파라미터 전달할 데이터 준비
            pstmt = connection.prepareStatement(sql);

            // sql 첫번쨰 ? 에 값지정. 문자라서 setString사용
            pstmt.setString(1, member.getMemberId());
            // sql 두번쨰 ? 에 값지정. 숫자라서 setInt사용.
            pstmt.setInt(2, member.getMoney());

            // statement통해 준비된 sql을 Connection통해 실제 DB에 전달.
            pstmt.executeUpdate(); // executeUpdate는 DB row수를 int형 반환.

            return  member;

        }catch (SQLException e) {
            throw new MyDbException(e);
        }finally {
            // 리소스 정리
            // 리소스 정리를 안하는것을 리소스 누수라하는데 Connection 장애가 발생할수있다.
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
            throw new MyDbException(e);
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
            throw new MyDbException(e);
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
            throw new MyDbException(e);
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

