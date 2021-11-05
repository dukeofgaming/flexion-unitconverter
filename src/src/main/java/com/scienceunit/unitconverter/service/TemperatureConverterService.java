package com.scienceunit.unitconverter.service;

import com.scienceunit.unitconverter.exception.InvalidConversionUnitException;
import com.scienceunit.unitconverter.temperature.TemperatureConverter;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class TemperatureConverterService {


    /**
     * Performs a temperature conversion using the TemperatureConverter class, throws an exception with
     * message "invalid" if none of the
     *
     * @param value
     * @param input_unit
     * @param target_unit
     * @return
     * @throws Exception
     */
    public double convert(double value, String input_unit, String target_unit, int decimals) throws InvalidConversionUnitException {

        return new TemperatureConverter(
            input_unit
        ).setPrecision(
            decimals
        ).convert(
            value,
            target_unit
        );

    }

    /**
     * Takes same arguments than convert, plus a student response and normalizes both answers by rounding to
     * a given number of decimals, then compares them and outputs correct if they are the same after being rounded.
     * 
     * @param value
     * @param input_unit
     * @param target_unit
     * @param student_response
     * @param decimals
     * @return
     */
    public String grade(
        String value,
        String input_unit,
        String target_unit,
        String student_response,
        int decimals
    ) {

        double conversion;

        try{
            conversion = this.convert(
                Double.parseDouble(value),
                input_unit,
                target_unit,
                decimals
            );
        }catch(NumberFormatException exception){
            return "invalid";
        }catch(InvalidConversionUnitException exception){
            return "invalid";
        }

        System.out.println("Conversion: " + conversion);

        double rounded_student_response;
        try{
            rounded_student_response = BigDecimal.valueOf(
                Double.parseDouble(student_response)
            ).setScale(
                decimals,
                RoundingMode.HALF_UP
            ).doubleValue();
        }catch(NumberFormatException exception){
            return "incorrect";
        }

        System.out.println("Rounded authoritative answer: " + conversion);
        System.out.println("Rounded student response: " + rounded_student_response);

        return (conversion == rounded_student_response)?("correct"):("incorrect");
    }
}
