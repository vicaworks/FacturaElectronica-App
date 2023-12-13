/**
 * 
 */
package com.vcw.falecpv.web.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.FechaUtil;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Infoadicional;
import com.vcw.falecpv.core.servicio.CabeceraServicio;
import com.vcw.falecpv.core.servicio.CabeceraadjuntoServicio;
import com.vcw.falecpv.core.servicio.InfoadicionalServicio;
import com.vcw.falecpv.core.util.HtmlUtil;
import com.vcw.falecpv.web.ctrl.adquisicion.RetencionMainCtrl;
import com.vcw.falecpv.web.ctrl.common.MessageCommonCtrl.Message;
import com.vcw.falecpv.web.ctrl.comprobantes.guiarem.GuiaRemCtrl;
import com.vcw.falecpv.web.ctrl.comprobantes.liqcompra.LiqCompraCtrl;
import com.vcw.falecpv.web.ctrl.comprobantes.nc.CompNcCtrl;
import com.vcw.falecpv.web.ctrl.comprobantes.nd.NotaDebitoCtrl;
import com.vcw.falecpv.web.ctrl.facturacion.FacEmitidaCtrl;
import com.vcw.falecpv.web.servicio.SriDispacher;
import com.vcw.falecpv.web.servicio.emailcomprobante.EmailComprobanteServicio;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@ViewScoped
public class EnviarDocCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7974992544036042307L;
	
	@EJB
	private CabeceraServicio cabeceraServicio;
	@EJB
	private InfoadicionalServicio infoadicionalServicio;
	@EJB
	private SriDispacher sriDispacher;
	@EJB
	private EmailComprobanteServicio emailComprobanteServicio;
	@EJB
	private CabeceraadjuntoServicio cabeceraadjuntoServicio;
	
	private String idCabecera;
	private Cabecera cabeceraSelected;
	private String callModule;
	private String callForm;
	private String updateView;
	private List<Infoadicional> infoadicionalList;
	private String correoSelected;
	private Map<String, byte[]> adjuntosMap;
	private String subject;
	private String estadoComprobante;
	private String to;
	private String cc;
	private String contenido;
	
	

	/**
	 * 
	 */
	public EnviarDocCtrl() {
	}
	
	public void cargarFormulario() {
		try {
			to = null;
			cc = null;
			contenido = null;
			adjuntosMap = new HashMap<>();
			consultar();
			AppJsfUtil.showModalRender("dlEnvioDoc", "formEnvioDoc");
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	private void consultar()throws DaoException{
		cabeceraSelected = null;
		cabeceraSelected = cabeceraServicio.consultarByPk(idCabecera);
		infoadicionalList = null;
		infoadicionalList = infoadicionalServicio.getInfoadicionalDao().getByIdCabecera(idCabecera);
		// adjuntos
		adjuntosMap = cabeceraadjuntoServicio.getByCabecera(idCabecera);
		// cliente
		if(!cabeceraSelected.getTipocomprobante().getIdentificador().equals("06")) {
			if(cabeceraSelected.getCliente()!=null && cabeceraSelected.getCliente().getCorreoelectronico()!=null) {
				to = cabeceraSelected.getCliente().getCorreoelectronico();
			}
		}else {
			if(cabeceraSelected.getTransportista()!=null && cabeceraSelected.getTransportista().getEmail()!=null) {
				to = cabeceraSelected.getTransportista().getEmail();
			}
		}
		// subject por defecto
			
		subject = msg.getString("label.comprobanteelectronico.subject", 
				new Object[] {
						cabeceraSelected.getTipocomprobante().getComprobante(),
						getFormatoNumDocumento(cabeceraSelected.getNumdocumento()),
						FechaUtil.formatoFecha(cabeceraSelected.getFechaemision())
						}
		);
	}
	
	
	
	public void enviarCorreo() {
		try {
			if(to==null) {
				getMessageCommonCtrl().crearMensaje("Error", 
						"No existe emails", 
						Message.ERROR);
				return;
			}
			List<String> correoList = new ArrayList<>();
			
			String[] emails = to.split(",");
			for (String e : emails) {
				if(!correoList.contains(e)) {
					correoList.add(e);
				}				
			}
			
			List<String> correoCcList = new ArrayList<>();
			if(cc != null && cc.trim().length()>0) {
				emails = cc.split(",");
				for (String e : emails) {
					if(!correoCcList.contains(e)) {						
						if(!correoCcList.contains(e)) {
							correoCcList.add(e);							
						}						
					}					
				}				
			}
			
			contenido = contenido!=null?HtmlUtil.sustituirCaracteres(contenido):null;
			
			// guardar adjuntos
			if(adjuntosMap != null && !adjuntosMap.isEmpty()) {
				cabeceraadjuntoServicio.guardarAdjuntos(
						cabeceraSelected.getIdcabecera(), 
						cabeceraSelected.getEstablecimiento().getIdestablecimiento(), 
						adjuntosMap);
			}
			
			emailComprobanteServicio.enviarComprobanteFacade(null, null, adjuntosMap, idCabecera, null, subject, contenido, correoList,false);
			actualizarPantalla();
			getMessageCommonCtrl().crearMensaje("Ok", 
					"Enviado correctamente", 
					Message.INFO);
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	
	public void handleUpload(FileUploadEvent event) throws IOException {
		
		if(adjuntosMap.containsKey(event.getFile().getFileName())) {
			getMessageCommonCtrl().crearMensaje("Error", 
					"Ya existe el archivo : " + event.getFile().getFileName(), 
					Message.ERROR);
			return;			
		}
		// agrega el file a la lista
		
		adjuntosMap.put(event.getFile().getFileName(), event.getFile().getContent());		
	}
	
	public void eliminarAdjuntos(String nombre) {
		try {
			if(!adjuntosMap.isEmpty()) {
				adjuntosMap.remove(nombre);				
			}
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	
	public void firmarEnviarCorreo() {
		try {
			
			Cabecera c = cabeceraServicio.consultarByPk(idCabecera);
			c.setIdUsurioTransaccion(AppJsfUtil.getUsuario().getIdusuario());
			sriDispacher.queue_comprobanteSriDispacher(c);
			actualizarPantalla();
			getMessageCommonCtrl().crearMensaje("Ok", 
					"Documento firmado y enviado correctamente", 
					Message.INFO);
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	private void actualizarPantalla() throws DaoException {
		switch (callForm) {
		case "FACTURA":
			FacEmitidaCtrl facEmitidaCtrl = (FacEmitidaCtrl)AppJsfUtil.getManagedBean("facEmitidaCtrl");
			facEmitidaCtrl.consultar();
			break;
			
		case "RETENCION":
			RetencionMainCtrl retencionMainCtrl = (RetencionMainCtrl)AppJsfUtil.getManagedBean("retencionMainCtrl");
			retencionMainCtrl.consultarRetenciones();
			break;
		
		case "NOTA_CREDITO":
			CompNcCtrl compNcCtrl = (CompNcCtrl)AppJsfUtil.getManagedBean("compNcCtrl");
			compNcCtrl.consultar();
			break;
			
		case "NOTA_DEBITO":
			NotaDebitoCtrl notaDebitoCtrl = (NotaDebitoCtrl)AppJsfUtil.getManagedBean("notaDebitoCtrl");
			notaDebitoCtrl.consultar();
			break;	
		
		case "GUIA_REMISION":
			GuiaRemCtrl guiaRemCtrl = (GuiaRemCtrl)AppJsfUtil.getManagedBean("guiaRemCtrl");
			guiaRemCtrl.consultar();
			break;
		
		case "LIQ_COMPRA":
			LiqCompraCtrl liqCompraCtrl = (LiqCompraCtrl)AppJsfUtil.getManagedBean("liqCompraCtrl");
			liqCompraCtrl.consultar();
			break;
			
		default:
			break;
		}
	}

	/**
	 * @return the idCabecera
	 */
	public String getIdCabecera() {
		return idCabecera;
	}

	/**
	 * @param idCabecera the idCabecera to set
	 */
	public void setIdCabecera(String idCabecera) {
		this.idCabecera = idCabecera;
	}

	/**
	 * @return the cabeceraSelected
	 */
	public Cabecera getCabeceraSelected() {
		return cabeceraSelected;
	}

	/**
	 * @param cabeceraSelected the cabeceraSelected to set
	 */
	public void setCabeceraSelected(Cabecera cabeceraSelected) {
		this.cabeceraSelected = cabeceraSelected;
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
	 * @return the infoadicionalList
	 */
	public List<Infoadicional> getInfoadicionalList() {
		return infoadicionalList;
	}

	/**
	 * @param infoadicionalList the infoadicionalList to set
	 */
	public void setInfoadicionalList(List<Infoadicional> infoadicionalList) {
		this.infoadicionalList = infoadicionalList;
	}

	/**
	 * @return the correoSelected
	 */
	public String getCorreoSelected() {
		return correoSelected;
	}

	/**
	 * @param correoSelected the correoSelected to set
	 */
	public void setCorreoSelected(String correoSelected) {
		this.correoSelected = correoSelected;
	}

	/**
	 * @return the adjuntosMap
	 */
	public Map<String, byte[]> getAdjuntosMap() {
		return adjuntosMap;
	}

	/**
	 * @param adjuntosMap the adjuntosMap to set
	 */
	public void setAdjuntosMap(Map<String, byte[]> adjuntosMap) {
		this.adjuntosMap = adjuntosMap;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	/**
	 * @return the estadoComprobante
	 */
	public String getEstadoComprobante() {
		return estadoComprobante;
	}

	/**
	 * @param estadoComprobante the estadoComprobante to set
	 */
	public void setEstadoComprobante(String estadoComprobante) {
		this.estadoComprobante = estadoComprobante;
	}

	/**
	 * @return the to
	 */
	public String getTo() {
		return to;
	}

	/**
	 * @param to the to to set
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * @return the cc
	 */
	public String getCc() {
		return cc;
	}

	/**
	 * @param cc the cc to set
	 */
	public void setCc(String cc) {
		this.cc = cc;
	}

	/**
	 * @return the contenido
	 */
	public String getContenido() {
		return contenido;
	}

	/**
	 * @param contenido the contenido to set
	 */
	public void setContenido(String contenido) {
		this.contenido = contenido;
	}
	
}
