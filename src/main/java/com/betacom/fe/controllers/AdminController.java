package com.betacom.fe.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {
	private final WebClient webclient;
	
	@GetMapping("listAttivita")
	public ModelAndView listAttivita() {
		ModelAndView mav = new ModelAndView("admin/listAttivita");
		
		
		return mav;
	}
}
