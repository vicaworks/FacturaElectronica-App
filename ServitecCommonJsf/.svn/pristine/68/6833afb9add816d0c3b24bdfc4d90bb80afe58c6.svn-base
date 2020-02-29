/**
 * 
 */
package com.servitec.common.jsf.controller;

import java.io.Serializable;

/**
 * @author cvillarreal
 *
 */
public abstract class ParametroController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3680300823215788590L;

	protected String opcionBusqueda="1";
	
	/**
	 * 
	 */
	public ParametroController() {
	}
	
	public abstract void guardar();
	public abstract void editar();
	public abstract void eliminar();
	public abstract void nuevo();
	public abstract void buscar();

	/**
	 * @return the opcionBusqueda
	 */
	public String getOpcionBusqueda() {
		return opcionBusqueda;
	}

	/**
	 * @param opcionBusqueda the opcionBusqueda to set
	 */
	public void setOpcionBusqueda(String opcionBusqueda) {
		this.opcionBusqueda = opcionBusqueda;
	}

	public String getDescripcion(){
		switch (opcionBusqueda) {
		case "1":
			
			return "TODOS";
		case "2":
			
			return "ACTIVOS";	
		default:
			return "INACTIVOS";
		}
	}
	
}
