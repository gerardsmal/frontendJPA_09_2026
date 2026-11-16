package com.betacom.fe.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.betacom.fe.dto.input.UtenteReq;
import com.betacom.fe.dto.output.ResponseDTO;
import com.betacom.fe.security.UtenteServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Controller
public class RegisterController {
	private final WebClient webclient;
	private final UtenteServices utS;
	
	
	@GetMapping("/registry")
	public ModelAndView registry(Model model) {
		ModelAndView mav = new ModelAndView("registrazione");
		if (!model.containsAttribute("req")) {
			mav.addObject("req", new UtenteReq());
		}
		return mav;
	}
	
	@PostMapping("/saveNuovoUtente")
	public String saveNuovoUtente(@ModelAttribute UtenteReq req, RedirectAttributes ra) {
		req.setRole("USER");
		
		ResponseEntity<ResponseDTO> response = webclient.post()
				.uri("utente/create")
				.bodyValue(req)
				.exchangeToMono(resp -> resp.toEntity(ResponseDTO.class))
				.block();
		if (!response.getStatusCode().is2xxSuccessful()) {
			ra.addFlashAttribute("errorMsg", response.getBody().getMsg());
		}
		
		utS.updateUtente(req);
		return "redirect:/login";
	}
}
