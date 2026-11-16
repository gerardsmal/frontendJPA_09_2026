package com.betacom.fe.security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.betacom.fe.dto.output.UtenteDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomUserDetailsServices {
	private final WebClient webclient;
	
	
	public InMemoryUserDetailsManager loadUser() {
		log.debug("loadUser ....");
		
		
		List<UtenteDTO> ut = webclient.get()
				.uri("utente/list")
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<List<UtenteDTO>>() {})
				.block();
		
		List<UserDetails> userDetailsList = ut.stream()
				.map(usr -> User.withUsername(usr.getUserName())
						.password(usr.getPwd())
						.roles(usr.getRole())
						.build()
						).collect(Collectors.toList());
		return new InMemoryUserDetailsManager(userDetailsList);
	}
}
