/**
 * 
 */
package com.vcw.falecpv.core.util;

import java.util.Base64;

/**
 * @author cristianvillarreal
 *
 */
public class EncoderUtil {

	/**
	 * 
	 */
	public EncoderUtil() {
		
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param base64String
	 * @return
	 */
	public static byte[] decodeBase64String(String base64String) {
		if(base64String == null) return null;
		return Base64.getDecoder().decode(base64String);
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param hexString
	 * @return
	 */
	public static byte[] decodeHexString(String hexString) {
	    if (hexString.length() % 2 == 1) {
	        throw new IllegalArgumentException(
	          "Cadena hexadecimal erronea.");
	    }
	     
	    byte[] bytes = new byte[hexString.length() / 2];
	    for (int i = 0; i < hexString.length(); i += 2) {
	        bytes[i / 2] = hexToByte(hexString.substring(i, i + 2));
	    }
	    return bytes;
	}
	
	private static byte hexToByte(String hexString) {
	    int firstDigit = toDigit(hexString.charAt(0));
	    int secondDigit = toDigit(hexString.charAt(1));
	    return (byte) ((firstDigit << 4) + secondDigit);
	}
	
	private static int toDigit(char hexChar) {
	    int digit = Character.digit(hexChar, 16);
	    if(digit == -1) {
	        throw new IllegalArgumentException(
	          "Invalid Hexadecimal Character: "+ hexChar);
	    }
	    return digit;
	}

}
