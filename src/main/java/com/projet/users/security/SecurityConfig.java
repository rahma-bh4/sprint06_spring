package com.projet.users.security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;



@Configuration
@EnableWebSecurity

public class SecurityConfig {
	
	@Autowired
	AuthenticationManager authMgr;
	
	
	
	  @Bean
	    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	        http.csrf().disable()
	            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
	            .cors(cors -> cors.configurationSource(new CorsConfigurationSource() {
	                @Override
	                public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
	                    CorsConfiguration cors = new CorsConfiguration();
	                    cors.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
	                    cors.setAllowedMethods(Collections.singletonList("*"));
	                    cors.setAllowCredentials(true);
	                    cors.setAllowedHeaders(Collections.singletonList("*"));
	                    cors.setExposedHeaders(Collections.singletonList("Authorization"));
	                    cors.setMaxAge(3600L);
	                    return cors;
	                }
	            }))
	            .authorizeHttpRequests(auth -> auth
	                .requestMatchers("/login", "/register/**", "/verifyEmail/**").permitAll()
	                .requestMatchers("/all").hasAuthority("ADMIN")
	                .anyRequest().authenticated()
	            )
	            .addFilterBefore(new JWTAuthenticationFilter(authMgr), UsernamePasswordAuthenticationFilter.class)
	            .addFilterBefore(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

	        return http.build();
	}
	
	
	

}
