/**
 * 
 */
package com.servitec.common.util;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

/**
 * @author cvillarreal
 *
 */
public class TextoUtil implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8462499429060518993L;
	
	private static final String PATRON_NUMERO_ENTERO = "\\d*";

	/**
	 * 
	 */
	public TextoUtil() {
	}
	
	
	/**
     * Formatea el mensaje en base a los parametros ingresados
     * Se usar&aacute;n al momento de mostrar los mensaje en los diversos componentes
     *
     * @param texto valor al cual se dara formato
     * @param variable variable que se agregara al texto formateado
     * @return {@link String} de la cadena resultante de aplicar el formato
     */
    public static String formatText(String texto, String variable) {
        return String.format(texto, variable);
    }
    

	/**
	 * Se encarga de verificar si la cadena pasada como par�metro tiene una
	 * longitud a la longitud pasada como par�metro.
	 * 
	 * En caso de que el string tenga una longitud mayor lo recorta para que
	 * tenga una longitud igual a la especificada
	 * 
	 * @param str
	 *            El string a ser evaluado
	 * @param longitud
	 *            la longitud que debe tener este string
	 * @return Una cadena de longitud igual a la especificada por el parametro
	 *         longitud
	 * @throws IllegalArgumentException
	 *             cuando la cadena pasada como par�metro es null
	 */
	public static String recortarString(String str, int longitud)
			throws IllegalArgumentException {

		if (str == null) {
			throw new IllegalArgumentException(
					"La cadena pasada como par�metro es null");
		}
		if (str.length() > longitud) {
			return str.substring(0, longitud);
		}

		return str;

	}
	

	/**
	 * Setermina si una cadena contiene solo n�meros
	 * 
	 * @param cadena
	 *            la cadena a evaluar
	 * @return true en caso de que la cadena tenga solo n�meros, false en caso
	 *         contrario
	 */
	public static boolean validarSoloNumeros(String cadena) {
		if (cadena == null)
			return false;

		if (cadena.length() == 0)
			return false;
		return cadena.matches(PATRON_NUMERO_ENTERO);

	}
	
	
	/**
	 * Imprime la pila de errores en un String
	 * 
	 * @param e
	 * @return
	 */

	public static String imprimirStackTrace(Throwable e) {
		StringWriter writer = new java.io.StringWriter();
		PrintWriter pw = new PrintWriter(writer);
		e.printStackTrace(pw);
		return writer.toString();
	}
	
	
	/**
	 * @author cvillarreal
	 * 
	 * @param e
	 * @param length
	 * @return
	 */
	public static String imprimirStackTrace(Throwable e,Integer length) {
		
		return imprimirStackTrace(e).length()>length?imprimirStackTrace(e).substring(0, length-1):imprimirStackTrace(e);
	}

	
	/**
	 * <b>Descricpion : </b> <br/>
	 * 
	 * @param numero
	 * @param campoMensaje
	 * @return
	 * @throws NumberFormatException
	 * @author cvillarreal created 16/12/2009
	 */
	public static BigDecimal cadenaToBigDecimal(String numero,
			String campoMensaje) throws NumberFormatException {

		return new BigDecimal(Double.parseDouble(numero));

	}
	
	/**
	 * <b>Descricpion : </b> <br/>
	 *
	 * @param numero
	 * @param campoMensaje
	 * @return
	 * @throws NumberFormatException 
	 * @author cvillarreal
	 * created 16/12/2009
	 */
	public static Long cadenaToLong(String numero, String campoMensaje)
			throws NumberFormatException {
			return Long.parseLong(numero);
	}
	
	/**
	 * <b>Descricpion : </b> <br/>
	 * 
	 * @param fecha
	 * @param formatoFecha
	 * @return
	 * @throws ParseException
	 * @author cvillarreal created 16/12/2009
	 */
	public static Date cadenaToFecha(String fecha, String formatoFecha)
			throws ParseException {

		SimpleDateFormat formato = new SimpleDateFormat(formatoFecha);
		return formato.parse(fecha);

	}
	
	/**
	 * @author phidalgo
	 * 
	 * Transforma una cadena a integer
	 * 
	 * @param cadena valor de la cadena
	 * @return un {@link Integer}
	 * @throws NumberFormatException
	 */
	public static Integer cadenaToInteger(String cadena)throws NumberFormatException{
		return Integer.valueOf(cadena);
	}
	
	/**
	 *  Rellena la cadena con ceros a la izquierda
	 * @author iLobato, Jpillajo
	 * @param cadena
	 * @param formato
	 * @return
	 */
	public static String rellenarCadenaConCeros(String formato, Object cadena) {
		return String.format(formato, cadena);
	}
	
	/**
	 * 
	 * Completa la cadena <br/>
	 * Si la cadena es mayor a la congitud, retorna una cadena con la longitud asignada<br/>
	 * Si la cadena es menor a la longitud la completa con carateres del par&aacute;metro <b>caracter</b>
	 * 
	 * @author cvillarreal
	 * @param cadena
	 * @param longitud
	 * @param caracter
	 * @return
	 */
	public static String leftPadTexto(String cadena,int longitud,String caracter){
		
		if (cadena.length()>longitud){
			return cadena.substring(0,longitud);
		}
		
		return StringUtils.leftPad(cadena, longitud, caracter);
		
	}
	
	/**
	 * @author cristianvillarreal
	 * Transforma la cadena de texto en formato HTML
	 * @param string
	 * @return
	 */
	public static String stringToHTMLString(String string) {
	    StringBuffer sb = new StringBuffer(string.length());
	    // true if last char was blank
	    boolean lastWasBlankChar = false;
	    int len = string.length();
	    char c;

	    for (int i = 0; i < len; i++) {
	        c = string.charAt(i);
	        if (c == ' ') {
	            // blank gets extra work,
	            // this solves the problem you get if you replace all
	            // blanks with &nbsp;, if you do that you loss 
	            // word breaking
	            if (lastWasBlankChar) {
	                lastWasBlankChar = false;
	                sb.append("&nbsp;");
	            } else {
	                lastWasBlankChar = true;
	                sb.append(' ');
	            }
	        } else {
	            lastWasBlankChar = false;
	            //
	            // HTML Special Chars
	            if (c == '"')
	                sb.append("&quot;");
	            else if (c == '&')
	                sb.append("&amp;");
	            else if (c == '<')
	                sb.append("&lt;");
	            else if (c == '>')
	                sb.append("&gt;");
	            else if (c == '\n')
	                // Handle Newline
	                sb.append("<br/>");
	            else {
	                int ci = 0xffff & c;
	                if (ci < 160)
	                    // nothing special only 7 Bit
	                    sb.append(c);
	                else {
	                    // Not 7 Bit use the unicode system
	                    sb.append("&#");
//	                    sb.append(new Integer(ci).toString());
	                    sb.append(ci + "");
	                    sb.append(';');
	                }
	            }
	        }
	    }
	    return sb.toString();
	}
	
}
