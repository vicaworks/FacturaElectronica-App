package com.vcw.falecpv.web.validator;

import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.primefaces.validate.ClientValidator;

import com.servitec.common.util.ExpresionRegularValidador;
import com.servitec.common.util.exceptions.ValidarExpresionException;

@FacesValidator("custom.emailValidator")
public class EmailValidator implements Validator, ClientValidator {

	public EmailValidator() {
		//
	}

	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		if (value == null) {
			return;
		}

		String[] mailArray = (((String) value)).trim().split(",");

		for (String mail : mailArray) {
			try {
				ExpresionRegularValidador.validarEmail(((String) mail).trim());
			} catch (ValidarExpresionException e) {
				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Validation Error", e.getMessage()));
			}
		}
	}

	public Map<String, Object> getMetadata() {
		return null;
	}

	public String getValidatorId() {
		return "custom.emailValidator";
	}
}
