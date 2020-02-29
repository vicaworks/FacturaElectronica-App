/**
 * 
 */
package com.servitec.common.util;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author cvillarreal
 *
 */
public class UtilMd5 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4637836578336637856L;

	/**
	 * 
	 */
	public UtilMd5() {
	}

	/**
	 * Transforma una cadena a Md5 (Hexadecimal)
	 * 
	 * @param cadena - cadena a ser transformada a md5
	 * @return un string con el valor md5 de la cadena
	 */
	public static String hash(String cadena) {
		if (cadena == null) {
			return null;
		}

		try {
			// Crear un objeto para acceder al algoritmo:
			MessageDigest md = MessageDigest.getInstance("MD5");

			// Crear el hash:
			md.update(cadena.getBytes());

			// Retornar el hash en forma hexadecimal:
			return toHex(md.digest());

		} catch (NoSuchAlgorithmException e) {
			System.out
					.println("Fallo al cargar el algoritmo MD5 de MessageDigest.");
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * Convierte un arreglo de bytes en una cadena representando cada byte como
	 * un numero hexadecimal sin signo.
	 * <p>
	 * Methodo desarrollado por Santeri Paavolainen, Helsinki Finland 1996<br>
	 * (c) Santeri Paavolainen, Helsinki Finland 1996<br>
	 * Distribuido bajo licencia LGPL.
	 * 
	 * @param hash
	 *            un arreglo de bytes a ser convertido
	 * @return la cadena hexadecimal que equivale al arreglo de bytes.
	 */
	private static final String toHex(byte[] bytes) {
		StringBuffer buf = new StringBuffer(bytes.length * 2);

		for (int i = 0; i < bytes.length; i++) {
			if (((int) bytes[i] & 0xff) < 0x10) {
				buf.append("0");
			}

			buf.append(Long.toString((int) bytes[i] & 0xff, 16));
		}

		return buf.toString();
	}

}
