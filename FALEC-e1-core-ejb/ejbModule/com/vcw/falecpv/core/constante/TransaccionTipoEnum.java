package com.vcw.falecpv.core.constante;

/**
 * @author cristianvillarreal
 *
 */
public enum TransaccionTipoEnum {
	CAJA_CHICA("1");
	
	private String id;

	private TransaccionTipoEnum(String id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
}
