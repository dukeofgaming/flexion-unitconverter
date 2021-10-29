package com.scienceunit.unitconverter.api;

import com.scienceunit.unitconverter.temperature.TemperatureConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RestController
@RequestMapping("/api")
public class TemperatureConverterRestController {

    private static final int PRECISION = 1;

    @GetMapping("/convert")
    public String convert(
        @RequestParam(name = "input_value") double value,
        @RequestParam(name = "student_response") double student_response,
        @RequestParam(name = "input_unit") String input_unit,
        @RequestParam(name = "target_unit") String target_unit
    ){

        try{
            double conversion = new TemperatureConverter(
                    input_unit
            ).convert(
                    value,
                    target_unit
            );

            BigDecimal rounded_conversion = BigDecimal.valueOf(
                conversion
            ).setScale(
                PRECISION,
                RoundingMode.HALF_UP
            );

            System.out.println("Rounded conversion: " + rounded_conversion.toString());

            BigDecimal rounded_student_response = BigDecimal.valueOf(
                student_response
            ).setScale(
                PRECISION,
                RoundingMode.HALF_UP
            );

            System.out.println("Rounded student response: " + rounded_student_response.toString() + "\n");

            return rounded_conversion.equals(rounded_student_response)?("correct"):("incorrect");

        }catch(Exception exception){
            return exception.getMessage();
        }
    }
}
