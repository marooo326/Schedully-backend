package schedully.schedully.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import schedully.schedully.auth.filter.JwtAuthenticationFilter;
import schedully.schedully.auth.provider.JwtTokenProvider;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web) -> web.ignoring()
                .requestMatchers(
                        "/",
                        "/swagger-ui.html",
                        "/v3/api-docs",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/docs/**"
                );
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // 비활성화
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(manage-> manage.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Session 사용 안함
                .formLogin(AbstractHttpConfigurer::disable)     // form login 사용 안함
                .httpBasic(AbstractHttpConfigurer::disable)     // http basic 방식 사용 안함
                .authorizeHttpRequests(authorize -> authorize   // lambda 방식
                        .requestMatchers("/schedule/").permitAll()
                        .requestMatchers("/schedule/*/join").permitAll()
                        .requestMatchers("/schedule/*/login").permitAll()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}