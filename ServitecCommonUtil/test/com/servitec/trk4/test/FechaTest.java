package com.servitec.trk4.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.servitec.common.util.FechaUtil;

public class FechaTest {

	public FechaTest() {
	}

	public static void main(String[] args) {

		// System.out.println(FechaUtil.formatoFechaddMMMMyyyy(new Date()));
		SimpleDateFormat formateador = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("es","ES"));
		Date fechaDate = new Date();
		String fecha = formateador.format(fechaDate);
		System.out.println(fecha);

	}

}
