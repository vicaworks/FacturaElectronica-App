/**
 * 
 */
package com.servitec.common.util;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.servitec.common.util.exceptions.ValidarExpresionException;

/**
 * @author cvillarreal
 *
 */
public class ExpresionRegularValidador implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1633619763637354300L;

	/**
	 * 
	 */
	public ExpresionRegularValidador() {
		
	}
	
	/**
	 * @param input
	 * @throws ValidarExpresionException
	 */
	public static void validarEmail(String input) throws ValidarExpresionException {

		Pattern p = Pattern.compile("^\\.|^\\@|^\\www");
		Matcher m = p.matcher(input);
		if (m.find()) {
			throw new ValidarExpresionException(input +  ", no puede empezar con . @ www");
		}

		p = Pattern.compile("\\@");
		m = p.matcher(input);
		if (!m.find())
			throw new ValidarExpresionException(input +  ", no contiene @");

		// comprueba que no contenga caracteres prohibidos
//		p = Pattern.compile("[^A-Za-z0-9\\.\\@_\\-~#]+");
		p = Pattern.compile("^[_A-Za-z0-9-\\\\+]+(\\\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\\\.[A-Za-z0-9]+)*(\\\\.[A-Za-z]{2,})$");
		m = p.matcher(input);
		StringBuffer sb = new StringBuffer();
		boolean resultado = m.find();
		boolean caracteresIlegales = false;

		while (resultado) {
			caracteresIlegales = true;
			m.appendReplacement(sb, "");
			resultado = m.find();
		}

		// AÃ’ade el ultimo segmento de la entrada a la cadena
		m.appendTail(sb);

		input = sb.toString();

		if (caracteresIlegales) {
			throw new ValidarExpresionException(input +  ", contiene caracteres invalidos");
		}

	}
	
	 public static boolean validarEmailAnterior(String email)  throws ValidarExpresionException {
        // Patron para validar el email
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
 
        Matcher mather = pattern.matcher(email);
        return mather.find();
    }
	
	
	/**
	 * @param input
	 * @throws ValidarSoloNumerosException
	 */
	public static void validarSoloNumeros(String input)
			throws ValidarExpresionException {

		Pattern p = Pattern.compile("^([0-9])*$");
		Matcher m = p.matcher(input);
		if (!m.find()) {
			throw new ValidarExpresionException(
					"La cadena debe tener solo numeros, no espacios.");
		}

	}
	
	/**
	 * @param input
	 * @throws ValidarExpresionException
	 */
	public static void validarSoloNumerosRayaEspacio(String input)
			throws ValidarExpresionException {

		Pattern p = Pattern.compile("^([0-9- ])*$");
		Matcher m = p.matcher(input);
		if (!m.find()) {
			throw new ValidarExpresionException(
					"La cadena debe tener solo numeros (-) y espacio.");
		}

	}
	
	/**
	 * @param input
	 * @throws ValidarExpresionException
	 */
	public static void validarSoloLetrasEspacio(String input)
			throws ValidarExpresionException {

		Pattern p = Pattern.compile("^([a-zA-ZÃ±Ã‘Ã¡Ã©Ã­Ã³ÃºÃ�Ã‰Ã�Ã“Ãš ])*$");
		Matcher m = p.matcher(input);
		if (!m.find()) {
			throw new ValidarExpresionException(
					"La cadena debe tener solo letras y espacio en blanco no caracteres especiales.");
		}

	}
	
	/**
	 * @param input
	 * @throws ValidarExpresionException
	 */
	public static void validarSoloLetrasEspacioNumeros(String input)
			throws ValidarExpresionException {

		Pattern p = Pattern.compile("^([a-zA-ZÃ±Ã‘Ã¡Ã©Ã­Ã³ÃºÃ�Ã‰Ã�Ã“Ãš0-9 ])*$");
		Matcher m = p.matcher(input);
		if (!m.find()) {
			throw new ValidarExpresionException(
					"La cadena debe tener solo letras, nÃºmeros, y espacios en blanco no caracteres especiales.");
		}

	}
	
	/**
	 * @param input
	 * @throws ValidarExpresionException
	 */
	public static void validarSoloLetrasNumeros(String input)
			throws ValidarExpresionException {

		Pattern p = Pattern.compile("^([a-zA-ZÃ±Ã‘Ã¡Ã©Ã­Ã³ÃºÃ�Ã‰Ã�Ã“Ãš0-9])*$");
		Matcher m = p.matcher(input);
		if (!m.find()) {
			throw new ValidarExpresionException(
					"La cadena debe tener solo letras, nÃºmeros, y espacios en blanco no caracteres especiales.");
		}

	}
	
	/**
	 * @param input
	 * @throws ValidarExpresionException
	 */
	public static void validarSoloLetras(String input)
			throws ValidarExpresionException {

		Pattern p = Pattern.compile("^([a-zA-ZÃ±Ã‘Ã¡Ã©Ã­Ã³ÃºÃ�Ã‰Ã�Ã“Ãš])*$");
		Matcher m = p.matcher(input);
		if (!m.find()) {
			throw new ValidarExpresionException(
					"La cadena debe tener solo letras, no espacios en blanco, y caracteres especiales.");
		}

	}
	
	/**
	 * @param input
	 * @throws ValidarTelefonoException
	 */
	public static void validarNumeroTelefono(String input)
			throws ValidarExpresionException {

		Pattern p = Pattern
				.compile("^(\\(?[0-9]{3,3}\\)?|[0-9]{3,3}[-. ]?)[0-9]{7,13}$");
		Matcher m = p.matcher(input);
		if (!m.find()) {
			throw new ValidarExpresionException(
					input +  ", Formato de telefono (###)#######");
		}

	}
	
	/**
	 * @param input
	 * @throws ValidarTelefonoException
	 */
	public static void validarSoloNumeros(String input,Integer cantidadDigitos)
			throws ValidarExpresionException {

		Pattern p = Pattern.compile("^([0-9]{" + cantidadDigitos + "})*$");
		Matcher m = p.matcher(input);
		if (!m.find()) {
			throw new ValidarExpresionException(
					"La cadena debe tener solo numeros, no espacios.");
		}

	}
	

	/**
	 * @param input
	 * @throws ValidarExpresionException
	 */
	public static void validarSoloNumerosDecimalesPunto(String input)
			throws ValidarExpresionException {
		Pattern p = Pattern.compile("^\\d+|^\\d+\\.?\\d+");
		Matcher m = p.matcher(input);
		if (!m.matches()) {
			throw new ValidarExpresionException(
					"La cadena debe tener solo numeros decimales");
		}

	}
	
	
	

}
