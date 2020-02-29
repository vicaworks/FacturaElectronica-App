/**
 * 
 */
package com.servitec.common.jsf.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 * @author cvillarreal
 *
 */
public class MayusculaConverter implements Converter {

	/**
	 * 
	 */
	public MayusculaConverter() {
	}

	@Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return value == null ? null : value.toString().toUpperCase();
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return value == null ? null : value.toString().toUpperCase();
    }

}
