package com.challenge.gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class ApplicationSecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

        http.csrf().disable()
                .authorizeExchange()
                .pathMatchers(HttpMethod.POST, "/employees/update").hasRole("ADMIN")
                .pathMatchers("/**").permitAll()
                .and()
                .httpBasic();

        /*http.csrf().disable()
                .authorizeExchange()
                .pathMatchers("/", "index", "/css/*", "/js/*").permitAll() // whitelist patterns todo start md page
                .pathMatchers(HttpMethod.POST, "/itineraries/**").hasAuthority(PERMISSION_ITINERARIES_WRITE.getName())
                .pathMatchers(HttpMethod.DELETE, "/itineraries/**").hasAuthority(PERMISSION_ITINERARIES_WRITE.getName())
                .pathMatchers(HttpMethod.PUT, "/itineraries/**").hasAuthority(PERMISSION_ITINERARIES_WRITE.getName())
                .pathMatchers("/**").permitAll() // all requests must be must be authenticated
                .and().httpBasic(); // with basic auth*/

        return http.build();
    }

    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();

        /*UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("1234"))
                .authorities(ROLE_API_CONSUMER.getGrantedAuthorities())
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("1234"))
                .authorities(ROLE_API_ADMIN.getGrantedAuthorities())
                .build();*/

        return new MapReactiveUserDetailsService(user);
        // return new InMemoryUserDetailsManager(user);
    }


}
