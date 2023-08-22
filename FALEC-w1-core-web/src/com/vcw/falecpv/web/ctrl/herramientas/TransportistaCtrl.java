/**
 * 
 */
package com.vcw.falecpv.web.ctrl.herramientas;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.modelo.persistencia.TipoIdentificacion;
import com.vcw.falecpv.core.modelo.persistencia.Transportista;
import com.vcw.falecpv.core.servicio.ClienteServicio;
import com.vcw.falecpv.core.servicio.TransportistaServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.ctrl.common.MessageCommonCtrl.Message;
import com.vcw.falecpv.web.ctrl.comprobantes.guiarem.GuiaRemFormCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@ViewScoped
public class TransportistaCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1303136713768047090L;
	
	@EJB
	private ClienteServicio clienteServicio;
	
	@EJB
	private TransportistaServicio transportistaServicio;
	
	private String callModule;
	private String viewUpdate;
	private Transportista transportistaSelected;
	private List<TipoIdentificacion> tipoIdentificacionList;
	private TransportistaMainCtrl transportistaMainCtrl;

	/**
	 * 
	 */
	public TransportistaCtrl() {
	}
	
	@PostConstruct
	private void init() {
		try {
			consultarTipoIdentificacion();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void consultarTipoIdentificacion()throws DaoException{
		tipoIdentificacionList = null;
		tipoIdentificacionList = clienteServicio.getByProveedor();
	}
	
	@Override
	public void guardar() {
		try {
			
			if(transportistaServicio.existeCampo(transportistaSelected.getIdtransportista(), transportistaSelected.getEmpresa().getIdempresa(), "RS", transportistaSelected.getRazonsocial())) {
				AppJsfUtil.addErrorMessage("frmTransportista","ERROR" ,msg.getString("error.transportista.razonsocial.yaexiste"));
				AppJsfUtil.addErrorMessage("frmTransportista:intTransportistaRazonSocial","Ya existe");
				return;
			}
			
			if(transportistaServicio.existeCampo(transportistaSelected.getIdtransportista(), transportistaSelected.getEmpresa().getIdempresa(), "ID", transportistaSelected.getIdentificacion())) {
				AppJsfUtil.addErrorMessage("frmTransportista","ERROR" , msg.getString("error.transportista.identificacion.yaexiste"));
				AppJsfUtil.addErrorMessage("frmTransportista:intTransportistaIentifiaccion","Ya existe");
				return;
			}
			
			transportistaSelected.setUpdated(new Date());
			transportistaSelected.setIdusuario(AppJsfUtil.getUsuario().getIdentificacion());
			transportistaSelected = transportistaServicio.guardar(transportistaSelected, AppJsfUtil.getEstablecimiento().getIdestablecimiento());
			
			switch (callModule) {
			case "GUIA_REMISION":
				
				GuiaRemFormCtrl guiaRemFormCtrl = (GuiaRemFormCtrl) AppJsfUtil.getManagedBean("guiaRemFormCtrl");
				guiaRemFormCtrl.consultarTransportista();
				guiaRemFormCtrl.getGuiaRemisionSelected().setTransportista(transportistaSelected);
				guiaRemFormCtrl.cambioTransportista();
				
				AppJsfUtil.hideModal("dlgTransportista");
				break;
			case "TRANSPORTISTA":
				transportistaMainCtrl.consultar();
				AppJsfUtil.addInfoMessage("frmTransportista", "OK", msg.getString("mensaje.guardado.correctamente"));
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

	public void nuevoEditar(String idTransportista) {
		try {
			
			if(idTransportista!=null) {
				transportistaSelected = transportistaServicio.consultarByPk(idTransportista);
			}
			
			if(transportistaSelected==null) {
				nuevoTransportista();
			}
			
			AppJsfUtil.showModalRender("dlgTransportista", "frmTransportista");
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	
	public void nuevoAction() {
		try {
			
			nuevoTransportista();
			
			AppJsfUtil.showModalRender("dlgTransportista", "frmTransportista");
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	private void nuevoTransportista() {
		transportistaSelected = new Transportista();
		transportistaSelected.setEmpresa(AppJsfUtil.getEstablecimiento().getEmpresa());
		transportistaSelected.setEstado(EstadoRegistroEnum.ACTIVO.getInicial());
	}
	
	@Override
	public void nuevo() {
		try {
			if(transportistaSelected==null) {
				nuevoTransportista();
			}
			
			AppJsfUtil.showModalRender("dlgTransportista", "frmTransportista");
			
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
			
			AppJsfUtil.showModalRender("dlgTransportista", "frmTransportista");
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
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
	 * @return the viewUpdate
	 */
	public String getViewUpdate() {
		return viewUpdate;
	}

	/**
	 * @param viewUpdate the viewUpdate to set
	 */
	public void setViewUpdate(String viewUpdate) {
		this.viewUpdate = viewUpdate;
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

	/**
	 * @return the transportistaMainCtrl
	 */
	public TransportistaMainCtrl getTransportistaMainCtrl() {
		return transportistaMainCtrl;
	}

	/**
	 * @param transportistaMainCtrl the transportistaMainCtrl to set
	 */
	public void setTransportistaMainCtrl(TransportistaMainCtrl transportistaMainCtrl) {
		this.transportistaMainCtrl = transportistaMainCtrl;
	}

}
