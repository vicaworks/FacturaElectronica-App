/**
 * 
 */
package com.servitec.common.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.servitec.common.util.exceptions.ParametroRequeridoException;

/**
 * @author cvillarreal
 *
 */
public class ValidarParametro implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7187220812667051679L;

	
	
	/**
	 * 
	 */
	public ValidarParametro() {
		
	}

	
	/**
	 * Valida si el parámetro param es null en caso de ser un objeto, y si es una lista {@link Collection} valida que no este vacia
	 * 
	 * @param param - el objeto a ser validado
	 * @param nombreParametro - el nombre del parámetro que se utiliza para el mensaje de error
	 * @throws ParametroRequeridoException mensaje del archo de configuracián de la aplicacián con el nombre del parámetro
	 */
	@SuppressWarnings("unchecked")
	public static void validar(Object param,String nombreParametro)throws ParametroRequeridoException{
		
		if (param == null){
			throw new ParametroRequeridoException(AppConfiguracion.getString(
					"parametro.requerido", nombreParametro));
		}
		
		
		if (param instanceof Collection){
			
			Collection<Object> lista = (Collection<Object>)param;
			
			if (lista.size()==0){
				throw new ParametroRequeridoException(AppConfiguracion.getString(
						"parametro.requerido", nombreParametro));
			}
		}
		
		if (param instanceof Map){
			Map<Object, Object> mapa = (Map<Object, Object>)param;
			if (mapa.isEmpty()){
				throw new ParametroRequeridoException(AppConfiguracion.getString(
						"parametro.requerido", nombreParametro));
			}
		}
	}
	
	public static void main(String[] arg){
		Map<String, String> edad = new HashMap<String, String>();
		
		try {
			ValidarParametro.validar(edad,"edad");
		} catch (ParametroRequeridoException e) {
			e.printStackTrace();
		}
		
	}
}
