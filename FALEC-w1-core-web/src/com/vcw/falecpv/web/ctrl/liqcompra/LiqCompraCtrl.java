/**
 * 
 */
package com.vcw.falecpv.web.ctrl.liqcompra;

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
public class LiqCompraCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4702498949971426306L;
	
	@EJB
	private CabeceraServicio cabeceraServicio;
	
	@EJB
	private UsuarioServicio usuarioServicio;

	private Date desde;
	private Date hasta;
	private String criterioBusqueda;
	private List<Cabecera> liqCompraList;
	private Cabecera liqCompraSelected;
	
	/**
	 * 
	 */
	public LiqCompraCtrl() {
	}
	
	@PostConstruct
	private void init() {
		try {
			hasta = new Date();
			desde = FechaUtil.agregarDias(hasta, -60);
			criterioBusqueda = null;
			consultar();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void consultar()throws DaoException{
		AppJsfUtil.limpiarFiltrosDataTable("formMain:liqCompraDT");
		liqCompraList = null;
		liqCompraList = cabeceraServicio.getCabeceraDao().getByLiqCompraCriteria(desde, hasta, criterioBusqueda, AppJsfUtil.getEstablecimiento().getIdestablecimiento(), estado);
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
	public void nuevo() {
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public String nuevaLiqCompra() {
		try {
			
			LiqCompraFormCtrl liqCompraFormCtrl = (LiqCompraFormCtrl) AppJsfUtil.getManagedBean("liqCompraFormCtrl");
			liqCompraFormCtrl.nuevaLiqCompra();
			
			return "./liqCompraForm.jsf?faces-redirect=true";
			
			
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
	 * @return the liqCompraList
	 */
	public List<Cabecera> getLiqCompraList() {
		return liqCompraList;
	}

	/**
	 * @param liqCompraList the liqCompraList to set
	 */
	public void setLiqCompraList(List<Cabecera> liqCompraList) {
		this.liqCompraList = liqCompraList;
	}

	/**
	 * @return the liqCompraSelected
	 */
	public Cabecera getLiqCompraSelected() {
		return liqCompraSelected;
	}

	/**
	 * @param liqCompraSelected the liqCompraSelected to set
	 */
	public void setLiqCompraSelected(Cabecera liqCompraSelected) {
		this.liqCompraSelected = liqCompraSelected;
	}
	
	
	

}
