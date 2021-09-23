package com.scienceunit.unitconverter.temperature;

public class CelsiusConverter implements ConverterStrategy {

    @Override
    public double toCelsius(double value) {
        return value;
    }

    @Override
    public double toFahrenheit(double value) {
        return (value * (9.0/5.0)) + 32.0;
    }

    @Override
    public double toKelvin(double value) {
        return value + 273.15;
    }

    @Override
    public double toRankine(double value) {
        return (value * (9.0/5.0)) + 491.67;
    }
}
