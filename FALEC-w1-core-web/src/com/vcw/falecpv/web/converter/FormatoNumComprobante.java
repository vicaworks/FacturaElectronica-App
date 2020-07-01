/**
 * 
 */
package com.vcw.falecpv.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * @author cristianvillarreal
 *
 */
@FacesConverter(value = "formatoNumComprobanteConverter", managed = true)
public class FormatoNumComprobante implements Converter<String> {


	@Override
	public String getAsObject(FacesContext arg0, UIComponent arg1, String numeroDocumento) {
		
		return numeroDocumento!=null?numeroDocumento.replace("-", ""):null;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, String numeroDocumento) {
		return numeroDocumento.substring(0, 3) + "-" + numeroDocumento.substring(3, 6) + "-" + numeroDocumento.substring(6, numeroDocumento.length());
	}


}
