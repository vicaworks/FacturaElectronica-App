/**
 * 
 */
package com.vcw.falecpv.core.helper;

import com.servitec.common.util.ExpresionRegularValidador;
import com.servitec.common.util.exceptions.ValidarExpresionException;

/**
 * @author cristianvillarreal
 *
 */
public class ValidacionHelper {

	/**
	 * 
	 */
	public ValidacionHelper() {
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param value
	 * @throws ValidarExpresionException
	 */
	public static void validarEmail(String value)throws ValidarExpresionException {
		String[] mailArray = (((String) value)).trim().split(",");

		for (String mail : mailArray) {
			try {
				ExpresionRegularValidador.validarEmail(((String) mail).trim());
			} catch (ValidarExpresionException e) {
				throw new ValidarExpresionException("EMAIL : " + mail + " INCORECTO.");
			}
		}
	}

}
