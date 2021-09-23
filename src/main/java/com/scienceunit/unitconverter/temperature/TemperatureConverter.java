package com.scienceunit.unitconverter.temperature;

public class TemperatureConverter {
    private ConverterStrategy converter = null;

    public TemperatureConverter(String source_unit) throws Exception {

        this.setConverter(source_unit);

    }

    public TemperatureConverter setConverter(String source_unit) throws Exception {

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
                throw new Exception("invalid");
        }

        return this;
    }

    public double convert(double value, String target_unit) throws Exception {

        switch (target_unit.toLowerCase()){
            case "celsius":
                return this.converter.toCelsius(value);
            case "fahrenheit":
                return this.converter.toFahrenheit(value);
            case "kelvin":
                return this.converter.toKelvin(value);
            case "rankine":
                return this.converter.toRankine(value);
            default:
                throw new Exception("invalid");
        }



    }

}
