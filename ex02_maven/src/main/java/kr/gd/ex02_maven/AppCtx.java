package kr.gd.ex02_maven;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration //Spring 환경을 설정 기능의 프로 그램
@ComponentScan(basePackages = {"kr.gd.ex02_maven"})
public class AppCtx {

}
