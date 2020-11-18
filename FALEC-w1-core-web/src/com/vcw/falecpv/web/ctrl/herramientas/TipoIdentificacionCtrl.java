package com.vcw.falecpv.web.ctrl.herramientas;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.modelo.persistencia.TipoIdentificacion;
import com.vcw.falecpv.core.servicio.TipoIdentificacionServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author Jorge Toaza
 *
 */
@Named
@ViewScoped
public class TipoIdentificacionCtrl extends BaseCtrl {

	private static final long serialVersionUID = -8788719067123516137L;
	
	@EJB
	private TipoIdentificacionServicio tipoIdentificacionServicio;
	
	private List<TipoIdentificacion> tipoIdentificacionList;

	public TipoIdentificacionCtrl() {
	}
	
	@PostConstruct
	private void init()  {
		try {
			tipoIdentificacionList = new ArrayList<>();
			consultarTiposIdentificacion();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	@Override
	public void refrescar() {
	}
	
	@Override
	public void nuevo() {
	}
	
	@Override
	public void eliminar() {
	}
	
	public void consultarTiposIdentificacion() throws DaoException {
		tipoIdentificacionList = tipoIdentificacionServicio.getTipoIdentificacionDao().consultarTiposIdentificacion();
	}

	/**
	 * @return the tipoIdentificacionList
	 */
	public List<TipoIdentificacion> getTipoIdentificacionList() {
		return tipoIdentificacionList;
	}

	/**
	 * @param tipoIdentificacionList the tipoIdentificacionList to set
	 */
	public void setTipoIdentificacionList(List<TipoIdentificacion> tipoIdentificacionList) {
		this.tipoIdentificacionList = tipoIdentificacionList;
	}
}