package gradleProject.shop3.aop;

import gradleProject.shop3.domain.User;
import gradleProject.shop3.exception.ShopException;
import jakarta.servlet.http.HttpSession;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 사용자 로그인 상태 및 권한을 확인하는 AOP(Aspect-Oriented Programming) 클래스입니다.
 * 특정 컨트롤러 메서드 실행 전후에 공통 로직을 적용합니다.
 */
@Component // Spring 빈으로 등록
@Aspect    // AOP Aspect로 선언
public class UserLoginAspect {

	/**
	 * `UserController`의 `idCheck`로 시작하는 메서드들을 대상으로 합니다.
	 * 마이페이지, 정보 수정, 탈퇴 등 본인 확인이 필요한 메서드에 적용됩니다.
	 * `HttpSession`이 마지막 매개변수로 오는 모든 메서드에 적용됩니다.
	 * `userid`는 어드바이스 내부에서 동적으로 찾습니다.
	 *
	 * @param joinPoint 현재 실행 중인 조인 포인트 (메서드) 정보
	 * @param session   HttpSession 객체 (마지막 매개변수)
	 * @return 대상 메서드의 실행 결과
	 * @throws Throwable 예외 발생 시 전파
	 */
	@Around("execution(* gradleProject.shop3.controller.User*.idCheck*(..)) && args(..,session)") // << args 패턴 수정!
	public Object userIdCheck(ProceedingJoinPoint joinPoint, HttpSession session) throws Throwable { // << userid 매개변수 제거

		User loginUser = (User)session.getAttribute("loginUser");
		String requestedUserId = null; // 요청된 userid를 저장할 변수

		Object[] args = joinPoint.getArgs();
		for (Object arg : args) {
			if (arg instanceof String && (joinPoint.getSignature().getName().contains("idCheck") || joinPoint.getSignature().getName().contains("delete"))) {

				if (joinPoint.getSignature().getName().equals("idCheckMypage") ||
						joinPoint.getSignature().getName().equals("idCheckUser") ||
						joinPoint.getSignature().getName().equals("idCheckUpdate")) {
					// 이 메서드들에서는 userid가 첫 번째 String 매개변수입니다.
					if (args.length > 0 && args[0] instanceof String) {
						requestedUserId = (String) args[0];
					} else if (args.length > 0 && args[0] instanceof User) { // idCheckUpdate의 User user 객체
						requestedUserId = ((User) args[0]).getUserid();
					}
				} else if (joinPoint.getSignature().getName().equals("idCheckDelete")) {

					if (args.length > 1 && args[1] instanceof String) {
						requestedUserId = (String) args[1];
					}
				}

				if (requestedUserId != null) {
					break;
				}
			}
		}



		System.out.println("--- AOP: userIdCheck 시작 ---");
		System.out.println("세션의 loginUser: " + (loginUser != null ? loginUser.getUserid() : "null"));
		System.out.println("요청된 userid (AOP 파싱): " + requestedUserId); // 파싱된 userid 출력


		if(loginUser == null || !(loginUser instanceof User)) {
			System.out.println("AOP: 로그인이 필요합니다 예외 발생");
			throw new ShopException("[idCheck]로그인이 필요합니다", "/user/login");
		}

		if (requestedUserId == null) {
			System.out.println("AOP: 요청된 userid를 찾을 수 없습니다. 예외 발생.");
			throw new ShopException("잘못된 접근입니다.", "/item/list");
		}

		if(!loginUser.getUserid().equals("admin") && !loginUser.getUserid().equals(requestedUserId)) { // << requestedUserId 사용
			System.out.println("AOP: 본인 정보만 가능 예외 발생");
			throw new ShopException("[idCheck]본인 정보만 열람 및 수정 가능합니다", "/item/list"); // 상품 목록 페이지로 리다이렉트 URL
		}

		System.out.println("--- AOP: userIdCheck 통과 ---");
		return joinPoint.proceed();
	}

	@Around("execution(* gradleProject.shop3.controller.User*.loginCheck*(..)) && args(..,session)")
	public Object loginCheck(ProceedingJoinPoint joinPoint, HttpSession session) throws Throwable {

		User loginUser = (User) session.getAttribute("loginUser");

		// --- 디버깅 로그 추가 ---
		System.out.println("--- AOP: loginCheck 시작 ---");
		System.out.println("세션의 loginUser: " + (loginUser != null ? loginUser.getUserid() : "null"));


		if (loginUser == null || !(loginUser instanceof User)) {
			System.out.println("AOP: 로그인이 필요합니다 (loginCheck) 예외 발생");
			throw new ShopException("[loginCheck]로그인이 필요합니다.", "/user/login"); // 로그인 페이지로 리다이렉트 URL
		}

		System.out.println("--- AOP: loginCheck 통과 ---");

		return joinPoint.proceed();
	}
}
