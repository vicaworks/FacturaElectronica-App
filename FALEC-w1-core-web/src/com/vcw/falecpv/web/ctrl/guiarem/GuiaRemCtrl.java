/**
 * 
 */
package com.vcw.falecpv.web.ctrl.guiarem;

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
import com.vcw.falecpv.core.servicio.GuiaRemisionServicio;
import com.vcw.falecpv.core.servicio.UsuarioServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@SessionScoped
public class GuiaRemCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5481608094276437109L;

	@EJB
	private GuiaRemisionServicio guiaRemisionServicio;
	
	@EJB
	private CabeceraServicio cabeceraServicio;
	
	@EJB
	private UsuarioServicio usuarioServicio;
	
	
	private Date desde;
	private Date hasta;
	private String criterioBusqueda;
	private List<Cabecera> guiaRemisionList;
	private Cabecera guiaRemisionSelected;
	private GuiaRemFormCtrl guiaRemFormCtrl;
	
	
	/**
	 * 
	 */
	public GuiaRemCtrl() {
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
		AppJsfUtil.limpiarFiltrosDataTable("formMain:guiaRemDT");
		guiaRemisionList = null;
		guiaRemisionList = guiaRemisionServicio.getByDateCriteria(AppJsfUtil.getEstablecimiento().getIdestablecimiento(), desde, hasta, criterioBusqueda);
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
			
			String analisis = guiaRemisionServicio.analizarEstado(guiaRemisionSelected.getIdcabecera(), AppJsfUtil.getEstablecimiento().getIdestablecimiento(), "ANULAR");
			if(analisis!=null) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", analisis);
				return;
			}
			
			cabeceraServicio.anularById(guiaRemisionSelected.getIdcabecera());
			consultar();
			guiaRemisionSelected = null;
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public String nuevaGuiaRemision(){
		try {
			
			guiaRemFormCtrl = (GuiaRemFormCtrl) AppJsfUtil.getManagedBean("guiaRemFormCtrl");
			guiaRemFormCtrl.nuevaGuiaRemision();
			
			return "./guiaRemForm.jsf?faces-redirect=true";
			
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
	 * @return the guiaRemisionList
	 */
	public List<Cabecera> getGuiaRemisionList() {
		return guiaRemisionList;
	}

	/**
	 * @param guiaRemisionList the guiaRemisionList to set
	 */
	public void setGuiaRemisionList(List<Cabecera> guiaRemisionList) {
		this.guiaRemisionList = guiaRemisionList;
	}

	/**
	 * @return the guiaRemisionSelected
	 */
	public Cabecera getGuiaRemisionSelected() {
		return guiaRemisionSelected;
	}

	/**
	 * @param guiaRemisionSelected the guiaRemisionSelected to set
	 */
	public void setGuiaRemisionSelected(Cabecera guiaRemisionSelected) {
		this.guiaRemisionSelected = guiaRemisionSelected;
	}

	/**
	 * @return the guiaRemFormCtrl
	 */
	public GuiaRemFormCtrl getGuiaRemFormCtrl() {
		return guiaRemFormCtrl;
	}

	/**
	 * @param guiaRemFormCtrl the guiaRemFormCtrl to set
	 */
	public void setGuiaRemFormCtrl(GuiaRemFormCtrl guiaRemFormCtrl) {
		this.guiaRemFormCtrl = guiaRemFormCtrl;
	}
	
	
	

}
