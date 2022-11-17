/**
 * 
 */
package com.vcw.falecpv.web.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.FechaUtil;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Infoadicional;
import com.vcw.falecpv.core.servicio.CabeceraServicio;
import com.vcw.falecpv.core.servicio.InfoadicionalServicio;
import com.vcw.falecpv.web.ctrl.adquisicion.RetencionMainCtrl;
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
	
	private String idCabecera;
	private Cabecera cabeceraSelected;
	private String callModule;
	private String callForm;
	private String updateView;
	private List<String> correoList;
	private List<Infoadicional> infoadicionalList;
	private String correoSelected;
	private Map<String, byte[]> adjuntosMap;
	private String subject;
	private List<SelectItem> emailList;
	private String estadoComprobante;
	
	

	/**
	 * 
	 */
	public EnviarDocCtrl() {
	}
	
	public void cargarFormulario() {
		try {
			correoList = new ArrayList<>();
			emailList = new ArrayList<>();
			consultar();
			AppJsfUtil.showModalRender("dlEnvioDoc", "formEnvioDoc");
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage(callForm, "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	private void consultar()throws DaoException{
		cabeceraSelected = null;
		cabeceraSelected = cabeceraServicio.consultarByPk(idCabecera);
		infoadicionalList = null;
		infoadicionalList = infoadicionalServicio.getInfoadicionalDao().getByIdCabecera(idCabecera);
		// cliente
		if(cabeceraSelected.getCliente()!=null && cabeceraSelected.getCliente().getCorreoelectronico()!=null) {
			agregarCorreoList(cabeceraSelected.getCliente().getCorreoelectronico());
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
	
	private void agregarCorreoList(String emails) {
		String mails[] = emails.split(",");
		for (int i = 0; i < mails.length; i++) {
			String valor = mails[i];
			if(correoList.stream().filter(x->x.equals(valor)).count()==0) {
				correoList.add(valor);
			}
		}
	}

	public void eliminar(Integer idx) {
		try {
			emailList.remove(idx.intValue());
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formEnvioDoc", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	@Override
	public void nuevo() {
		try {
			correoList.add("");
			
			int cont = 0; 
			for (SelectItem i : emailList) {
				i.setValue("M" + (cont++));
			}
			emailList.add(new SelectItem("M" + (emailList.size()+1), null));
			AppJsfUtil.executeJavaScript("PrimeFaces.focus('formEnvioDoc:pvEnvioCorreosDT:" + (correoList.size()-1) + ":intEnCorreo');");
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formEnvioDoc", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	public void enviarCorreo() {
		try {
			if(emailList.size()==0) {
				AppJsfUtil.addErrorMessage("formEnvioDoc", "ERROR", "NO EXISTE EMAILS.");
				return;
			}
			correoList = new ArrayList<>();
			for (SelectItem i : emailList) {
				String[] emails = i.getLabel().split(",");
				for (String e : emails) {
					if(!correoList.contains(e)) {
						correoList.add(e);
					}
					
				}
				
			}
			emailComprobanteServicio.enviarComprobanteFacade(null, null, adjuntosMap, idCabecera, null, subject, null, correoList,false);
			actualizarPantalla();
			AppJsfUtil.addInfoMessage("formEnvioDoc", "OK", "ENVIADO CORRECTAMENTE.");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formEnvioDoc", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void firmarEnviarCorreo() {
		try {
			
			Cabecera c = cabeceraServicio.consultarByPk(idCabecera);
			c.setIdUsurioTransaccion(AppJsfUtil.getUsuario().getIdusuario());
			sriDispacher.queue_comprobanteSriDispacher(c);
			actualizarPantalla();
			AppJsfUtil.addInfoMessage("formEnvioDoc", "OK", "DOCUMENTO FIRMADO Y ENVIADO CORRECTAMENTE.");
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formEnvioDoc", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
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
	 * @return the correoList
	 */
	public List<String> getCorreoList() {
		return correoList;
	}

	/**
	 * @param correoList the correoList to set
	 */
	public void setCorreoList(List<String> correoList) {
		this.correoList = correoList;
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
	 * @return the emailList
	 */
	public List<SelectItem> getEmailList() {
		return emailList;
	}

	/**
	 * @param emailList the emailList to set
	 */
	public void setEmailList(List<SelectItem> emailList) {
		this.emailList = emailList;
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

}
