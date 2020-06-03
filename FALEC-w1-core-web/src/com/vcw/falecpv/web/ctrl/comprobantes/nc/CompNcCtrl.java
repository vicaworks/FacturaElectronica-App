/**
 * 
 */
package com.vcw.falecpv.web.ctrl.comprobantes.nc;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.FechaUtil;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.servicio.CabeceraServicio;
import com.vcw.falecpv.core.servicio.UsuarioServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@SessionScoped
public class CompNcCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1948894985826004161L;
	
	@EJB
	private UsuarioServicio usuarioServicio;
	
	@EJB
	private CabeceraServicio cabeceraServicio;
	
	
	private Date desde;
	private Date hasta;
	private String criterioBusqueda;
	private List<Cabecera> notaCreditoList;
	private Cabecera notaCreditoSelected;
	
	/**
	 * 
	 */
	public CompNcCtrl() {
	}
	
	@PostConstruct
	private void init() {
		try {
			hasta = new Date();
			desde = FechaUtil.agregarDias(hasta, -30);
			criterioBusqueda = null;
			consultar();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void consultar()throws DaoException{
		notaCreditoList = null;
		notaCreditoList = cabeceraServicio.getCabeceraDao().getByNotaCreditoCriteria(desde, hasta, criterioBusqueda, AppJsfUtil.getEstablecimiento().getIdestablecimiento());
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
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	@Override
	public void editar() {
		try {
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public String nuevaNotaCredito() {
		try {
			
			//retencionFormCtrl.nuevaRetencionDispacher();
			
			NotaCreditoCtrl notaCreditoCtrl = (NotaCreditoCtrl) AppJsfUtil.getManagedBean("notaCreditoCtrl");
			notaCreditoCtrl.setCallModule("NOTACREDITO");
			notaCreditoCtrl.nuevaNotaCredito();
			
			
			return "./notacredito_form.jsf?faces-redirect=true";
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		
		return null;
	}

	/**
	 * @return the desde
	 */
	public Date getDesde() {
		return desde;
	}

	/**
	 * @param desde the desde to set
	 */
	public void setDesde(Date desde) {
		this.desde = desde;
	}

	/**
	 * @return the hasta
	 */
	public Date getHasta() {
		return hasta;
	}

	/**
	 * @param hasta the hasta to set
	 */
	public void setHasta(Date hasta) {
		this.hasta = hasta;
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
	 * @return the notaCreditoList
	 */
	public List<Cabecera> getNotaCreditoList() {
		return notaCreditoList;
	}

	/**
	 * @param notaCreditoList the notaCreditoList to set
	 */
	public void setNotaCreditoList(List<Cabecera> notaCreditoList) {
		this.notaCreditoList = notaCreditoList;
	}

	/**
	 * @return the notaCreditoSelected
	 */
	public Cabecera getNotaCreditoSelected() {
		return notaCreditoSelected;
	}

	/**
	 * @param notaCreditoSelected the notaCreditoSelected to set
	 */
	public void setNotaCreditoSelected(Cabecera notaCreditoSelected) {
		this.notaCreditoSelected = notaCreditoSelected;
	}

}
