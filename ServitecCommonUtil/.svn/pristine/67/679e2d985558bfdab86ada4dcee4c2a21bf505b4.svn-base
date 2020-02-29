/* 
 * Cedula.java
 * Sep 25, 2009 
 * Copyright 2009 Saviasoft Cia. Ltda. 
 */
package com.servitec.common.util;

/**
 * @author saviasoft1
 * 
 */
public class Cedula {

	static final int MULT = 2;
	static final int TOTAL_PROVINCES = 24;

	public static void main(String[] args) {
		String cedula = "2300000391";
		System.out.println(validate(cedula));
	}

	/**
	 * Valida el n?mero de c?dula de Ecuador
	 * 
	 * @param identificationNumber
	 *            Solo d?gitos, sin guiones.
	 * 
	 * @return
	 */
	public static boolean validate(String identificationNumber) {

		boolean valid = false;

		// si no tiene 10 d?gitos es inv?lida
		if (identificationNumber.length() != 10) {
			return valid;
		}

		try {
			Long.parseLong(identificationNumber);
		} catch (NumberFormatException e) {
			return valid;
		}

		String province = identificationNumber.substring(0, 2);

		// si sus dos primeros d?gitos son inv?lidos
		if (Integer.parseInt(province) > TOTAL_PROVINCES) {
			return valid;
		}

		int totalEven = 0; // pares
		int totalOdd = 0; // impares

		// la ?ltima posici?n no cuenta solo es verificador
		int totalValidNumbers = identificationNumber.length() - 1;
		int verifier = Integer.parseInt(identificationNumber.charAt(9) + "");

		for (int i = 0; i < totalValidNumbers; i++) {
			int digit = Integer.parseInt(identificationNumber.charAt(i) + "");
			// System.out.println("digit:"+digit);
			if (i % 2 == 0) {// si son impares
				int product = digit * MULT;
				// System.out.println("product:"+product);
				if (product > 9) {
					product = product - 9;
				}
				totalEven += product;
			} else { // si son pares
				totalOdd += digit;
				// System.out.println(">>>"+totalOdd);
			}
		}

		int total = totalOdd + totalEven;

		String totalString = String.valueOf(total + 10);

		// se verifica la decena superior
		if (totalString.length() > 1) {
			int first = Integer.parseInt(totalString.charAt(0) + "");
			total = Integer.parseInt(first + "0") - total;
			if (total == 10) {
				total = 0;
			}
		}

		int result = total;

		// si el n?mero verificador es igual al resultado del algoritmo
		// entonces es una c?dula v?lida

		if (result == verifier) {
			valid = true;
		}

		return valid;
	}
}