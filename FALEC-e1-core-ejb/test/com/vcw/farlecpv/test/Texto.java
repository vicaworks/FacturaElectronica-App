package com.vcw.farlecpv.test;

import com.servitec.common.util.TextoUtil;

public class Texto {

	public Texto() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {

		String cadena = "001";
		System.out.println(TextoUtil.leftPadTexto(cadena, 3, "0"));
		
	}

}
