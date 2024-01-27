package com.ddangme.board.config;

import com.ddangme.board.dto.security.BoardPrincipal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class JpaConfig {

    @Bean
    public AuditorAware<String> auditorAware() {

        return () -> Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication) // 보안 컨텍스트에서 인증(Authentication) 객체를 추출한다.
                .filter(Authentication::isAuthenticated) // 인증 객체가 인증되었는지 확인한다. 즉, 로그인 상태인지 검사한다.
                .map(Authentication::getPrincipal)// 로그인한 사용자의 주체(principal)를 가져온다. 주체는 일반적으로 사용자 정보를 나타내는 객체이다.
                .map(BoardPrincipal.class::cast) // 주체를 BoardPrincipal타입으로 캐스팅한다. 이전 단계에서 주체가 BoardPrincipal의 인스턴스로 설정되어 있다고 가정한 상태
                .map(BoardPrincipal::getUsername); // BoardPrincipal에서 username을 가져온다.
    }

}