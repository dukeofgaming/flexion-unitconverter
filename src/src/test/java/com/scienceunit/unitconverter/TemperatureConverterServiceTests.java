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
    @DisplayName("Test bad teacher input throws invalid")
    public void testBadTeacherInput() throws Exception {
        assertEquals(converterService.grade(
            "cowdog",
            "celsius",
            "Kelvin",
            "396.2",
            1
        ),"invalid");

    }

    @Test
    @DisplayName("Test grade rounding up")
    public void testGradeRoundUp() throws Exception {
        assertEquals(converterService.grade(
            "123",
            "celsius",
            "Kelvin",
            "396.2",
            1
        ),"correct");

    }

    @Test
    @DisplayName("Test example scenario 1")
    public void testExampleScenario1() throws Exception {
        assertEquals(converterService.grade(
            "84.2",
            "Fahrenheit",
            "Rankine",
            "543.94",
            1
        ),"correct");

    }

    @Test
    @DisplayName("Test example scenario 2")
    public void testExampleScenario2() throws Exception {
        assertEquals(converterService.grade(
            "317.33",
            "Kelvin",
            "Fahrenheit",
            "111.554",
            1
        ),"incorrect");

    }
    @Test
    @DisplayName("Test example scenario 3")
    public void testExampleScenario3() throws Exception {
        assertEquals(converterService.grade(
            "6.5",
            "Fahrenheit",
            "Rankine",
            "dog",
            1
        ),"incorrect");

    }
    @Test
    @DisplayName("Test example scenario 4")
    public void testExampleScenario4() throws Exception {
        assertEquals(converterService.grade(
            "136.1",
            "dogcow",
            "Celsius",
            "45.32",
            1
        ),"invalid");

    }

}
