/**
 * 
 */
package com.vcw.falecpv.web.ctrl.productos;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.vcw.falecpv.core.servicio.CategoriaServicio;
import com.vcw.falecpv.web.common.BaseCtrl;

/**
 * @author cristianvillarreal
 *
 */
@Named
@ViewScoped
public class CategoriaCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8788719067123516137L;
	
	@EJB
	private CategoriaServicio categoriaServicio;

	/**
	 * 
	 */
	public CategoriaCtrl() {
	}

	@Override
	public void limpiar() {
		super.limpiar();
	}

	@Override
	public void buscar() {
		super.buscar();
	}

	@Override
	public void refrescar() {
		super.refrescar();
	}

	@Override
	public void eliminar() {
		super.eliminar();
	}

	@Override
	public void guardar() {
		super.guardar();
	}
	
	

}
