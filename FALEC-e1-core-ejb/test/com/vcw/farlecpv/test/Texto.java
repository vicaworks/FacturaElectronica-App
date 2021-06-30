package com.vcw.farlecpv.test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Texto {

	public Texto() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {

//		String cadena = "001";
//		System.out.println(TextoUtil.leftPadTexto(cadena, 3, "0"));
		
		List<String> c = new ArrayList<>();
//		c.add("aaaa");
//		c.add("mmm");
//		c.add("mmm1");
//		c.add("mmm2");
		System.out.println(c.stream().collect(Collectors.joining("','")));
		
	}

}
