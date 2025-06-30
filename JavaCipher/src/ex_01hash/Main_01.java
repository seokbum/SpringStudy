package ex_01hash; 

import java.security.MessageDigest; 
import java.security.NoSuchAlgorithmException; 
import java.security.Security; 
import java.util.Scanner;
import java.util.Set;

public class Main_01 {
	public static void main(String[] args) throws NoSuchAlgorithmException {
		// byte[] : 바이트 배열, 데이터를 바이트 단위로 처리하기 위함
		byte[] plain = null; // 사용자가 입력한 원본 문자열을 바이트 배열로 저장할 변수 (평문)
		byte[] hash = null;  // 해시값을 바이트 배열로 저장할 변수

		// Security.getAlgorithms("MessageDigest") :
		// Java 환경에서 사용 가능한 모든 MessageDigest(해시 함수) 알고리즘 이름들을 가져옵니다.
		Set<String> algorithms = Security.getAlgorithms("MessageDigest");
		System.out.println(algorithms); // 사용 가능한 해시 알고리즘 목록을 출력

		// 미리 정의된 일부 해시 알고리즘 이름들 (예시용 배열, 실제 코드에서는 algorithms Set을 사용)
		String[] algo = {"MD5","SHA-1","SHA-256","SHA-512"}; // 이 변수는 선언만 되어 있고 실제 코드에서는 사용되지 않습니다.

		System.out.println("해쉬값을 구할 문자열을 입력하세요"); // 사용자에게 입력 안내 메시지 출력
		Scanner scan = new Scanner(System.in); // Scanner 객체 생성 (표준 입력에서 읽기)
		String str = scan.nextLine(); // 사용자로부터 한 줄의 문자열을 입력받음
		scan.close(); // Scanner 사용이 끝났으므로 닫아줍니다. (자원 해제)

		plain = str.getBytes(); // 입력받은 문자열을 바이트 배열로 변환. 해시 함수는 바이트 배열을 입력으로 받습니다.

		// algorithms Set에 있는 각 해시 알고리즘에 대해 반복
		for(String al : algo) {
			// MessageDigest.getInstance(al) :
			// 특정 알고리즘(al)의 MessageDigest 객체를 생성합니다.
			// 예를 들어, al이 "SHA-256"이면 SHA-256 해시 함수 객체를 가져옵니다.
			MessageDigest md = MessageDigest.getInstance(al);

			// md.digest(plain) :
			// plain 바이트 배열에 대해 해시 함수(md)를 적용하여 해시값을 계산합니다.
			// 계산된 해시값은 바이트 배열 형태로 반환됩니다.
			hash = md.digest(plain);

			// 해시값의 크기를 비트(bits) 단위로 출력
			// hash.length는 바이트 크기이므로 8을 곱하여 비트로 변환합니다.
			System.out.println(al + " 해쉬값 크기 : "+ (hash.length*8) +"bits" );
			System.out.print("해쉬값: "); // "해쉬값: " 출력

			// 계산된 해시값(바이트 배열)을 16진수 문자열로 출력
			for(byte b : hash) {
				// System.out.printf("%02X", b) :
				// 각 바이트(b)를 16진수(X)로 포맷하여 출력합니다.
				// %02X는 16진수로 출력하되, 항상 두 자리로 맞추고 (예: A -> 0A), 대문자로 출력하라는 의미입니다.
				System.out.printf("%02X",b);
			}
			System.out.println(); // 한 해시값 출력이 끝나면 줄바꿈
		}
	}
}