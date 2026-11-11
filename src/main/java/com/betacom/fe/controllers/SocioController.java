package com.betacom.fe.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.betacom.fe.dto.input.SocioReq;
import com.betacom.fe.dto.output.AttivitaDTO;
import com.betacom.fe.dto.output.ResponseDTO;
import com.betacom.fe.dto.output.SocioDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Controller
public class SocioController {

	private final WebClient webclient;
	
	
	@GetMapping("/listSocio")
	public ModelAndView listSocio(
			@RequestParam(required = false) Integer id,
			@RequestParam(required = false) String nome,
			@RequestParam(required = false) String cognome,
			@RequestParam(required = false) Integer attivita			
			) {
		ModelAndView mav = new ModelAndView("listSocio");
		
		log.debug("listSocio {}/{}/{}", nome, cognome, attivita);
		
		List<SocioDTO> soci = webclient.get()
				.uri(uriBuilder -> uriBuilder
						.path("socio/list")
						.queryParamIfPresent("id", Optional.ofNullable(id))
						.queryParamIfPresent("nome", Optional.ofNullable(nome))
						.queryParamIfPresent("cognome", Optional.ofNullable(cognome))
						.queryParamIfPresent("attivita", Optional.ofNullable(attivita))
						.build())
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<List<SocioDTO>>() {})
				.block();
				
		log.debug("dopo webclient size:{}", soci.size());
		
		List<AttivitaDTO> att = webclient.get()
				.uri(uriBuilder -> uriBuilder
						.path("attivita/list")
						.build())
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<List<AttivitaDTO>>() {})
				.block();
		
		mav.addObject("listsocio", soci);
		mav.addObject("listAttivita", att);
		mav.addObject("param", new SocioReq());
		
		return mav;
	}
	
	@GetMapping("createSocio")
	public ModelAndView createSocio(Model model) {
		ModelAndView mav = new ModelAndView("createSocio");
		
		if (!model.containsAttribute("socio")) {
			mav.addObject("socio", new SocioReq());			
		}
		
		
		return mav;
	}

	@PostMapping("saveSocio")
	public String saveSocio(@ModelAttribute("socio") SocioReq req, RedirectAttributes ra) {
		
		ResponseEntity<ResponseDTO> response = webclient.post()
				.uri("socio/create")
				.bodyValue(req)
				.exchangeToMono(resp -> resp.toEntity(ResponseDTO.class))
				.block();
		
		if (!response.getStatusCode().is2xxSuccessful()) {
			log.debug("error found in create..");
			log.debug("msg: {}", response.getBody().getMsg());
			ra.addFlashAttribute("errorMsg", response.getBody().getMsg());
			ra.addFlashAttribute("socio", req);

			return "redirect:/createSocio";
		}
		
		return "redirect:/listSocio";
		
	}
	
	
}
