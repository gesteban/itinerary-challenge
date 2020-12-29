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

import static com.challenge.gateway.security.ApplicationUserPermission.*;
import static com.challenge.gateway.security.ApplicationUserRole.ROLE_API_ADMIN;
import static com.challenge.gateway.security.ApplicationUserRole.ROLE_API_CONSUMER;

@EnableWebFluxSecurity
public class ApplicationSecurityConfig {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

        http.csrf().disable()
                .authorizeExchange()
                .pathMatchers(HttpMethod.GET, "/api/search").hasAuthority(PERMISSION_SEARCH_READ.getName())
                .pathMatchers(HttpMethod.GET, "/api/itineraries").hasAuthority(PERMISSION_ITINERARIES_READ.getName())
                .pathMatchers(HttpMethod.POST, "/api/itineraries").hasAuthority(PERMISSION_ITINERARIES_WRITE.getName())
                .pathMatchers(HttpMethod.DELETE, "/api/itineraries").hasAuthority(PERMISSION_ITINERARIES_WRITE.getName())
                .pathMatchers(HttpMethod.PUT, "/api/itineraries").hasAuthority(PERMISSION_ITINERARIES_WRITE.getName())
                .pathMatchers("/**").permitAll()
                .and().httpBasic();

        return http.build();
    }

    @Bean
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


}
