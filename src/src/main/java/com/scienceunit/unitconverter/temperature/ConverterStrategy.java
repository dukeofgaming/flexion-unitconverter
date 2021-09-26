package com.scienceunit.unitconverter.temperature;

public interface ConverterStrategy {
    public double toCelsius(double value);
    public double toFahrenheit(double value);
    public double toKelvin(double value);
    public double toRankine(double value);
}
