package com.scienceunit.unitconverter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@Controller
@RequestMapping(value = {"/", "/home"})
public class UnitconverterApplication {

	public static void main(String[] args) {
		SpringApplication.run(UnitconverterApplication.class, args);
	}

	@GetMapping(value = {"","/"})
	public String home() {
		return "home";
	}
}
