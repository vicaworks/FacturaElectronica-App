/**
 * 
 */
package com.vcw.falecpv.core.constante;

/**
 * @author ymarin
 */
public enum EstadoEnvioEmailEnum {

	NADA("verde", "0"), PROCESO("rojo", "1"), ENVIADO("amarillo", "2"), ERROR("rojo", "3");

	private String palabra;
	private String id;

	/**
	 * @param estado
	 */
	private EstadoEnvioEmailEnum(String palabra, String id) {
		this.palabra = palabra;
		this.id = id;
	}

	public String getPalabra() {
		return palabra;
	}

	public String getId() {
		return id;
	}

	public static EstadoEnvioEmailEnum getById(String id) {
		if (id == null)
			return NADA;
		
		switch (id) {
		case "0":
			return NADA;
		case "1":
			return PROCESO;
		case "2":
			return ENVIADO;
		case "3":
			return ERROR;
		default:
			return NADA;
		}
	}

}
