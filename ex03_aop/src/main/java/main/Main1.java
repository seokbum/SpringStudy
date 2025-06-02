package main;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import annotation.Article;
import annotation.Member;
import annotation.MemberService;
import annotation.ReadArticleService;
import annotation.UpdateInfo;
import config.AppCtx;
/*
  1.환경 설정 자바 클래스에서 사용되는 어노테이션
  	@Configuration : 환경 설정 자바 클래스. xml 대체 되는 클래스
  	@ComponentScan : 객체 생성을 위한 패키지 설정.
  	@EnableAspectJAutoProxy : AOP를 사용하도록 설정
  	@Bean : 따로 객체를 생성.
  2.클래스에서 사용되는 어노테이션
  	@Component : 객체화 되는 클래스. 객체 주입이 완료된 상태.
  	@Autowired : 자료형 기준으로 객체 주입. 주입 대상의 객체가 없으면 오류 발생.
  				 객체 주입 필수
  	@Autowired(required=false) : 주입대상의 객체가 없으면 null 로 주입함.
  				 객체 주입 선택
  	@Scope : 일회용 객체로 생성. 사용될때 마다 새로운 객체로 생성함
  3.AOP 관련 어노테이션
   	@Aspect : AOP로 사용될 클래스로 지정
   	@Order(순서) : 순서는 실행전(before)기준. 실행후(after)의 경우 order 의 역순으로 실행됨.
   	
   	@Before : 핵심 기능 수행 이전
   	@AfterReturning : 핵심 기능 정상 실행 수행 이후. 리턴값 조회 가능
   	@AfterThrowing : 핵심 기능 오류 실행 수행 이후 . 예외 값 조회 가능
   	@After : 핵심 기능 수행 이후
   	@Around : 핵심 메서드 실행 이전, 이후 모두 처리
 */
public class Main1 {

	public static void main(String[] args) {
		// ApplicationContext : 컨테이너. 객체들을 저장하고 있는 공간
		ApplicationContext ctx =
				new AnnotationConfigApplicationContext(AppCtx.class);
		// service : ReadArticleServiceImpl 객체를 저장  
		ReadArticleService service = 
				ctx.getBean("readArticleService",ReadArticleService.class);
		
		try {
			// a1 : Article id 값이 1인 객체
			Article a1 = service.getArticleAndReadCnt(1); // 핵심 메서드 
			// a2 : Article id 값이 1인 객체
			Article a2 = service.getArticleAndReadCnt(1);
			System.out.println("[main] a1 == a2 : " + (a1==a2)); // false
			service.getArticleAndReadCnt(0);
		} catch (Exception e) {
			System.out.println("[main]" + e.getMessage());
		}
		System.out.println("\n UpdateTraceAspect 연습");
		MemberService ms = ctx.getBean("memberService",MemberService.class);
		ms.regist(new Member()); // 핵심 기능
		ms.update("hong", new UpdateInfo());
		ms.delete("hong2","test",new UpdateInfo());
	}

}
