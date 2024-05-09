package hello.jdbc.repository;


import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

// jdbc - 커넥션을 파라미터로 넘기기
@Slf4j
@RequiredArgsConstructor
public class MemberRepositoryV2 {
    private final DataSource dataSource;


    // db에 전달할 sql정의.
    public Member save(Member member) throws SQLException{
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
            e.printStackTrace();
            throw e;

        }finally {
            // 리소스 정리
            // 리소스 정리를 안하는것을 리소스 누수라하는데 Connection 장애가 발생할수있다.
            close(connection, pstmt, null);
        }

    }


    //  조회
    public Member findById(String memberId) throws SQLException{
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
            log.info("error", e);
            throw e;
        }finally {
            close(connection, pstmt, rs);
        }
    }


    // 수정
    public void update(String memberId, int money) throws SQLException{
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
            log.error("db error", e);
            throw e;
        }finally {
            close(connection, pstmt, null);
        }

    }

    // 삭제
    public void delete(String memberId) throws SQLException {
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
            log.error("db error", e);
            throw e;
        }finally {
            close(connection, pstmt, null);
        }

    }


    //  조회(트랜잭션 예제에서사용 - Connection을 파라미터로 받음.)
    public Member findById(String memberId, Connection connection) throws SQLException{
        String sql = "select * from member where member_id = ?";
        //        Connection connection2 = null; // 다른 새로운 커넥션 만들면안됨.
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
//            connection = getConnection(); // 다른 새로운 커넥션 만들면안됨.
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
            log.info("error", e);
            throw e;
        }finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(pstmt);
            // 여기서 Connection 닫으면안되서 작성안해준것.
//            JdbcUtils.closeConnection(connection);
        }
    }

    // 수정(트랜잭션 예제에서 사용 - Connection을 파라미터로 받음.)
    public void update(String memberId, int money, Connection connection) throws SQLException{
        String sql = "update member set money=? where member_id=?";
//        Connection connection2 = null; // 다른 새로운 커넥션 만들면안됨.
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            //            connection = getConnection(); // 다른 새로운 커넥션 만들면안됨.
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);

            int resultSize = pstmt.executeUpdate(); // data개수 100개이면 100 반환.
            log.info("resultSize= {}", resultSize);

        }catch (SQLException e) {
            log.error("db error", e);
            throw e;
        }finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(pstmt);
            // 여기서 Connection 닫으면안되서 작성안해준것.
//            JdbcUtils.closeConnection(connection);
        }

    }


    private void close(Connection connection, Statement stmt, ResultSet rs) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        JdbcUtils.closeConnection(connection);
    }

    // 이제이거 안써도된다.
//    private Connection getConnection() {
//        return DBConnectionUtil.getConnection();
//    }

}

