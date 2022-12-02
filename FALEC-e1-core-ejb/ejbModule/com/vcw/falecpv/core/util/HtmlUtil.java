/**
 * 
 */
package com.vcw.falecpv.core.util;

import java.text.Normalizer;

import com.servitec.common.util.AppConfiguracion;

/**
 * @author cristianvillarreal
 *
 */
public class HtmlUtil {

	/**
	 * 
	 */
	public HtmlUtil() {
		
	}
	
	/**
	 * Remplaza codigo hatml
	 * @param source
	 * @return
	 */
	public static String normalizeUsingJavaText(String source){
	    source = Normalizer.normalize(source, Normalizer.Form.NFD);
	    return source.replaceAll("[^\\p{ASCII}]", "");
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param cadena
	 * @return
	 */
	public static String sustituirCaracteres(String cadena) {
		if(cadena!=null) {
			String[] mayusculasTile = AppConfiguracion.getString("mayuscula.tile").split(",");
			String[] mayusculasTileRemplazar =  AppConfiguracion.getString("mayuscula.remplazar").split(",");
			
			for (int i = 0; i < mayusculasTile.length; i++) {
				cadena = cadena.replace(mayusculasTile[i],mayusculasTileRemplazar[i]);
			}
		}
		return cadena;
	}

}
