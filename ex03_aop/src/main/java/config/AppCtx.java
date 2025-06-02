package config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration  //spring 환경을 설정하는 클래스
//@ComponentScan : annotation 패키지에 속한 클래스 중 @Component 어노테이션을
//                 가진 클래스의 객체 생성하여 저장. 
@ComponentScan(basePackages={"annotation"})
@EnableAspectJAutoProxy  //AOP설정함. AOP 관련 어노테이션 기능 사용
public class AppCtx {

}