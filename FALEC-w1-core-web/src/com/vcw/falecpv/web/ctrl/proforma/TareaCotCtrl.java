/**
 * 
 */
package com.vcw.falecpv.web.ctrl.proforma;

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
import com.vcw.falecpv.core.constante.EtiquetaModuloEnum;
import com.vcw.falecpv.core.constante.parametrosgenericos.PGEmpresaEnum;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Tareacabecera;
import com.vcw.falecpv.core.modelo.persistencia.Etiqueta;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.falecpv.core.servicio.TareacabeceraServicio;
import com.vcw.falecpv.core.servicio.ParametroGenericoEmpresaServicio.TipoRetornoParametroGenerico;
import com.vcw.falecpv.core.servicio.EtiquetaServicio;
import com.vcw.falecpv.core.servicio.ParametroGenericoEmpresaServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.ctrl.common.MessageCommonCtrl.Message;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@SessionScoped
public class TareaCotCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3334408502335495024L;
	
	@EJB
	private EstablecimientoServicio establecimientoServicio;
	
	@EJB
	private EtiquetaServicio tareaetiquetaServicio;
	
	@EJB
	private TareacabeceraServicio tareacabeceraServicio;
	
	@EJB
	private ParametroGenericoEmpresaServicio parametroGenericoEmpresaServicio;
	
	private String callModule;
	private String callForm;
	private String updateView;
	private Cabecera cotizacionSelected;
	private List<Etiqueta> tareaetiquetaList;
	private Tareacabecera tareacabeceraSelected = new Tareacabecera();
	
	

	/**
	 * 
	 */
	public TareaCotCtrl() {
		
	}

	@PostConstruct
	private void init() {
		try {
			establecimientoFacade(establecimientoServicio, false);
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	public void nuevaTarea() {
		try {
			
			tareacabeceraSelected = new Tareacabecera();
			tareacabeceraSelected.setUsuario(AppJsfUtil.getUsuario());
			tareacabeceraSelected.setCabecera(cotizacionSelected);
			tareacabeceraSelected.setEstado("PENDIENTE");
			tareacabeceraSelected.setPrioridad("BAJA");
			tareacabeceraSelected.setFechalimite(new Date());
			
			consultarEtiquetas();
			AppJsfUtil.showModalRender("dlgCotizacionTarea", "frmCotizacionTarea");
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	

	public void consultarEtiquetas() throws DaoException {
		tareaetiquetaList = null;
		tareaetiquetaList = tareaetiquetaServicio.getEtiquetas(establecimientoMain.getEmpresa().getIdempresa(),EtiquetaModuloEnum.TAREA_COTIZACION.toString());
	}
	
	@Override
	public void guardar() {
		try {
			
			if(FechaUtil.comparaFechas(new Date(), tareacabeceraSelected.getFechalimite())>0 && !tareacabeceraSelected.getEstado().equals("VENCIDO")) {
				getMessageCommonCtrl().crearMensaje("Error", 
						"Fechas incorrectas", 
						Message.ERROR);
				return;
			}
			
			// valor de la prioridad
			switch (tareacabeceraSelected.getPrioridad()) {
			case "IMPORTANTE":
				tareacabeceraSelected.setPrioridadvalor(1);
				break;
			case "ALTA":
				tareacabeceraSelected.setPrioridadvalor(2);
				break;
			case "MEDIA":
				tareacabeceraSelected.setPrioridadvalor(3);
				break;
			case "BAJA":
				tareacabeceraSelected.setPrioridadvalor(4);
				break;

			default:
				break;
			}
			
			tareacabeceraSelected.setIdEstablecimiento(cotizacionSelected.getEstablecimiento().getIdestablecimiento());
			if(tareacabeceraSelected.getIdtareacabecera()==null) {
				tareacabeceraServicio.crear(tareacabeceraSelected);
			}else {
				tareacabeceraServicio.actualizar(tareacabeceraSelected);
			}
			
			TareaCotMainCtrl tareaCotMainCtrl = (TareaCotMainCtrl)AppJsfUtil.getManagedBean("tareaCotMainCtrl");
			switch (callModule) {
			case "COTIZACION":
				tareaCotMainCtrl.consultar();
				break;
			case "SEGUIMIENTO" :
				tareaCotMainCtrl.consultar();
				break;
			default:
				break;
			}
			
			getMessageCommonCtrl().crearMensaje("Ok", 
					"Registro guardado correctamente", 
					Message.OK);
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	@Override
	public void eliminar() {
		
		try {
			
			tareacabeceraServicio.eliminar(tareacabeceraSelected);
			
			TareaCotMainCtrl tareaCotMainCtrl = (TareaCotMainCtrl)AppJsfUtil.getManagedBean("tareaCotMainCtrl");
			switch (callModule) {
			case "SEGUIMIENTO" :
				tareaCotMainCtrl.consultar();
				break;
			default:
				break;
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
		
	}
	
	public void cerrar() {
		try {
			
			tareacabeceraSelected.setEstado("CERRADO");
			tareacabeceraServicio.actualizar(tareacabeceraSelected);
			
			TareaCotMainCtrl tareaCotMainCtrl = (TareaCotMainCtrl)AppJsfUtil.getManagedBean("tareaCotMainCtrl");
			switch (callModule) {
			case "SEGUIMIENTO" :
				tareaCotMainCtrl.consultar();
				break;
			default:
				break;
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	
	@Override
	public void editar() {
		try {
			
			consultarEtiquetas();
			AppJsfUtil.showModalRender("dlgCotizacionTarea", "frmCotizacionTarea");
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	public boolean habilitarEtiqueta() {
		try {
			
			return parametroGenericoEmpresaServicio.consultarParametroEmpresa(
					PGEmpresaEnum.COTIZACION_ETIQUETAS_PREDEFIN, TipoRetornoParametroGenerico.BOOLEAN,
					establecimientoMain.getEmpresa().getIdempresa());
			
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
		return false;
	}
	
	/**
	 * @return the callModule
	 */
	public String getCallModule() {
		return callModule;
	}

	/**
	 * @param callModule the callModule to set
	 */
	public void setCallModule(String callModule) {
		this.callModule = callModule;
	}

	/**
	 * @return the callForm
	 */
	public String getCallForm() {
		return callForm;
	}

	/**
	 * @param callForm the callForm to set
	 */
	public void setCallForm(String callForm) {
		this.callForm = callForm;
	}

	/**
	 * @return the updateView
	 */
	public String getUpdateView() {
		return updateView;
	}

	/**
	 * @param updateView the updateView to set
	 */
	public void setUpdateView(String updateView) {
		this.updateView = updateView;
	}

	/**
	 * @return the cotizacionSelected
	 */
	public Cabecera getCotizacionSelected() {
		return cotizacionSelected;
	}

	/**
	 * @param cotizacionSelected the cotizacionSelected to set
	 */
	public void setCotizacionSelected(Cabecera cotizacionSelected) {
		this.cotizacionSelected = cotizacionSelected;
	}

	/**
	 * @return the tareaetiquetaList
	 */
	public List<Etiqueta> getTareaetiquetaList() {
		return tareaetiquetaList;
	}

	/**
	 * @param tareaetiquetaList the tareaetiquetaList to set
	 */
	public void setTareaetiquetaList(List<Etiqueta> tareaetiquetaList) {
		this.tareaetiquetaList = tareaetiquetaList;
	}

	/**
	 * @return the tareacabeceraSelected
	 */
	public Tareacabecera getTareacabeceraSelected() {
		return tareacabeceraSelected;
	}

	/**
	 * @param tareacabeceraSelected the tareacabeceraSelected to set
	 */
	public void setTareacabeceraSelected(Tareacabecera tareacabeceraSelected) {
		this.tareacabeceraSelected = tareacabeceraSelected;
	}
	
}
