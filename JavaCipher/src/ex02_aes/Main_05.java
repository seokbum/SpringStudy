package ex02_aes;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/*
    usercipher 테이블의 내용을 출력하기
    이메일은 복호화 하여 출력하기
    1. usercipher 테이블 읽기
    2. 반복문 실행
        - userid 값의 해쉬값 조회하여 키로 사용
        - 이메일 복호화 실행하기
 */
public class Main_05 {
    public static void main(String[] args) throws Exception {
        Class.forName("org.mariadb.jdbc.Driver");
        Connection conn =
                DriverManager.getConnection("jdbc:mariadb://localhost:3306/gdjdb", "gduser", "1234");
        PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM usercipher");
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            String userid = rs.getString("userid");
            String key = CipherUtil.makehash(userid);
            String emailPlain = CipherUtil.dencrypt(rs.getString("email"),key);
            System.out.println(userid + ":" + emailPlain);
        }

    }
}
