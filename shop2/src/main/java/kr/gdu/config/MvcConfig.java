package kr.gdu.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry; // ★★★ import 수정
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import com.zaxxer.hikari.HikariDataSource;


import kr.gdu.Interceptor.BoardInterceptor;


@Configuration
@EnableAspectJAutoProxy //AOP 사용을 위한 설정
public class MvcConfig implements WebMvcConfigurer {
	// 예외처리 및 DataSource 관련 Bean 설정은 그대로 유지
	@Bean
	public SimpleMappingExceptionResolver exceptionHandler() {
		SimpleMappingExceptionResolver ser = new SimpleMappingExceptionResolver();
		Properties pr = new Properties();
		pr.put("kr.gdu.exception.ShopException", "exception");
		ser.setExceptionMappings(pr);
		return ser;
	}
	
	@Bean
	@Primary
	@ConfigurationProperties("spring.datasource")
	public DataSourceProperties dataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean
	@ConfigurationProperties("spring.datasource.hikari")
	public HikariDataSource dataSource(DataSourceProperties properties) {
		return properties.initializeDataSourceBuilder()
				.type(HikariDataSource.class).build();
	}
	
	// 인터셉터 관련 설정
	@Override
	public void addInterceptors(InterceptorRegistry registry) { 
		registry.addInterceptor(new BoardInterceptor())
				.addPathPatterns("/board/write") // 요청 url 정보
				.addPathPatterns("/board/update")
				.addPathPatterns("/board/delete");
	}
}