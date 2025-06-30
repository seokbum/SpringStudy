package ex_01hash;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/*
 * 1. usercipher 테이블 생성하기 .useraccount 와 같은 테이블로 생성하기
 * 	  ->CREATE TABLE usercipher SELECT * FROM useraccount;
 * 2. usercipher의 password 컬럼의 크기를 300으로 변경
 * 	  -> ALTER TABLE usercipher MODIFY COLUMN password VARCHAR(300);
 * 3. userid 컬럼을 기본키로 설정하기
 * 	  -> ALTER TABLE usercipher ADD CONSTRAINT PRIMARY KEY (userid);
 * 
 *  useraccount 테이블을 읽어서 usercipher 테이블의 password (컬럼)를 SHA-256
 *  알고리즘으로 해쉬값 저장하기
 */
public class Main_02 {
	public static void main(String[] args) throws Exception {
		 Class.forName("org.mariadb.jdbc.Driver");
	        Connection conn =
	                DriverManager.getConnection("jdbc:mariadb://localhost:3306/gdjdb", "gduser", "1234");

	        PreparedStatement pstmt = conn.prepareStatement("select userid, password from useraccount");
	        ResultSet rs = pstmt.executeQuery();
	        while (rs.next()) {
	            String userid = rs.getString("userid");
	            String password = rs.getString("password");
	            MessageDigest md = MessageDigest.getInstance("SHA-256");
	            if(password == null) {
	            	continue;
	            }
	            String hashpass = "";
	            byte[] plain = password.getBytes(); // 원본 비밀번호. byte[]로
	            byte[] hash = md.digest(plain);

	            for (byte b : hash) {
	                hashpass += String.format("%02x", b);
	            }
	            pstmt.close();
	            pstmt = conn.prepareStatement("update usercipher set password=? where userid=?");
	            pstmt.setString(1, hashpass);
	            pstmt.setString(2, userid);
	            pstmt.executeUpdate();

	        }
	}
}
