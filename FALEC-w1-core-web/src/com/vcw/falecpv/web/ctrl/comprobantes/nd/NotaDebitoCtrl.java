/**
 * 
 */
package com.vcw.falecpv.web.ctrl.comprobantes.nd;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.primefaces.model.StreamedContent;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.FechaUtil;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.servicio.CabeceraServicio;
import com.vcw.falecpv.core.servicio.NotaDebitoServicio;
import com.vcw.falecpv.core.servicio.UsuarioServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@SessionScoped
public class NotaDebitoCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1641640201539263798L;
	
	@EJB
	private NotaDebitoServicio notaDebitoServicio;
	
	@EJB
	private CabeceraServicio cabeceraServicio;
	
	@EJB
	private UsuarioServicio usuarioServicio;
	
	
	private Date desde;
	private Date hasta;
	private String criterioBusqueda;
	private List<Cabecera> notDebitoList;
	private Cabecera notDebitoSelected;

	/**
	 * 
	 */
	public NotaDebitoCtrl() {
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
		AppJsfUtil.limpiarFiltrosDataTable("formMain:notDebitoDT");
		notDebitoList = null;
		notDebitoList = null;
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
			
			String analisis = notaDebitoServicio.analizarEstado(notDebitoSelected.getIdcabecera(), AppJsfUtil.getEstablecimiento().getIdestablecimiento(), "ANULAR");
			if(analisis!=null) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", analisis);
				return;
			}
			
			cabeceraServicio.anularGuiaRemisionFacade(notDebitoSelected.getIdcabecera());
			consultar();
			notDebitoSelected = null;
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public String nuevanotaDebito(){
		try {
			
//			guiaRemFormCtrl = (GuiaRemFormCtrl) AppJsfUtil.getManagedBean("guiaRemFormCtrl");
//			guiaRemFormCtrl.nuevaGuiaRemision();
			
			return "./notaDebitoForm.jsf?faces-redirect=true";
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		return null;
	}
	
	public StreamedContent getFileGR() {
		try {
			
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
	 * @return the notDebitoList
	 */
	public List<Cabecera> getNotDebitoList() {
		return notDebitoList;
	}

	/**
	 * @param notDebitoList the notDebitoList to set
	 */
	public void setNotDebitoList(List<Cabecera> notDebitoList) {
		this.notDebitoList = notDebitoList;
	}

	/**
	 * @return the notDebitoSelected
	 */
	public Cabecera getNotDebitoSelected() {
		return notDebitoSelected;
	}

	/**
	 * @param notDebitoSelected the notDebitoSelected to set
	 */
	public void setNotDebitoSelected(Cabecera notDebitoSelected) {
		this.notDebitoSelected = notDebitoSelected;
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

}
