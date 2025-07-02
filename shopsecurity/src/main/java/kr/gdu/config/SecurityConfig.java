package kr.gdu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    /*
     * HttpSecurity http : Spring Security의 보안 설정을 담당하는 객체
     *                     HTTP 요청시 인증,권한을 정의가능
     *     authorizeHttpRequests() : 요청 URL에 따라 권한(Authorization) 설정
     *     requestMatchers() : 요청의 종류
     *     permitAll() : 허용
     *     anyRequest() : 그외의 모든 요청
     *     .authenticated(): 인증 필요
     */

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)
            throws Exception {
        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/", "/login", "/home", "/join", "/joinProc").permitAll()
                //ADMIN 권한의 사용자만 접근 가능
                .requestMatchers("/admin").hasRole("ADMIN")
                // /my/로 시작하는 요청시 ADMIN 또는 USER 권한 사용자 접근 가능
                .requestMatchers("/my/*").hasAnyRole("ADMIN", "USER")
                .anyRequest().authenticated());

        //커스텀 로그인
        http.formLogin((auth) -> auth.loginPage("/login") //로그인 요청 페이지 경로
                .loginProcessingUrl("/loginProc") //로그인 form의 action값
                /*
                    true : 무조건 my 페이지 요청
                    false : 로그인 전에 요청하던 페이지가 있는 경우 해당 페이지로 감. 그외는 /my 를 요청
                 */
                .defaultSuccessUrl("/my", true)
                .permitAll()  //로그인 페이지는 누구나 접근 가능
        );

        http.logout(logout -> logout  //로그아웃 설정
                .logoutUrl("/logout")   //요청 URL
                .logoutSuccessUrl("/login") //로그아웃 후 요청되는 페이지
                .invalidateHttpSession(true) //세션 무효화
                .deleteCookies("JSESSIONID") //쿠키 삭제.
                .permitAll());

        http.sessionManagement((auth) -> auth
                .sessionFixation().changeSessionId()    // 로그인시 세션 ID 새로 발급
                .maximumSessions(1) // 아이디별 최대 세션수 1개로 제한. 중복 로그인 방지
                .maxSessionsPreventsLogin(true)); // 새로운 로그인을 제한
		 /*
		 	CSRF(Cross-Site Request Forgery, 사이트간 요청 위조)
		 	사용자가 의도하지 않은 요청을 수행하도록 하는 공격 방식
		 	SpringSecurity는 기본적으로 POST,PUT,DELETE 요청시는 CSRF 토큰을 요구함
		  */

        // 세션기반 인증시에는 활성화 하는것이 안전함.
        http.csrf((auth) -> auth.disable()); // CSRF 인증 해제. 권장하지 않음
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
