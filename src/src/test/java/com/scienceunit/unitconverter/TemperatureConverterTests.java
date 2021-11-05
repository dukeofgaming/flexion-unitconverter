package com.scienceunit.unitconverter;

import com.scienceunit.unitconverter.exception.InvalidConversionUnitException;
import com.scienceunit.unitconverter.temperature.TemperatureConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TemperatureConverterTests {
    ////////////////////////////
    //Source & Target Unit Tests
    ////////////////////////////

    @Test
    @DisplayName("Test invalid source unit to Fahrenheit conversion")
    public void testConvertInvalidToFahrenheit(){
        try{

            assertEquals(
                new TemperatureConverter(
                    "invalid source"
                ).convert(
                    100.001,
                    "FAHRENHEIT"
                ),
                212.0018
            );

        }catch(InvalidConversionUnitException exception){
            assertEquals(
                exception.getClass().toString(),
                "class com.scienceunit.unitconverter.exception.InvalidConversionUnitException"
            );
        }
    }

    @Test
    @DisplayName("Test Celsius to invalit target unit conversion")
    public void testConvertCelsiusToInvalid(){
        try{

            assertEquals(
                new TemperatureConverter(
                    "celsius"
                ).convert(
                    100.001,
                    "invalid target"
                ),
                212.0018
            );

        }catch(InvalidConversionUnitException exception){
            assertEquals(
                exception.getClass().toString(),
                "class com.scienceunit.unitconverter.exception.InvalidConversionUnitException"
            );
        }
    }

    ///////////////////
    //Kelvin Unit Tests
    ///////////////////

    @Test
    @DisplayName("Test Kelvin to Kelvin conversion")
    public void testConvertKelvinToKelvin() throws Exception{
        assertEquals(
            new TemperatureConverter(
                "KELVIN"
            ).convert(
                100.001,
                "kelvin"
            ),
            100.001
        );
    }

    @Test
    @DisplayName("Test Kelvin to Celsius conversion")
    public void testConvertKelvinToCelsius() throws Exception{
        assertEquals(
            new TemperatureConverter(
                "KELVIN"
            ).convert(
                100.001,
                "celsius"
            ),
            -173.149
        );
    }

    @Test
    @DisplayName("Test Kelvin to Fahrenheit conversion")
    public void testConvertKelvinToFahrenheit() throws Exception{
        assertEquals(
            new TemperatureConverter(
                "KELVIN"
            ).setPrecision(4).convert(
                100.001,
                "FAHRENHEIT"
            ),
            -279.6682
        );
    }

    @Test
    @DisplayName("Test Kelvin to Rankine conversion")
    public void testConvertKelvinToRankine() throws Exception{
        assertEquals(
            new TemperatureConverter(
                "KELVIN"
            ).setPrecision(4).convert(
                100.001,
                "RaNkInE"
            ),
            180.0018
        );
    }

    ////////////////////
    //Celsius Unit Tests
    ////////////////////

    @Test
    @DisplayName("Test Celsius to Kelvin conversion")
    public void testConvertCelsiusToKelvin() throws Exception{
        assertEquals(
            new TemperatureConverter(
                "celsius"
            ).convert(
                100.001,
                "kelvin"
            ),
            373.151
        );
    }

    @Test
    @DisplayName("Test Celsius to Celsius conversion")
    public void testConvertCelsiusToCelsius() throws Exception{
        assertEquals(
            new TemperatureConverter(
                "celsius"
            ).convert(
                100.001,
                "celsius"
            ),
            100.001
        );
    }

    @Test
    @DisplayName("Test Celsius to Fahrenheit conversion")
    public void testConvertCelsiusToFahrenheit() throws Exception{
        assertEquals(
            new TemperatureConverter(
                "celsius"
            ).setPrecision(4).convert(
                100.001,
                "FAHRENHEIT"
            ),
            212.0018
        );
    }

    @Test
    @DisplayName("Test Celsius to Rankine conversion")
    public void testConvertCelsiusToRankine() throws Exception{
        assertEquals(
            new TemperatureConverter(
                "CELSIUS"
            ).setPrecision(4).convert(
                100.001,
                "RaNkInE"
            ),
            671.6718
        );
    }


    ///////////////////////
    //Fahrenheit Unit Tests
    ///////////////////////

    @Test
    @DisplayName("Test Fahrenheit to Kelvin conversion")
    public void testConvertFahrenheitToKelvin() throws Exception{
        assertEquals(
            new TemperatureConverter(
                "fahRENheit"
            ).setPrecision(7).convert(
                100.001,
                "kelvin"
            ),
            310.9283333
        );
    }

    @Test
    @DisplayName("Test Fahrenheit to Celsius conversion")
    public void testConvertFahrenheitToCelsius() throws Exception{
        assertEquals(
            new TemperatureConverter(
                "fahRENheit"
            ).setPrecision(8).convert(
                100.001,
                "celsius"
            ),
            37.77833333
        );
    }

    @Test
    @DisplayName("Test Fahrenheit to Fahrenheit conversion")
    public void testConvertFahrenheitToFahrenheit() throws Exception{
        assertEquals(
            new TemperatureConverter(
                "fahRENheit"
            ).setPrecision(4).convert(
                100.001,
                "FAHRENHEIT"
            ),
            100.001
        );
    }


    @Test
    @DisplayName("Test Fahrenheit to Rankine conversion")
    public void testConvertFahrenheitToRankine() throws Exception{
        assertEquals(
            new TemperatureConverter(
                "fahRENheit"
            ).convert(
                100.001,
                "RaNkInE"
            ),
            559.671
        );
    }

    ///////////////////////
    //Rankine Unit Tests
    ///////////////////////

    @Test
    @DisplayName("Test Rankine to Kelvin conversion")
    public void testConvertRankineToKelvin() throws Exception{
        assertEquals(
            new TemperatureConverter(
                "rAnKiNe"
            ).setPrecision(8).convert(
                100.001,
                "kelVIn"
            ),
            55.55611111
        );
    }

    @Test
    @DisplayName("Test Rankine to Celsius conversion")
    public void testConvertRankineToCelsius() throws Exception{
        assertEquals(
            new TemperatureConverter(
                "rAnKiNe"
            ).setPrecision(8).convert(
                100.001,
                "ceLSius"
            ),
            -217.59388889
        );
    }

    @Test
    @DisplayName("Test Rankine to Fahrenheit conversion")
    public void testConvertRankineToFahrenheit() throws Exception{
        assertEquals(
            new TemperatureConverter(
                "rAnKiNe"
            ).setPrecision(3).convert(
                100.001,
                "FAHRENHEIT"
            ),
            -359.669
        );
    }


    @Test
    @DisplayName("Test Rankine to Rankine conversion")
    public void testConvertRankineToRankine() throws Exception{
        assertEquals(
            new TemperatureConverter(
                "rAnKiNe"
            ).convert(
                100.001,
                "RaNkInE"
            ),
            100.001
        );
    }

}
