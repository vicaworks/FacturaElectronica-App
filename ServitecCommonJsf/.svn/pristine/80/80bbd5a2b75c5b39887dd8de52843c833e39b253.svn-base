/**
 * 
 */
package com.servitec.common.jsf.converter;

import java.util.Locale;
import java.util.TimeZone;

import javax.faces.convert.DateTimeConverter;

import com.servitec.common.util.AppConfiguracion;

/**
 * @author cvillarreal
 *
 */
public class FechaConverter extends DateTimeConverter {

	/**
	 * 
	 */
	public FechaConverter() {
		
		String pattern = AppConfiguracion.getString("formato.fecha.sistema");
        String idiomaLocale = AppConfiguracion.getString("locale");
        
        setTimeZone(TimeZone.getDefault());
        setPattern(pattern);
        setLocale(new Locale(idiomaLocale));
		
	}

}
