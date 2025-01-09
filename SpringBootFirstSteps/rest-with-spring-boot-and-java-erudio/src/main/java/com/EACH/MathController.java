package com.EACH;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.EACH.exceptions.UnsupportedMathOperationException;
import com.EACH.methods.MathMethods;

@RestController
public class MathController {

	@GetMapping(value = "/sum/{numberOne}/{numberTwo}")
	public Double sum(@PathVariable String numberOne, @PathVariable String numberTwo) throws Exception {
		if (!MathMethods.isNumeric(numberOne) || !MathMethods.isNumeric(numberTwo))
			throw new UnsupportedMathOperationException("Please set a numeric value");
		return MathMethods.convertToDouble(numberOne) + MathMethods.convertToDouble(numberTwo);
	}

	@GetMapping(value = "/subtraction/{numberOne}/{numberTwo}")
	public Double subtraction(@PathVariable String numberOne, @PathVariable String numberTwo) throws Exception {
		if (!MathMethods.isNumeric(numberOne) || !MathMethods.isNumeric(numberTwo))
			throw new UnsupportedMathOperationException("Please set a numeric value");
		return MathMethods.convertToDouble(numberOne) - MathMethods.convertToDouble(numberTwo);
	}

	@GetMapping(value = "/multiplication/{numberOne}/{numberTwo}")
	public Double multiplication(@PathVariable String numberOne, @PathVariable String numberTwo) throws Exception {
		if (!MathMethods.isNumeric(numberOne) || !MathMethods.isNumeric(numberTwo))
			throw new UnsupportedMathOperationException("Please set a numeric value");
		return MathMethods.convertToDouble(numberOne) * MathMethods.convertToDouble(numberTwo);
	}

	@GetMapping(value = "/division/{numberOne}/{numberTwo}")
	public Double division(@PathVariable String numberOne, @PathVariable String numberTwo) throws Exception {
		if (!MathMethods.isNumeric(numberOne) || !MathMethods.isNumeric(numberTwo))
			throw new UnsupportedMathOperationException("Please set a numeric value");
		if (MathMethods.convertToDouble(numberTwo) == 0)
			throw new UnsupportedMathOperationException("Please set the second number with any other besides 0");
		return MathMethods.convertToDouble(numberOne) / MathMethods.convertToDouble(numberTwo);
	}

	@GetMapping(value = "/pow/{numberOne}/{numberTwo}")
	public Double pow(@PathVariable String numberOne, @PathVariable String numberTwo) throws Exception {
		if (!MathMethods.isNumeric(numberOne) || !MathMethods.isNumeric(numberTwo))
			throw new UnsupportedMathOperationException("Please set a numeric value");
		return Math.pow(MathMethods.convertToDouble(numberOne), MathMethods.convertToDouble(numberTwo));
	}

	@GetMapping(value = "/sqrtRoot/{numberOne}")
	public Double sqrtRoot(@PathVariable String numberOne) throws Exception {
		if (!MathMethods.isNumeric(numberOne))
			throw new UnsupportedMathOperationException("Please set a numeric value");
		return Math.sqrt(MathMethods.convertToDouble(numberOne));
	}

	@GetMapping(value = "/mean/{numberOne}/{numberTwo}")
	public Double mean(@PathVariable String numberOne, @PathVariable String numberTwo) throws Exception {
		if (!MathMethods.isNumeric(numberOne) || !MathMethods.isNumeric(numberTwo))
			throw new UnsupportedMathOperationException("Please set a numeric value");
		return (MathMethods.convertToDouble(numberOne) + MathMethods.convertToDouble(numberTwo)) / 2;
	}

}
