package com.betacom.fe.controllers;

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

import com.betacom.fe.dto.input.CertificatoReq;
import com.betacom.fe.dto.output.ResponseDTO;
import com.betacom.fe.dto.output.SocioDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Controller
public class CertificatoMedicoController {
	private final WebClient webclient;
	
	@GetMapping("updateCertificatoMedico")
	public ModelAndView updateCertificatoMedico(@RequestParam Integer id, Model model) {
		ModelAndView mav = new ModelAndView("listCertificatoMedico");
		
		if (!model.containsAttribute("certificato")) {
			SocioDTO soc = webclient.get()
					.uri(uriBuilder -> uriBuilder
							.path("socio/getById")
							.queryParam("id",id)
							.build())
					.retrieve()
					.bodyToMono(SocioDTO.class)
					.block();

			CertificatoReq req = null;
			if (soc.getCertificato() == null) {
				req = CertificatoReq.builder()
						.socioID(id)
						.build();
			}  else {
				req = CertificatoReq.builder()
						.socioID(id)
						.dataCerticifato(soc.getCertificato().getDataCerticifato())
						.tipo(soc.getCertificato().getTipo().equals("normale") ? false : true)
						.build();
			}
			
			model.addAttribute("certificato", req);
			
		}
		
		return mav;
	}
	
	@PostMapping("saveCertificato")
	public String saveCertificato(@ModelAttribute("certificato") CertificatoReq req, RedirectAttributes ra) {
		
		ResponseEntity<ResponseDTO> response = webclient.put()
				.uri("certificato/update")
				.bodyValue(req)
				.exchangeToMono(resp -> resp.toEntity(ResponseDTO.class))
				.block();
		
		log.debug("response {}", response.getBody().getMsg());
		
		if (!response.getStatusCode().is2xxSuccessful()) {
			ra.addFlashAttribute("errorMsg", response.getBody().getMsg());
			ra.addFlashAttribute("certificato", req);
			return "redirect:/updateCertificatoMedico?id="+ req.getSocioID();			
		}
		return "redirect:/listSocio";
	}
}
