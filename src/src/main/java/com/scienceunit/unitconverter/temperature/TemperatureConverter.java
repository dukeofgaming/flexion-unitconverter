package com.scienceunit.unitconverter.temperature;

import com.scienceunit.unitconverter.exception.InvalidConversionUnitException;

import java.lang.reflect.InvocationTargetException;
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
        try{

            this.converter = (ConverterStrategy) Class.forName(
                "com.scienceunit.unitconverter.temperature."
                + source_unit.substring(0,1).toUpperCase()
                + source_unit.substring(1).toLowerCase()
                + "Converter"
            ).getConstructor().newInstance();

        }catch (
            ClassNotFoundException
            | NoSuchMethodException
            | InvocationTargetException
            | InstantiationException
            | IllegalAccessException

            converter_not_found_exception
        ){
            System.out.println(converter_not_found_exception.getMessage());
            converter_not_found_exception.printStackTrace();
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

        try {

            conversion = (double) this.converter.getClass().getMethod(
                "to"
                + target_unit.substring(0,1).toUpperCase()
                + target_unit.substring(1).toLowerCase(),
                new Class[]{Double.TYPE}
            ).invoke(
                this.converter,
                new Object[]{value}
            );

        } catch (
            IllegalAccessException
            | InvocationTargetException
            | NoSuchMethodException

            method_not_found_exception
        ) {
            System.out.println(method_not_found_exception.getMessage());
            method_not_found_exception.printStackTrace();
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
