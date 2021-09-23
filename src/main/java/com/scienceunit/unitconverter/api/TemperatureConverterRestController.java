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

    @GetMapping("/convert")
    public String convert(
        @RequestParam(name = "input_unit") String input_unit,
        @RequestParam(name = "input_value") double value,
        @RequestParam(name = "target_unit") String target_unit,
        @RequestParam(name = "debug_precision", defaultValue = "1", required = false) int precision
    ){

        try{

            return String.valueOf(
                new BigDecimal(
                        new TemperatureConverter(
                        input_unit
                    ).convert(
                        value,
                        target_unit
                    )
                ).setScale(
                    precision,
                    RoundingMode.HALF_UP
                )
            );

        }catch(Exception exception){
            return exception.getMessage();
        }
    }
}
