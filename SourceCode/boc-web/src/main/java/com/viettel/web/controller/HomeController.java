package com.viettel.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class HomeController {
	
	private static final Logger LOGGER = Logger.getLogger(HomeController.class);
	
	@GetMapping("/")
    public ModelAndView welcomePage(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		return new ModelAndView("index");
	}
}