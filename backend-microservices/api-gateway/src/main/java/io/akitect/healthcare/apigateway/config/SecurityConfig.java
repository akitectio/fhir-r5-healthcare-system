package io.akitect.healthcare.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.config.Customizer;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private String[] getPublicPaths() {
        return new String[]{
                "/actuator/**",
                "/fallback/**",
                "/",
                "/static/**",
                "/login",
                "/oauth2/**"
        };
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(authorize -> authorize
                        .pathMatchers(getPublicPaths()).permitAll()
                        .pathMatchers("/fhir/**").hasAuthority("SCOPE_fhir_admin")
                        .pathMatchers("/api/patients/**").hasAuthority("SCOPE_patient_read")
                        .pathMatchers("/api/encounters/**").hasAuthority("SCOPE_encounter_read")
                        .pathMatchers(HttpMethod.POST, "/api/patients/**").hasAuthority("SCOPE_patient_write")
                        .pathMatchers(HttpMethod.PUT, "/api/patients/**").hasAuthority("SCOPE_patient_write")
                        .pathMatchers(HttpMethod.DELETE, "/api/patients/**").hasAuthority("SCOPE_patient_write")
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(Customizer.withDefaults())
                )
                .build();
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder(
            @Value("${spring.security.oauth2.resource-server.jwt.jwk-set-uri}") String jwkSetUri) {
        return ReactiveJwtDecoders.fromOidcIssuerLocation(jwkSetUri);
    }
}