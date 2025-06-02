package annotation;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(2)
public class ArticleCacheAspect {
    private Map<Integer, Article> cache = new HashMap<>();
    // pointcut :
    // *.. : 패키에 상관없이
    // ReadArticleService : 클래스명이 ReadArticleService인 경우
    @Around("execution(public * *..ReadArticleService.*(..))")
    public Object cache(ProceedingJoinPoint joinPoint) throws Throwable {
    	// getArgs() : 핵심기능의 매개변수 값들
        Integer id = (Integer)joinPoint.getArgs()[0];
        // joinPoint.getSignature().getName() : 핵심메서드의 메서드 이름
        System.out.println("[ACA] " + joinPoint.getSignature().getName() + " (" + id + ") 메서드 호출 전");
        Article article = cache.get(id);
        if (article != null) {
            System.out.println("[ACA] cache에서 Article [" + id + "] 가져옴");
            return article;
        }
        
        //ret : 핵심 메서드의 리턴값
        Object ret = joinPoint.proceed(); // 다음 메서드 호출.
        System.out.println("[ACA] " + joinPoint.getSignature().getName() + " (" + id + ") 메서드 호출 후");
        if (ret != null && ret instanceof Article) {
            cache.put(id, (Article)ret); // 1, Article(1) 객체 저장
            System.out.println("[ACA] cache에 Article [" + id + "] 추가함");
        }
        return ret;
    }
}