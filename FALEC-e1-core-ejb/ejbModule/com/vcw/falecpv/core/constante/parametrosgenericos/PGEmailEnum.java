package com.vcw.falecpv.core.constante.parametrosgenericos;


public enum PGEmailEnum implements ParametroGenericoBaseEnum{

//	Datos de SMTP global
	SMTP_SERVER("30"), 
	SMTP_PORT("31"),
	SMTP_USER("32"),
	SMTP_PASSWORD("33"),
	SMTP_AUTH("34"),
	SMTP_SSL("35"),
	SMTP_START_TLS("36"),
// Si está habilitado en empresa	
	SERVER_SMTP_PROPIO("10"),
	SERVER_SMTP_TEST("139"),
	SERVER_SMTP_TEST_EMAIL("140"),
	
	SERVER_SMTP_SERVER("13"), 
	SERVER_SMTP_PORT("14"),
	SERVER_SMTP_USER("15"),
	SERVER_SMTP_PASSWORD("16"),
	SERVER_SMTP_AUTH("17"),
	SERVER_SMTP_SSL("18"),
	SERVER_SMTP_START_TLS("19");

	private String id;

	private PGEmailEnum(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

}
