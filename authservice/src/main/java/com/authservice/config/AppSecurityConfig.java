package com.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.authservice.service.CustomUserDetailsService;


@Configuration
public class AppSecurityConfig {
	
	
	
	private CustomUserDetailsService customerUserDetailsService;
	private JWTFilter jwtFilter;
	
    public AppSecurityConfig(CustomUserDetailsService customerUserDetailsService,JWTFilter jwtFilter) {
				this.customerUserDetailsService = customerUserDetailsService;	
				this.jwtFilter=jwtFilter;
	}

private String[] openUrl = {
			
			"/api/v1/auth/register",
			"/api/v1/auth/login",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**",
            "/configuration/**",
            "/favicon.ico" // Static files if used
			
	};
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		
		http
	    .csrf(csrf->csrf.disable())
	    .authorizeHttpRequests(
			        req->{
			        	req.requestMatchers(openUrl).permitAll()
			        	.requestMatchers("/api/v1/auth/message").hasAnyRole("ADMIN","USER")
			        	.anyRequest().authenticated();
			        })
	    .authenticationProvider(authProvider())
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
	
	return http.build();	

	}
	
	
	//For Encoding The Password
	
	@Bean
	public PasswordEncoder getEncodedPassword() {
		return new BCryptPasswordEncoder();
	}
	
	//Authentication Mananger
	
	@Bean
	public AuthenticationManager authManager(AuthenticationConfiguration config)throws Exception {
		return config.getAuthenticationManager();
	}
	
	@Bean
	public AuthenticationProvider authProvider() {
		
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		
		authProvider.setUserDetailsService(customerUserDetailsService);
		authProvider.setPasswordEncoder(getEncodedPassword());
		
		return authProvider;
	}
}
