package gradleProject.shop3.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * Spring MVC 설정 클래스입니다.
 * 주로 웹 관련 설정(인터셉터, 뷰 리졸버 등)과 AOP 활성화를 담당합니다.
 */
@Configuration
@EnableAspectJAutoProxy
public class MvcConfig implements WebMvcConfigurer {


//  (선택 사항) 인터셉터 관련 설정 - 필요에 따라 주석을 해제하고 구현할 수 있습니다.
//  @Override
//  public void addInterceptors(InterceptorRegistry registry) {
//
//     registry.addInterceptor(new BoardInterceptor())
//     .addPathPatterns("/board/write")
//     .addPathPatterns("/board/update")
//     .addPathPatterns("/board/delete");
//  }

}
