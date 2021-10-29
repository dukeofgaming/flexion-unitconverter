package com.scienceunit.unitconverter.service;

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
    public double convert(double value, String input_unit, String target_unit) throws Exception {

        return new TemperatureConverter(
            input_unit
        ).convert(
            value,
            target_unit
        );

    }

    /**
     * Takes two doubles and normalizes both by rounding to a given number of decimals, then compares them
     * and outputs correct if they are the same after being rounded
     *
     * @param authoritative_answer
     * @param student_response
     * @param decimals
     * @return
     */
    public String grade(double authoritative_answer, double student_response, int decimals){
        BigDecimal rounded_authoritative_answer = BigDecimal.valueOf(
            authoritative_answer
        ).setScale(
            decimals,
            RoundingMode.HALF_UP
        );

        BigDecimal rounded_student_response = BigDecimal.valueOf(
            student_response
        ).setScale(
            decimals,
            RoundingMode.HALF_UP
        );

        System.out.println("Rounded authoritative answer: " + rounded_authoritative_answer.toString());
        System.out.println("Rounded student response: " + rounded_student_response.toString());

        return rounded_authoritative_answer.equals(
            rounded_student_response
        )?("correct"):("incorrect");
    }
}
