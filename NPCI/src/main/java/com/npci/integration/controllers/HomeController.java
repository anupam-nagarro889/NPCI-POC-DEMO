package com.npci.integration.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/rest/service/home")
public class HomeController {
	
	@GetMapping("/test/api")
	public String homeView() {
		return "hi this is an test API...";
		
	}

}
