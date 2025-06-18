package kr.gdu.config;

import java.util.Properties;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.zaxxer.hikari.HikariDataSource;

import kr.gdu.Interceptor.BoardInterceptor;


@Configuration //spring 환경설정
public class MvcConfig  implements WebMvcConfigurer{

	//예외처리 객체
	@Bean
	public SimpleMappingExceptionResolver exceptionHandler() {
		SimpleMappingExceptionResolver ser = new SimpleMappingExceptionResolver();
		Properties pr = new Properties();
		//shopException 예외가발생하면 exception.jsp호출
		pr.put("exception.ShopException", "exception");
		
		ser.setExceptionMappings(pr);
		return ser;
	}	
	@Bean
	@Primary //우선적용
	@ConfigurationProperties("spring.datasource")
	public DataSourceProperties dataSourceProperties(){ //Connection 객체		
		return new DataSourceProperties();
	}
	
	@Bean
	@ConfigurationProperties("spring.datasource.hikari")
	public HikariDataSource dataSource(DataSourceProperties properties) {
		return properties.initializeDataSourceBuilder()
				.type(HikariDataSource.class).build(); //Connection Pool 객체
	}
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new BoardInterceptor())
		.addPathPatterns("/board/write")
		.addPathPatterns("/board/update")
		.addPathPatterns("/board/delete"); 
		//해당경로에접근하면 controller접근 전에 
		//BoardInterceptor검증을 통과해야함
		
	}
	
	
}


