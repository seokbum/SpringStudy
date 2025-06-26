package gradleProject.shop3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;



// Scheduling : 정기적인 작업을 실행하기

@Configuration
@EnableScheduling // scheduler 활성화.
public class BatchConfig {
	
//	@Bean
//	public CountScheduler countSchduler() { //CountScheduler 객체를 생성하여 컨테이너의 등록한 상태
//		return new CountScheduler();
//	}
}
