package co.frameworks.huellas.config;

import co.frameworks.huellas.security.LoginRateLimitFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final LoginRateLimitFilter loginRateLimitFilter;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .headers(headers -> headers
                        .xssProtection(xss -> xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))
                        .contentSecurityPolicy(csp -> csp.policyDirectives(
                                "default-src 'self'; " +
                                        "img-src 'self' data: https://images.unsplash.com https://*.tile.openstreetmap.org; " +
                                        "style-src 'self' 'unsafe-inline' https://fonts.googleapis.com; " +
                                        "font-src 'self' https://fonts.gstatic.com; " +
                                        "script-src 'self' 'unsafe-inline' https://cdn.jsdelivr.net; " +
                                        "frame-src https://www.openstreetmap.org; " +
                                        "connect-src 'self'"
                        ))
                        .frameOptions(frame -> frame.sameOrigin())
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/registro", "/acceso-denegado",
                                "/terminos", "/privacidad", "/como-adoptar",
                                "/css/**", "/js/**", "/img/**", "/favicon.ico",
                                "/mascotas/catalogo", "/mascotas/detalle/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/refugios", "/refugios/")
                        .permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/voluntario/**").hasAnyRole("VOLUNTARIO", "ADMIN")
                        .requestMatchers("/adoptante/**").hasAnyRole("ADOPTANTE", "ADMIN")
                        .requestMatchers("/refugios/nuevo", "/refugios/*/editar",
                                "/mascotas/gestionar/**", "/adopciones/gestionar/**",
                                "/seguimientos/**")
                        .hasAnyRole("VOLUNTARIO", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/refugios", "/refugios/**")
                        .hasAnyRole("VOLUNTARIO", "ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/redirigir", true)
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/acceso-denegado")
                )
                .addFilterBefore(loginRateLimitFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
