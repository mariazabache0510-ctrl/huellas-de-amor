package co.frameworks.huellas.config;

import co.frameworks.huellas.security.LoginRateLimitFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final LoginRateLimitFilter loginRateLimitFilter;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        HttpSessionRequestCache requestCache = new HttpSessionRequestCache() {
            @Override
            public void saveRequest(HttpServletRequest request, HttpServletResponse response) {
                String uri = request.getRequestURI();
                if (uri != null && (uri.equals("/login") || uri.startsWith("/login;") || uri.startsWith("/error"))) {
                    return;
                }
                super.saveRequest(request, response);
            }
        };

        var paths = PathPatternRequestMatcher.withDefaults();

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
                        .requestMatchers(
                                paths.matcher("/"),
                                paths.matcher("/login"),
                                paths.matcher("/login/**"),
                                paths.matcher("/registro"),
                                paths.matcher("/registro/**"),
                                paths.matcher("/acceso-denegado"),
                                paths.matcher("/error"),
                                paths.matcher("/terminos"),
                                paths.matcher("/privacidad"),
                                paths.matcher("/como-adoptar"),
                                paths.matcher("/css/**"),
                                paths.matcher("/js/**"),
                                paths.matcher("/img/**"),
                                paths.matcher("/favicon.ico"),
                                paths.matcher("/mascotas/catalogo"),
                                paths.matcher("/mascotas/detalle/**")
                        ).permitAll()
                        .requestMatchers(paths.matcher(HttpMethod.GET, "/refugios")).permitAll()
                        .requestMatchers(paths.matcher(HttpMethod.GET, "/refugios/")).permitAll()
                        .requestMatchers(paths.matcher("/admin/**")).hasRole("ADMIN")
                        .requestMatchers(paths.matcher("/voluntario/**")).hasAnyRole("VOLUNTARIO", "ADMIN")
                        .requestMatchers(paths.matcher("/adoptante/**")).hasAnyRole("ADOPTANTE", "ADMIN")
                        .requestMatchers(
                                paths.matcher("/refugios/nuevo"),
                                paths.matcher("/refugios/*/editar"),
                                paths.matcher("/mascotas/gestionar/**"),
                                paths.matcher("/adopciones/gestionar/**"),
                                paths.matcher("/seguimientos/**")
                        ).hasAnyRole("VOLUNTARIO", "ADMIN")
                        .requestMatchers(paths.matcher(HttpMethod.POST, "/refugios")).hasAnyRole("VOLUNTARIO", "ADMIN")
                        .requestMatchers(paths.matcher(HttpMethod.POST, "/refugios/**")).hasAnyRole("VOLUNTARIO", "ADMIN")
                        .anyRequest().authenticated()
                )
                .requestCache(cache -> cache.requestCache(requestCache))
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
