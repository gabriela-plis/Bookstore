package app.backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final RestAuthenticationSuccessHandler successHandler;
    private final ObjectMapper objectMapper;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
            .userDetailsService(userDetailsService)
            .and()
            .build();
    }


    // 5
    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return String.valueOf(rawPassword);
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return encodedPassword.equals(encode(rawPassword));
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {

        // 1
        http.csrf().disable()
            .cors().disable();

        http.authorizeHttpRequests()
            .requestMatchers(HttpMethod.PUT, "/users/*").hasAnyRole("CUSTOMER", "EMPLOYEE", "ADMIN")
            .requestMatchers("/books/*/borrow").hasAnyRole("CUSTOMER", "EMPLOYEE")
            .requestMatchers("/books/*/return").hasAnyRole("CUSTOMER", "EMPLOYEE")
            .requestMatchers("/books/user/*").hasAnyRole("CUSTOMER", "EMPLOYEE")
            .requestMatchers("/books/to-remove").hasRole("EMPLOYEE")
            .requestMatchers(HttpMethod.POST, "/books").hasRole("EMPLOYEE")
            .requestMatchers(HttpMethod.PUT, "/books").hasRole("EMPLOYEE")
            .requestMatchers(HttpMethod.DELETE, "/books/*").hasRole("EMPLOYEE")
            .anyRequest().permitAll()
            .and()
            .addFilterBefore(authenticationFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling()
            .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));

        // 2
        http.formLogin(withDefaults());

        return http.build();
    }

    private JsonObjectAuthenticationFilter authenticationFilter(AuthenticationManager authenticationManager) {
        JsonObjectAuthenticationFilter authenticationFilter = new JsonObjectAuthenticationFilter(objectMapper);
        authenticationFilter.setAuthenticationSuccessHandler(successHandler);
        authenticationFilter.setAuthenticationManager(authenticationManager);
        return authenticationFilter;
    }
}
