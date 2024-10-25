package co.edu.udea.salasinfo.configuration.security;

import co.edu.udea.salasinfo.configuration.security.filter.JwtAuthenticationFilter;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Generated
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authProvider;
    private final MvcRequestMatcher.Builder mvc;

    @Value("${allowed-origins}")
    private List<String> allowedOrigins;

    @Value("${public-endpoints}")
    private List<String> authWhitelist;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())//NOSONAR not used in secure contexts
                        .requireCsrfProtectionMatcher(request //NOSONAR not used in secure contexts(
                                -> request.getMethod().equalsIgnoreCase("POST") //NOSONAR not used in secure contexts
                                && request.getRequestURI().startsWith("/secure/")) //NOSONAR not used in secure contexts
                )
                .authorizeHttpRequests(auth -> {
                    authWhitelist.forEach(uri ->
                            auth.requestMatchers(mvc.pattern(uri)).permitAll()
                    );
                    auth.anyRequest().authenticated();
                })
                .sessionManagement(sessionManager -> sessionManager
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authProvider)
                .exceptionHandling(handler -> handler.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        org.springframework.web.cors.CorsConfiguration configuration = new org.springframework.web.cors.CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.addAllowedHeader("*");
        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(List.of("POST", "GET", "PUT", "DELETE", "OPTIONS"));
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
