/**
 * 
 */
package com.vcw.falecpv.web.ctrl.herramientas;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.modelo.persistencia.Transportista;
import com.vcw.falecpv.core.servicio.TransportistaServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@ViewScoped
public class TransportistaMainCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3699480692753928950L;
	
	@EJB
	private TransportistaServicio transportistaServicio;

	private String criterioBusqueda;
	private String estadoRegBusqueda;
	private List<Transportista> transportistaList;
	private Transportista transportistaSelected;
	
	/**
	 * 
	 */
	public TransportistaMainCtrl() {
	}
	
	@PostConstruct
	private void init() {
		try {
			estadoRegBusqueda = EstadoRegistroEnum.ACTIVO.getInicial();
			consultar();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void consultar()throws DaoException{
		transportistaList = null;
		transportistaList = transportistaServicio.getTransportistaDao().getByCriteria(
				AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa(), criterioBusqueda,
				EstadoRegistroEnum.getByInicial(estadoRegBusqueda));
	}

	@Override
	public void buscar() {
		try {
			consultar();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	@Override
	public void eliminar() {
		try {
			
			if(transportistaSelected==null) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTE REGISTRO SELECCIONADO.");
				return;
			}
			
			// valida dependencia
			if(transportistaServicio.tieneDependencias(transportistaSelected.getIdtransportista(), AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa())) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO SE PUEDE ELIMINAR EL REGISTRO TIENE DEPENDENCIAS.");
				return;
			}
			
			transportistaServicio.eliminar(transportistaSelected);
			transportistaSelected = null;
			consultar();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	/**
	 * @return the criterioBusqueda
	 */
	public String getCriterioBusqueda() {
		return criterioBusqueda;
	}

	/**
	 * @param criterioBusqueda the criterioBusqueda to set
	 */
	public void setCriterioBusqueda(String criterioBusqueda) {
		this.criterioBusqueda = criterioBusqueda;
	}

	/**
	 * @return the estadoRegBusqueda
	 */
	public String getEstadoRegBusqueda() {
		return estadoRegBusqueda;
	}

	/**
	 * @param estadoRegBusqueda the estadoRegBusqueda to set
	 */
	public void setEstadoRegBusqueda(String estadoRegBusqueda) {
		this.estadoRegBusqueda = estadoRegBusqueda;
	}

	/**
	 * @return the transportistaList
	 */
	public List<Transportista> getTransportistaList() {
		return transportistaList;
	}

	/**
	 * @param transportistaList the transportistaList to set
	 */
	public void setTransportistaList(List<Transportista> transportistaList) {
		this.transportistaList = transportistaList;
	}

	/**
	 * @return the transportistaSelected
	 */
	public Transportista getTransportistaSelected() {
		return transportistaSelected;
	}

	/**
	 * @param transportistaSelected the transportistaSelected to set
	 */
	public void setTransportistaSelected(Transportista transportistaSelected) {
		this.transportistaSelected = transportistaSelected;
	}

}
