/**
 * 
 */
package com.vcw.falecpv.web.constante;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ymarin
 *
 */
public enum DiasSemanaEnum {

	LUNES("LUNES"), MARTES("MARTES"), MIERCOLES("MIÉRCOLES"), JUEVES("JUEVES"), VIERNES("VIERNES"), SABADO("SÁBADO"), DOMINGO("DOMINGO");

	private String nombre;

	private DiasSemanaEnum(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public static List<DiasSemanaEnum> getListaDias() {
		DiasSemanaEnum[] tipos = values();
		List<DiasSemanaEnum> lista = new ArrayList<>();
		for (DiasSemanaEnum dias : tipos) {
			lista.add(dias);
		}
		return lista;
	}

}
