package com.ssafy.backend.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.backend.global.component.jwt.JwtTokenProvider;
import com.ssafy.backend.global.component.jwt.security.JwtTokenSecurityFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Spring Security의 구성을 정의하는 설정 클래스입니다.
 * 이 클래스는 JWT 인증을 포함한 Spring Security의 여러 보안 관련 설정을 구성합니다.
 * {@link EnableMethodSecurity} 어노테이션은 메소드 단위의 보안 주석을 활성화하여
 * 세밀한 접근 제어를 가능하게 합니다.
 */
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;    // JWT 토큰 생성 및 검증을 담당하는 컴포넌트
    private final ObjectMapper objectMapper;    // JSON 객체 변환을 위한 ObjectMapper

    /**
     * Spring Security의 HTTP 보안 설정을 구성하는 메서드입니다.
     * 이 메서드는 CORS 설정, CSRF 보호 비활성화, HTTP 기본 인증 비활성화,
     * 폼 기반 로그인과 로그아웃 비활성화, JWT 인증 필터 추가 등의 보안 관련 설정을 정의합니다.
     *
     * @param http HttpSecurity 객체를 통해 웹 보안 설정을 구성할 수 있습니다.
     * @return 구성된 SecurityFilterChain 객체를 반환합니다.
     * @throws Exception 보안 설정 중 발생할 수 있는 예외를 처리합니다.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CORS(Cross-Origin Resource Sharing) 설정을 적용합니다.
                .cors(cors ->
                        cors.configurationSource(corsConfigurationSource())
                )
                // HTTP Basic 인증을 비활성화하여, 사용자 이름과 비밀번호를 사용한 인증 방식을 사용하지 않습니다.
                .httpBasic(AbstractHttpConfigurer::disable)
                // 웹 페이지를 <frame> 또는 <iframe> 내에서 렌더링하는 것을 방지하는 X-Frame-Options 헤더를 비활성화합니다.
                .headers(header ->
                        header.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
                )
                // 모든 요청에 대해 접근을 허용합니다.
                // 상세한 접근 제어는 각 API 엔드포인트에 @PreAuthorize 등의 어노테이션을 사용하여 설정할 수 있습니다.
                .authorizeHttpRequests(auth ->
                        auth.anyRequest().permitAll()
                )
                // Spring Security가 제공하는 기본 로그인 페이지와 로그아웃 메커니즘을 비활성화합니다.
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                // JWT 인증을 위한 커스텀 필터를 UsernamePasswordAuthenticationFilter 클래스 실행 전에 추가합니다.
                // 이 필터는 요청 헤더에 포함된 JWT를 검증하여 사용자 인증을 수행합니다.
                .addFilterBefore(jwtSecurityFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    /**
     * 웹 보안을 커스터마이즈하는 WebSecurityCustomizer 빈을 생성합니다.
     * 이 설정을 통해 특정 요청 경로에 대한 보안 검사를 무시할 수 있습니다.
     *
     * @return WebSecurityCustomizer 객체
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().anyRequest();    // 모든 요청에 대해 보안 검사를 무시합니다.
    }

    /**
     * JWT 인증 필터를 생성하는 메소드입니다.
     * 이 필터는 HTTP 요청의 헤더에서 JWT를 추출하고 검증하는 역할을 합니다.
     *
     * @return JwtTokenSecurityFilter 객체
     */
    @Bean
    public JwtTokenSecurityFilter jwtSecurityFilter() {
        return new JwtTokenSecurityFilter(jwtTokenProvider, objectMapper);
    }

    /**
     * CORS 설정을 위한 CorsConfigurationSource 객체를 생성하는 메소드입니다.
     * 이 설정을 통해 서버는 다른 출처에서 온 요청을 안전하게 처리할 수 있습니다.
     *
     * @return CorsConfigurationSource 객체
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = getCorsConfiguration(3600L);
        // CORS 구성을 URL 패턴에 매핑합니다. 이 예에서는 애플리케이션의 모든 경로("/**")에 대해 CORS 구성을 적용합니다.
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * CORS 필터를 스프링 부트 애플리케이션의 필터 체인에 등록합니다.
     * 이를 통해 모든 들어오는 요청에 대해 CORS 정책이 적용되도록 합니다.
     *
     * @return FilterRegistrationBean 객체로, 스프링 부트가 관리하는 필터 체인에 CORS 필터를 등록하기 위해 사용됩니다.
     */
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterRegistrationBean() {
        CorsConfiguration config = getCorsConfiguration(6000L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 애플리케이션의 모든 경로("/**")에 대해 CORS 구성을 적용합니다.
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean<CorsFilter> filterBean = new FilterRegistrationBean<>(
                new CorsFilter(source));
        // 필터 체인에서의 실행 순서를 설정합니다. 숫자가 낮을수록 먼저 실행됩니다.
        filterBean.setOrder(0); // 필터 체인에서의 순서 설정
        return filterBean;
    }

    /**
     * CORS 정책 구성을 위한 CorsConfiguration 객체를 생성하고 구성합니다.
     * 이 메서드는 애플리케이션의 CORS 정책을 중앙에서 관리할 수 있게 해주며,
     * 필요에 따라 다양한 설정을 적용할 수 있는 유연성을 제공합니다.
     *
     * @param maxAge 프리플라이트 요청의 최대 캐시 시간(초). 이 값은 브라우저가 사전 요청의 결과를 캐시하는 시간을 결정합니다.
     * @return 구성된 CorsConfiguration 객체
     */
    private static CorsConfiguration getCorsConfiguration(long maxAge) {
        CorsConfiguration config = new CorsConfiguration();
        // 크레덴셜(인증 정보)을 포함한 요청을 허용합니다. 쿠키나 HTTP 인증 헤더 등의 사용을 가능하게 합니다.
        // 사용자 인증을 필요로 하는 리소스에 대한 접근을 가능하게 합니다.
        config.setAllowCredentials(true);
        // 모든 출처에서 오는 요청을 허용합니다. 구체적인 출처를 지정하는 것이 권장됩니다.
        config.addAllowedOriginPattern("*");
        // 모든 요청 헤더를 허용합니다. 클라이언트가 요청에 다양한 종류의 헤더를 포함시킬 수 있습니다.
        config.addAllowedHeader("*");
        // 모든 HTTP 메서드를 허용합니다. 이를 통해 RESTful API 지원이 강화됩니다.
        config.addAllowedMethod("*");
        // 프리플라이트 요청의 응답을 캐시할 시간(초)을 설정합니다. 이는 네트워크 지연을 줄이고 성능을 향상시킵니다.
        config.setMaxAge(maxAge);
        return config;
    }

    /**
     * 암호화된 비밀번호를 생성하고 검증하는 PasswordEncoder 빈을 생성합니다.
     * 이 빈은 Spring Security에서 제공하는 BCryptPasswordEncoder를 사용합니다.
     *
     * @return PasswordEncoder 객체
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCrypt 알고리즘을 사용한 패스워드 암호화 객체 생성하여 반환합니다.
    }

}
