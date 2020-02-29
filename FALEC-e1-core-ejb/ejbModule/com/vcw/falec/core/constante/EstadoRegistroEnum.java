/**
 * 
 */
package com.vcw.falec.core.constante;

/**
 * @author Administrador
 *
 */
public enum EstadoRegistroEnum {

	ACTIVO("A", "Activo", 1,"ACT"), INACTIVO("I", "Inactivo", 0,"DES"), TODOS("T", "Todos", 2,"Todos");

	private String inicial;
	private String palabra;
	private Integer estadoInteger;
	private String mediaPalabra;

	/**
	 * @param estado
	 */
	private EstadoRegistroEnum(String estado, String estado2, Integer estadoInteger,String mediaPalabra) {
		this.inicial = estado;
		this.palabra = estado2;
		this.estadoInteger = estadoInteger;
		this.mediaPalabra=mediaPalabra;
	}

	public String getInicial() {
		return inicial;
	}

	public String getPalabra() {
		return palabra;
	}

	public Integer getEstadoInteger() {
		return estadoInteger;
	}

	public String getMediaPalabra() {
		return mediaPalabra;
	}

}
