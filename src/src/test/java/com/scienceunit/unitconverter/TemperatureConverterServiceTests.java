package com.scienceunit.unitconverter;

import com.scienceunit.unitconverter.service.TemperatureConverterService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TemperatureConverterServiceTests {

    @Autowired
    private TemperatureConverterService converterService;

    @Test
    @DisplayName("Test Celsius to Kelvin conversion")
    public void testConvertCelsiusToKelvin() throws Exception {
        assertEquals(converterService.convert(
            123,
            "celsius",
            "Kelvin"
        ),396.15);
    }

    @Test
    @DisplayName("Test ")
    public void testGradeRoundUp() throws Exception {
        assertEquals(converterService.grade(
            123,
            "celsius",
            "Kelvin",
            396.2,
            1
        ),"correct");

    }

}
