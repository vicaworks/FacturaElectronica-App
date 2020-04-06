/**
 * 
 */
package com.vcw.falecpv.web.validator;

import java.io.Serializable;
import java.util.List;

import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.omnifaces.validator.MultiFieldValidator;

/**
 * @author cristianvillarreal
 *
 */
@Named
@ViewScoped
public class ValidatorDateBeforeThan implements Serializable, MultiFieldValidator {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6739658948884125806L;

	/**
	 * 
	 */
	public ValidatorDateBeforeThan() {
	}

	@Override
	public boolean validateValues(FacesContext arg0, List<UIInput> arg1, List<Object> values) {
		
		String strFecha1 = (String) values.get(0);
		String strFecha2 = (String) values.get(1);
		
		if(strFecha1==null || strFecha2==null) return false;
		
		if(strFecha1.trim().length()==0 || strFecha2.trim().length()==0) return true;
		
		System.out.println(strFecha2.compareTo(strFecha1));
		
		return  strFecha2.compareTo(strFecha1)<=0;
		
	}

	

}
