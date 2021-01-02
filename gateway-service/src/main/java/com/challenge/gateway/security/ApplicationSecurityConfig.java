package com.challenge.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import static com.challenge.gateway.security.ApplicationUserPermission.*;
import static com.challenge.gateway.security.ApplicationUserRole.ROLE_API_ADMIN;
import static com.challenge.gateway.security.ApplicationUserRole.ROLE_API_CONSUMER;

//@EnableWebFluxSecurity
public class ApplicationSecurityConfig {
/*

    private final PasswordEncoder passwordEncoder;

//    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

//    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                */
/*//*
/ disable csrf for testing
                .csrf().disable()
                // .requestCache().requestCache(NoOpServerRequestCache.getInstance()).and() // setting stateless mode
                .authorizeExchange()
                // open swagger endpoints to smooth swagger-ui load
                .pathMatchers("/api/search/v2/api-docs", "/api/itineraries/v2/api-docs").permitAll()
                // set api permissions
                .pathMatchers(HttpMethod.GET, "/api/search/**").hasAuthority(PERMISSION_SEARCH_READ.getName())
                .pathMatchers(HttpMethod.GET, "/api/itineraries/**").hasAuthority(PERMISSION_ITINERARIES_READ.getName())
                .pathMatchers(HttpMethod.POST, "/api/itineraries/**").hasAuthority(PERMISSION_ITINERARIES_WRITE.getName())
                .pathMatchers(HttpMethod.DELETE, "/api/itineraries/**").hasAuthority(PERMISSION_ITINERARIES_WRITE.getName())
                .pathMatchers(HttpMethod.PUT, "/api/itineraries/**").hasAuthority(PERMISSION_ITINERARIES_WRITE.getName())
                // open rest of urls to audit
                .pathMatchers("/**").permitAll()
                // set basic auth
                .and().httpBasic();*//*

                .csrf().disable().authorizeExchange().pathMatchers("/**").permitAll();
        return http.build();
    }

//    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("1234"))
                .authorities(ROLE_API_CONSUMER.getGrantedAuthorities())
                .build();
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("1234"))
                .authorities(ROLE_API_ADMIN.getGrantedAuthorities())
                .build();
        return new MapReactiveUserDetailsService(user, admin);
    }

//    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.challenge"))
                .build();
    }
*/


}
