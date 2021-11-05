package com.scienceunit.unitconverter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Scanner;

@SpringBootApplication
@Controller
@RequestMapping(value = {"/", "/home"})
public class UnitconverterApplication {

	public static void main(String[] args) {
		SpringApplication.run(UnitconverterApplication.class, args);
	}

	@GetMapping(value = {"","/"})
	public String home(Model model) throws IOException{

		model.addAttribute(
			"hostname",
			new Scanner(Runtime.getRuntime().exec("hostname").getInputStream()).useDelimiter("\\A").next()
		).addAttribute(
			"version",
			System.getenv("FLEXION_UNITCONVERTER_APP_VERSION")
		);

		return "home";
	}
}
