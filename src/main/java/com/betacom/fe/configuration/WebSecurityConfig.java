package com.betacom.fe.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((requests) -> requests
				.requestMatchers("/admin", "/admin/**").hasRole("ADMIN")
				.requestMatchers("/login", "/registry", "/saveNuovoUtente").permitAll()
				.anyRequest().authenticated()
				)
				.formLogin((form) -> form
						.loginPage("/login")
						.permitAll()
						)
				.logout((logout) -> logout.permitAll());
		return http.build();
	}
	@Bean
	PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
//	@Bean
//	UserDetailsService userDetailsServices() {
//		
//		List<UserDetails> userDettialsList = new ArrayList<UserDetails>();
//		
//		userDettialsList.add(User.withUsername("user")
//			.password(getPasswordEncoder().encode("user").toString())
//			.roles("USER")
//			.build());
//
//		userDettialsList.add(User.withUsername("admin")
//				.password(getPasswordEncoder().encode("admin").toString())
//				.roles("ADMIN")
//				.build());
//
//		return new InMemoryUserDetailsManager(userDettialsList);
//		
//	}
}
