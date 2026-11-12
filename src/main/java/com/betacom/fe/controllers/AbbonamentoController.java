package com.betacom.fe.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.betacom.fe.dto.input.AbbonamentoReq;
import com.betacom.fe.dto.output.AttivitaDTO;
import com.betacom.fe.dto.output.ResponseDTO;
import com.betacom.fe.dto.output.SocioDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Controller
public class AbbonamentoController {
	
	private final WebClient webclient;
	
	
	@GetMapping("listAbbonamento")
	public ModelAndView listAbbonamento(@RequestParam Integer id) {
		ModelAndView mav = new ModelAndView("listAbbonamento");
		SocioDTO soc = webclient.get()
				.uri(uriBuilder -> uriBuilder
						.path("socio/getById")
						.queryParam("id",id)
						.build())
				.retrieve()
				.bodyToMono(SocioDTO.class)
				.block();
		
		
		mav.addObject("socio", soc);
		mav.addObject("titolo", "Elenco abbonamento per " + soc.getNome() + " " + soc.getCognome());
		return mav;
	}

	@GetMapping("createAbbonamento")
	public String  createAbbonamento(@RequestParam Integer id) {
		AbbonamentoReq req = new AbbonamentoReq();
		req.setSocioID(id);
		req.setDataAbbonamento(LocalDate.now());
		
		ResponseEntity<ResponseDTO> response = webclient.post()
				.uri("abbonamento/create")
				.bodyValue(req)
				.exchangeToMono(resp -> resp.toEntity(ResponseDTO.class))
				.block();
		
		log.debug("response {}", response.getBody().getMsg());		 
		return "redirect:/listAbbonamento?id=" + id;
		
	}
	
	@GetMapping("removeAbbonamento")
	public String removeAbbonamento(@RequestParam Integer abboID, @RequestParam Integer socioID) {
		
		ResponseDTO response = webclient.delete()
				.uri("abbonamento/delete/{abboID}/{socioID}",abboID,socioID )
				.retrieve()
				.bodyToMono(ResponseDTO.class)
				.block();
		log.debug("response delete {}", response.getMsg());
		return "redirect:/listAbbonamento?id=" + socioID;
		
	}
	
	@GetMapping("aggiungiAttivita")
	public ModelAndView aggiungiAttivita(
			@RequestParam Integer abboID,
			@RequestParam Integer socioID) {
		ModelAndView mav = new ModelAndView("aggiungiAttivita");
		
		List<AttivitaDTO> att = webclient.get()
				.uri(uriBuilder -> uriBuilder
						.path("attivita/list")
						.build())
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<List<AttivitaDTO>>() {})
				.block();
		
		mav.addObject("listAttivita", att);
		mav.addObject("param", AbbonamentoReq.builder()
				.id(abboID)
				.socioID(socioID)
				.build());
		
		
		
		return mav;
	}
	
	@PostMapping("saveAbbonamentoAttivita")
	public String saveAbbonamentoAttivita(@ModelAttribute("param") AbbonamentoReq req, RedirectAttributes ra) {
		
		ResponseEntity<ResponseDTO> response = webclient.post()
				.uri("abbonamento/createAbbonamentoAttivita")
				.bodyValue(req)
				.exchangeToMono(resp -> resp.toEntity(ResponseDTO.class))
				.block();
		
		if (!response.getStatusCode().is2xxSuccessful()) {
			ra.addFlashAttribute("errorMsg", response.getBody().getMsg());
			return "redirect:/aggiungiAttivita?abboID=" + req.getId() + "&socioID=" + req.getSocioID();
			
		}
		return "redirect:/listAbbonamento?id=" + req.getSocioID();
	}
	
	
	
	
	
}
