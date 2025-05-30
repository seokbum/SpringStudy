package main;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import annotation.Article;
import annotation.ReadArticleService;
import config.AppCtx;

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
			Article a1 = service.getArticleAndReadCnt(1);
			// a2 : Article id 값이 1인 객체
			Article a2 = service.getArticleAndReadCnt(1);
			System.out.println("[main] al == a2 : " + (a1==a2)); // false
			service.getArticleAndReadCnt(0);
		} catch (Exception e) {
			System.out.println("[main]" + e.getMessage());
		}
	}

}
