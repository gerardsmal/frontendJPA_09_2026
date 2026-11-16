package com.betacom.fe.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

import com.betacom.fe.dto.input.UtenteReq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UtenteServices {
	
	private final PasswordEncoder getPasswordEncoder;
	private final InMemoryUserDetailsManager inMemoryUserDetailsManager;
	
	public void updateUtente(UtenteReq req) {
		if (inMemoryUserDetailsManager.userExists(req.getUserName())) {
			inMemoryUserDetailsManager.deleteUser(req.getUserName());
			log.debug("utente {} deleted", req.getUserName());
		}
		
		inMemoryUserDetailsManager.createUser(User
				.withUsername(req.getUserName())
				.password(getPasswordEncoder.encode(req.getPwd().toString()))
				.roles(req.getRole())
				.build()
				);
		log.debug("utente {} is created", req.getUserName());
	}
	
}
