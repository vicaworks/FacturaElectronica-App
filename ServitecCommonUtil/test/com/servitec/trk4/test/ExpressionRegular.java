package com.servitec.trk4.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionRegular {

	public ExpressionRegular() {
	}

	public static void main(String[] args) {

//		String input = "1111";
//
//		Pattern p = Pattern.compile("^([0-9]{10})*$");
//		Matcher m = p.matcher(input);
//		if (!m.find()) {
//			System.out.println("NOOOOOOOOOOOOOO");
//		} else {
//			System.out.println("SIIIIIIIIIII");
//		}

		//EXPRESION PARA NUMERO CON DECIMAL .
		String input2 = ".....";
		Pattern p2 = Pattern.compile("^\\d+|^\\d+\\.?\\d+");

		Matcher m2 = p2.matcher(input2);

		if (m2.matches()) {
			System.out.println("Numero correcto");
		} else {
			System.out.println("Número incorrecto");
		}

		System.out.println("Número: " + input2);

	}

}
