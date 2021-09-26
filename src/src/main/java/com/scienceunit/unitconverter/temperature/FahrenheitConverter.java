package com.scienceunit.unitconverter.temperature;

public class FahrenheitConverter implements ConverterStrategy {
    @Override
    public double toCelsius(double value) {
        return (value - 32.0) * ( 5.0 / 9.0);
    }

    @Override
    public double toFahrenheit(double value) {
        return value;
    }

    @Override
    public double toKelvin(double value) {
        return (value - 32.0) * ( 5.0 / 9.0 ) + ( 273.15 );
    }

    @Override
    public double toRankine(double value) {
        return value + 459.67;
    }
}
