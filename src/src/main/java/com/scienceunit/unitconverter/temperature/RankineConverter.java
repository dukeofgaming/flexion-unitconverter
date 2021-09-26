package com.scienceunit.unitconverter.temperature;

public class RankineConverter implements ConverterStrategy {
    @Override
    public double toCelsius(double value) {
        return (value - 491.67) * (5.0 / 9.0);
    }

    @Override
    public double toFahrenheit(double value) {
        return value - 459.67;
    }

    @Override
    public double toKelvin(double value) {
        return value * (5.0 / 9.0);
    }

    @Override
    public double toRankine(double value) {
        return value;
    }
}
