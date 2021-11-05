package com.scienceunit.unitconverter.temperature;

import com.scienceunit.unitconverter.exception.InvalidConversionUnitException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TemperatureConverter {
    private ConverterStrategy converter = null;
    private int precision_decimals      = 3;
    private RoundingMode rounding_mode  = RoundingMode.HALF_UP;

    public TemperatureConverter() {}

    public TemperatureConverter(String source_unit) throws InvalidConversionUnitException {
        this.setConverter(source_unit);
    }

    public TemperatureConverter setConverter(String source_unit) throws InvalidConversionUnitException {

        switch(source_unit.toLowerCase()){
            case "celsius":
                this.converter = new CelsiusConverter();
                break;

            case "fahrenheit":
                this.converter = new FahrenheitConverter();
                break;

            case "kelvin":
                this.converter = new KelvinConverter();
                break;

            case "rankine":
                this.converter = new RankineConverter();
                break;

            default:
                throw new InvalidConversionUnitException("Invalid source unit: " + source_unit.toLowerCase());
        }

        return this;
    }

    public TemperatureConverter setPrecision(int decimals){
        this.precision_decimals = decimals;
        return this;
    }

    public TemperatureConverter setRoundingMode(RoundingMode rounding_mode){
        this.rounding_mode = rounding_mode;
        return this;
    }

    public double convert(double value, String target_unit) throws InvalidConversionUnitException {

        double conversion;

        switch (target_unit.toLowerCase()){
            case "celsius":
                conversion = this.converter.toCelsius(value);
                break;
            case "fahrenheit":
                conversion = this.converter.toFahrenheit(value);
                break;
            case "kelvin":
                conversion = this.converter.toKelvin(value);
                break;
            case "rankine":
                conversion = this.converter.toRankine(value);
                break;
            default:
                throw new InvalidConversionUnitException(
                    "Invalid target unit: " + target_unit.toLowerCase()
                );
        }

        return BigDecimal.valueOf(
            conversion
        ).setScale(
            this.precision_decimals,
            this.rounding_mode
        ).doubleValue();

    }

}
