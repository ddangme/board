package com.ddangme.board.config;

import com.ddangme.board.dto.UserAccountDto;
import com.ddangme.board.dto.security.BoardPrincipal;
import com.ddangme.board.repository.UserAccountRepository;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // webSecurityCustomizer() 함수에 따로 작성하지 않고 여기에 작성한다.
                        .mvcMatchers(
                                HttpMethod.GET,
                                "/",
                                "/articles",
                                "/articles/search-hashtag"
                        ).permitAll()
                        .anyRequest().authenticated()

                )
                .formLogin()
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .and()
                .build();
    }


    /*
    해당 코드 사용 시 하위 WARN 로그가 출력된다.
    2024-01-27 21:44:17.086  WARN 35795 --- [  restartedMain] o.s.s.c.a.web.builders.WebSecurity
    : You are asking Spring Security to ignore org.springframework.boot.autoconfigure.security.servlet.StaticResourceRequest$StaticResourceRequestMatcher@5db1254c.
    This is not recommended -- please use permitAll via HttpSecurity#authorizeHttpRequests instead.

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // static resource, css, js, etc...
        // return (web) -> web.ignoring().antMatchers("/css"); 해당 코드 대신 하위 코드를 작성하는 것이 더 효율적이다.
        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
    */

    @Bean
    public UserDetailsService userDetailsService(UserAccountRepository userAccountRepository) {
        return username -> userAccountRepository
                .findById(username)
                .map(UserAccountDto::from)
                .map(BoardPrincipal::from)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다 - username: " + username));
    }


    // Spring Security의 인증 기능을 사용할 때에는 반드시 password encoder도 등록을 해주어야 한다.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}