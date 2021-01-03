package com.challenge.gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.savedrequest.NoOpServerRequestCache;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

public class ApplicationSecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

        http
                .csrf().disable()
                .requestCache().requestCache(NoOpServerRequestCache.getInstance()).and() // setting stateless mode
                .authorizeExchange()
                    .pathMatchers("/api/itinerary/v2/api-docs/")
                        .permitAll()
                    .and()
                .authorizeExchange()
                    .pathMatchers("/api/search/**")
                        .hasAuthority("SCOPE_resource.read")
                    .anyExchange()
                        .authenticated()
                    .and()
                .oauth2ResourceServer()
                    .jwt();

        return http.build();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.challenge"))
                .build();
    }

}
