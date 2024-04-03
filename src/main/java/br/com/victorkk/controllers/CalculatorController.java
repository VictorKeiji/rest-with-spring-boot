package br.com.victorkk.controllers;

import br.com.victorkk.calculator.*;
import br.com.victorkk.exceptions.UnsupportedMathOperationException;
import org.springframework.web.bind.annotation.*;

@RestController
public class CalculatorController {

    private final Calculator calculator = new Calculator();

    @RequestMapping(value="/sum/{numberOne}/{numberTwo}", method=RequestMethod.GET)
    public Double sum(@PathVariable(value = "numberOne") String numberOne,
                      @PathVariable(value = "numberTwo") String numberTwo) throws Exception{

        if(NumberProcessor.notNumeric(numberOne) || NumberProcessor.notNumeric(numberTwo)) {
            throw new UnsupportedMathOperationException("Please set a numeric value!");
        }
        return calculator.sum(NumberProcessor.convertToDouble(numberOne),
                NumberProcessor.convertToDouble(numberTwo));
    }

    @RequestMapping(value="/subtraction/{numberOne}/{numberTwo}", method=RequestMethod.GET)
    public Double subtraction(@PathVariable(value = "numberOne") String numberOne,
                      @PathVariable(value = "numberTwo") String numberTwo) throws Exception{

        if(NumberProcessor.notNumeric(numberOne) || NumberProcessor.notNumeric(numberTwo)) {
            throw new UnsupportedMathOperationException("Please set a numeric value!");
        }
        return calculator.subtraction(NumberProcessor.convertToDouble(numberOne),
                NumberProcessor.convertToDouble(numberTwo));
    }

    @RequestMapping(value="/division/{numberOne}/{numberTwo}", method=RequestMethod.GET)
    public Double division(@PathVariable(value = "numberOne") String numberOne,
                              @PathVariable(value = "numberTwo") String numberTwo) throws Exception{

        if(NumberProcessor.notNumeric(numberOne) || NumberProcessor.notNumeric(numberTwo)) {
            throw new UnsupportedMathOperationException("Please set a numeric value!");
        }
        return calculator.division(NumberProcessor.convertToDouble(numberOne),
                NumberProcessor.convertToDouble(numberTwo));
    }

    @RequestMapping(value="/multiplication/{numberOne}/{numberTwo}", method=RequestMethod.GET)
    public Double multiplication(@PathVariable(value = "numberOne") String numberOne,
                           @PathVariable(value = "numberTwo") String numberTwo) throws Exception{

        if(NumberProcessor.notNumeric(numberOne) || NumberProcessor.notNumeric(numberTwo)) {
            throw new UnsupportedMathOperationException("Please set a numeric value!");
        }
        return calculator.multiplication(NumberProcessor.convertToDouble(numberOne),
                NumberProcessor.convertToDouble(numberTwo));
    }

    @RequestMapping(value="/mean/{numberOne}/{numberTwo}", method=RequestMethod.GET)
    public Double mean(@PathVariable(value = "numberOne") String numberOne,
                                 @PathVariable(value = "numberTwo") String numberTwo) throws Exception{

        if(NumberProcessor.notNumeric(numberOne) || NumberProcessor.notNumeric(numberTwo)) {
            throw new UnsupportedMathOperationException("Please set a numeric value!");
        }
        return calculator.mean(NumberProcessor.convertToDouble(numberOne),
                NumberProcessor.convertToDouble(numberTwo));
    }

    @RequestMapping(value="/sqrt/{number}", method=RequestMethod.GET)
    public Double sqrt(@PathVariable(value = "number") String number) throws Exception{

        if(NumberProcessor.notNumeric(number)) {
            throw new UnsupportedMathOperationException("Please set a numeric value!");
        }
        return calculator.sqrt(NumberProcessor.convertToDouble(number));
    }

}
