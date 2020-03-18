/**
 * 
 */
package com.vcw.falecpv.web.ctrl.configuracion;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@ViewScoped
public class EstablecimientoCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8788719067123516137L;
	
	@EJB
	private EstablecimientoServicio establecimientoServicio;

	/**
	 * 
	 */
	public EstablecimientoCtrl() {
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
	
	public void nuevo() {
		try {
			
			AppJsfUtil.getEstablecimiento().getEmpresa();
			AppJsfUtil.showModalRender("wididM", "idfor");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("", "Error", TextoUtil.imprimirStackTrace(e,AppConfiguracion.getInteger("stacktrace.length")));
		}
		
	}
	
	

}
