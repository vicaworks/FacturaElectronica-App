/**
 * 
 */
package com.vcw.falecpv.web.validator;

import java.io.Serializable;
import java.text.ParseException;
import java.util.List;

import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.omnifaces.validator.MultiFieldValidator;

import com.servitec.common.util.FechaUtil;

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
		
		try {
//			System.out.println(FechaUtil.formatoFecha(strFecha2).compareTo(FechaUtil.formatoFecha(strFecha1))<=0);
			return  FechaUtil.formatoFecha(strFecha2).compareTo(FechaUtil.formatoFecha(strFecha1))<=0;
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		
	}

	

}
