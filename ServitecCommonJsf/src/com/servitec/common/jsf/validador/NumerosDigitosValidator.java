/**
 * 
 */
package com.servitec.common.jsf.validador;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import com.servitec.common.util.ExpresionRegularValidador;
import com.servitec.common.util.exceptions.ValidarExpresionException;

/**
 * @author cvillarreal
 *
 */
@FacesValidator("numeroDigitosValidator")
public class NumerosDigitosValidator implements Validator {

	/**
	 * 
	 */
	public NumerosDigitosValidator() {
	}

	/* (non-Javadoc)
	 * @see javax.faces.validator.Validator#validate(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
	 */
	@Override
	public void validate(FacesContext arg0, UIComponent arg1, Object arg2)
			throws ValidatorException {
		
		if (arg2==null || ((String)arg2).trim().length()==0) return;
		
		
		int cantidadDigitos = ((Long) arg1.getAttributes().get("cantidadDigitos")).intValue();
		String valor = (String)arg2;
		
		try {
			ExpresionRegularValidador.validarSoloNumeros(valor, cantidadDigitos);
		} catch (ValidarExpresionException e) {
			e.printStackTrace();
			FacesMessage msg = new FacesMessage("ERROR",e.getMessage());
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		}
		
		
	}

}
