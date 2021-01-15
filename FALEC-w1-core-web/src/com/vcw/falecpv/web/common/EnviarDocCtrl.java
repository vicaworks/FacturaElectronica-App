/**
 * 
 */
package com.vcw.falecpv.web.common;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Infoadicional;
import com.vcw.falecpv.core.servicio.CabeceraServicio;
import com.vcw.falecpv.core.servicio.InfoadicionalServicio;
import com.vcw.falecpv.web.servicio.SriDispacher;
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
	
	private String idCabecera;
	private Cabecera cabeceraSelected;
	private String callModule;
	private String callForm;
	private String updateView;
	private List<String> correoList;
	private List<Infoadicional> infoadicionalList;
	private String correoSelected;
	

	/**
	 * 
	 */
	public EnviarDocCtrl() {
	}
	
	public void cargarFormulario() {
		try {
			correoList = new ArrayList<>();
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
		infoadicionalList.stream().forEach(i->{
			if(i.getValor().contains("@")) {
				agregarCorreoList(i.getValor());
			}
		});
		// cliente
		if(cabeceraSelected.getCliente()!=null && cabeceraSelected.getCliente().getCorreoelectronico()!=null) {
			agregarCorreoList(cabeceraSelected.getCliente().getCorreoelectronico());
		}
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

	@Override
	public void eliminar() {
		try {
			correoList.remove(correoSelected);
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formEnvioDoc", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	@Override
	public void nuevo() {
		try {
			correoList.add("");
			AppJsfUtil.executeJavaScript("PrimeFaces.focus('formEnvioDoc:pvEnvioCorreosDT:" + (correoList.size()-1) + ":intEnCorreo');");
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formEnvioDoc", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	public void enviarCorreo() {
		try {
			if(correoList.size()==0) {
				AppJsfUtil.addErrorMessage("formEnvioDoc", "ERROR", "NO EXISTE EMAILS.");
			}
			
			AppJsfUtil.addInfoMessage("formEnvioDoc", "OK", "ENVIADO CORRECTAMENTE.");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formEnvioDoc", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void firmarEnviarCorreo() {
		try {
			
			sriDispacher.queue_comprobanteSriDispacher(cabeceraServicio.consultarByPk(idCabecera));
			
			AppJsfUtil.addInfoMessage("formEnvioDoc", "OK", "DOCUMENTO FIRMADO Y ENVIADO CORRECTAMENTE.");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formEnvioDoc", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
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

}
