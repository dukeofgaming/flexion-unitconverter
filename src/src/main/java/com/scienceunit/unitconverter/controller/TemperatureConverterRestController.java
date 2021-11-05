package com.scienceunit.unitconverter.controller;

import com.scienceunit.unitconverter.service.TemperatureConverterService;
import com.scienceunit.unitconverter.temperature.TemperatureConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RestController
@RequestMapping("/api")
public class TemperatureConverterRestController {

    @Autowired
    public TemperatureConverterService service;

    @GetMapping("/grade")
    public String grade(
        @RequestParam(name = "input_value") String value,
        @RequestParam(name = "student_response") String student_response,
        @RequestParam(name = "input_unit") String input_unit,
        @RequestParam(name = "target_unit") String target_unit
    ){
        return service.grade(
            value,
            input_unit,
            target_unit,
            student_response,
            1
        );
    }
}
