package com.betacom.fe.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("test")
public class TestController {

	@GetMapping("/hello/{nome}")
	public ModelAndView hello(@PathVariable String nome) {
		ModelAndView mav = new ModelAndView("test");
		
		mav.addObject("nome", nome);
		return mav;
	}
}
