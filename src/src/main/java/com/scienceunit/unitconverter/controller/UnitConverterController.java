package com.scienceunit.unitconverter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Scanner;

@Controller
@RequestMapping(value = {"/", "/home"})
public class UnitConverterController {

    @GetMapping(value = {"","/"})
    public String home(Model model) throws IOException {

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
