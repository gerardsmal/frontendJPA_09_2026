package com.betacom.fe.controllers;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.betacom.fe.dto.input.AttivitaReq;
import com.betacom.fe.dto.output.AttivitaDTO;
import com.betacom.fe.dto.output.ResponseDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {
	private final WebClient webclient;
	
	@GetMapping("listAttivita")
	public ModelAndView listAttivita(Model model) {
		ModelAndView mav = new ModelAndView("admin/listAttivita");
		List<AttivitaDTO> atti = webclient.get()
				 .uri(uriBuilder -> uriBuilder
					    .path("attivita/list")
					    .build())
					.retrieve()
					.bodyToMono(new ParameterizedTypeReference<List<AttivitaDTO>>() {})
					.block();
		
		
		model.addAttribute("attivita", atti);
		
		return mav;
	}
	
	@PostMapping("saveAttivita")
	public String saveAttivita(AttivitaReq req,  RedirectAttributes ra) {
		log.debug("saveAttivita:{}" , req);
		
		String operation = (req.getId() == null) ? "create" : "update";
		String url = "attivita/" + operation;
		HttpMethod metodo = (req.getId() == null) ? HttpMethod.POST : HttpMethod.PATCH;
		
		ResponseEntity<ResponseDTO> response = webclient.method(metodo)
				.uri(url)
				.bodyValue(req)
				.exchangeToMono(resp -> resp.toEntity(ResponseDTO.class) )
				.block();

		if (!response.getStatusCode().is2xxSuccessful()) {
			 ra.addFlashAttribute("errorMsg", response.getBody().getMsg());
		}
		return "redirect:/admin/listAttivita";		
	}
	
	@GetMapping("removeAttivita")
	public Object removeAttivita(@RequestParam (required = true) Integer id, RedirectAttributes ra) {
		log.debug("removeAttivita : {}" , id);

		ResponseEntity<ResponseDTO> response = webclient.delete()
				.uri("attivita/delete/{id}", id)
				.exchangeToMono(resp -> resp.toEntity(ResponseDTO.class) )
				.block();

		if (!response.getStatusCode().is2xxSuccessful()) {
			 ra.addFlashAttribute("errorMsg", response.getBody().getMsg());
		}

		return "redirect:/admin/listAttivita";
		
	}
	
}
