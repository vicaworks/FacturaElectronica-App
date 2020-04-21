/**
 * 
 */
package com.vcw.falecpv.web.ctrl.adquisicion;

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
import com.vcw.falecpv.core.modelo.persistencia.Retencion;
import com.vcw.falecpv.core.servicio.RetencionServicio;
import com.vcw.falecpv.core.servicio.UsuarioServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@SessionScoped
public class RetencionMainCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3490916972455247661L;

	@EJB
	private UsuarioServicio usuarioServicio;
	
	@EJB
	private RetencionServicio retencionServicio;
	
	
	private Date desde;
	private Date hasta;
	private String criterioBusqueda;
	private List<Retencion> retencionList;
	private RetencionFrmCtrl retencionFormCtrl;
	private Retencion retencionSelected;
	
	
	/**
	 * 
	 */
	public RetencionMainCtrl() {
	}

	@PostConstruct
	private void init() {
		try {
			hasta = new Date();
			desde = FechaUtil.agregarDias(hasta, -90);
			criterioBusqueda = null;
			retencionFormCtrl = (RetencionFrmCtrl) AppJsfUtil.getManagedBean("retencionFrmCtrl");
			consultarRetenciones();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void consultarRetenciones()throws DaoException{
		AppJsfUtil.limpiarFiltrosDataTable("formMain:retencionDT");
		retencionList = null;
		retencionList = retencionServicio.getRetencionDao()
				.getByCriteria(AppJsfUtil.getEstablecimiento().getIdestablecimiento(), desde, hasta, criterioBusqueda);
	}
	
	@Override
	public void refrescar() {
		try {
			
			consultarRetenciones();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	@Override
	public void eliminar() {
		try {
			
			retencionSelected = retencionServicio.consultarByPk(retencionSelected.getIdretencion());
			
			if(retencionSelected == null) return;
			
			if(retencionSelected.getEstado().equals("REGISTRO") || retencionSelected.getEstado().equals("RETENCION")) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO SE PUEDE ANULAR, SE ENCUENTRA EN ESTADO : " + retencionSelected.getEstado());
				return;
			}
			
			retencionServicio.anularRetencion(retencionSelected.getIdretencion());
			consultarRetenciones();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	
	public String editarRetencion(String idRetencion) {
		try {
			
			retencionFormCtrl.editarRetencion(idRetencion);
			
			return "./retencion_form.jsf?faces-redirect=true";
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
			return null;
		}
	}

	public String nuevaRetencion() {
		try {
			
			retencionFormCtrl.nuevaRetencionDispacher();
			
			return "./retencion_form.jsf?faces-redirect=true";
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		
		return null;
	}
	
	/**
	 * @return the usuarioServicio
	 */
	public UsuarioServicio getUsuarioServicio() {
		return usuarioServicio;
	}

	/**
	 * @param usuarioServicio the usuarioServicio to set
	 */
	public void setUsuarioServicio(UsuarioServicio usuarioServicio) {
		this.usuarioServicio = usuarioServicio;
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
	 * @return the retencionList
	 */
	public List<Retencion> getRetencionList() {
		return retencionList;
	}

	/**
	 * @param retencionList the retencionList to set
	 */
	public void setRetencionList(List<Retencion> retencionList) {
		this.retencionList = retencionList;
	}

	/**
	 * @return the retencionFormCtrl
	 */
	public RetencionFrmCtrl getRetencionFormCtrl() {
		return retencionFormCtrl;
	}

	/**
	 * @param retencionFormCtrl the retencionFormCtrl to set
	 */
	public void setRetencionFormCtrl(RetencionFrmCtrl retencionFormCtrl) {
		this.retencionFormCtrl = retencionFormCtrl;
	}

	/**
	 * @return the retencionSelected
	 */
	public Retencion getRetencionSelected() {
		return retencionSelected;
	}

	/**
	 * @param retencionSelected the retencionSelected to set
	 */
	public void setRetencionSelected(Retencion retencionSelected) {
		this.retencionSelected = retencionSelected;
	}
	

}
