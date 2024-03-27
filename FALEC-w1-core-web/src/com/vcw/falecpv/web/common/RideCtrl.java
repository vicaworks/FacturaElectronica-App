/**
 * 
 */
package com.vcw.falecpv.web.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.NoResultException;
import javax.resource.ResourceException;
import javax.servlet.ServletContext;
import javax.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;
import org.primefaces.model.StreamedContent;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.TextoUtil;
import com.servitec.common.util.exceptions.ParametroRequeridoException;
import com.vcw.falecpv.core.exception.RideException;
import com.vcw.falecpv.web.ctrl.common.MessageCommonCtrl.Message;
import com.vcw.falecpv.web.servicio.RideServicio;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@ViewScoped
public class RideCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1064305110579652914L;
	
	@EJB
	private RideServicio rideServicio;

	private StreamedContent rideFile;
	private String idCabecera;
	private String downloadFileName;
	private String url;
	private String urlContexto;
	private String inicialComprobante;
	private String numComprobante;
	
	/**
	 * 
	 */
	public RideCtrl() {
	}

	public void showRide() {
			
		try {
			
			generateRide(null,true);
			
		} catch (NoResultException | ResourceException |  RideException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
			
	}
	
	public void showRidePuntoVenta() {
		
		try {
			
			generateRide("PUNTO_VENTA",false);
			System.out.println(getUrlContexto());
			AppJsfUtil.executeJavaScript("printJS('" + getUrlContexto() +"')");
			
		} catch (NoResultException | ResourceException |  RideException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
			
	}
	
	private void generateRide(String subtipo,boolean showModal) throws NoResultException, 
		NumberFormatException, IOException, 
		RideException, DaoException, JAXBException, 
		ResourceException, ParametroRequeridoException {
		// crear el file en el directorio temp
		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		downloadFileName = inicialComprobante + numComprobante + ".pdf";
		String pathFile = servletContext.getRealPath("temp").concat("/").concat(downloadFileName);
		FileOutputStream output = new FileOutputStream(new File(pathFile));
		IOUtils.write(rideServicio.generarRideFacade(idCabecera,subtipo), output);
		setUrl("../../temp/" + downloadFileName);
		setUrlContexto(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath().concat("/temp/").concat(downloadFileName));
		if(showModal) {
			AppJsfUtil.showModalRender("dlgRide", "formRide");
		}
	}
	
	public void showCotizacion() {
		
		try {
			
			// crear el file en el directorio temp
			ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
			downloadFileName = inicialComprobante + numComprobante + ".pdf";
			String pathFile = servletContext.getRealPath("temp").concat("/").concat(downloadFileName);
			FileOutputStream output = new FileOutputStream(new File(pathFile));
			IOUtils.write(rideServicio.generarCotizacionFacade(idCabecera), output);
			setUrl("../../temp/" + downloadFileName);
			AppJsfUtil.showModalRender("dlgRide", "formRide");
			
		} catch (NoResultException | ResourceException |  RideException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
			
	}

	/**
	 * @return the rideFile
	 */
	public StreamedContent getRideFile() {
		return rideFile;
	}


	/**
	 * @param rideFile the rideFile to set
	 */
	public void setRideFile(StreamedContent rideFile) {
		this.rideFile = rideFile;
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
	 * @return the downloadFileName
	 */
	public String getDownloadFileName() {
		return downloadFileName;
	}


	/**
	 * @param downloadFileName the downloadFileName to set
	 */
	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}


	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}


	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}


	/**
	 * @return the inicialComprobante
	 */
	public String getInicialComprobante() {
		return inicialComprobante;
	}


	/**
	 * @param inicialComprobante the inicialComprobante to set
	 */
	public void setInicialComprobante(String inicialComprobante) {
		this.inicialComprobante = inicialComprobante;
	}


	/**
	 * @return the numComprobante
	 */
	public String getNumComprobante() {
		return numComprobante;
	}


	/**
	 * @param numComprobante the numComprobante to set
	 */
	public void setNumComprobante(String numComprobante) {
		this.numComprobante = numComprobante;
	}

	/**
	 * @return the urlContexto
	 */
	public String getUrlContexto() {
		return urlContexto;
	}

	/**
	 * @param urlContexto the urlContexto to set
	 */
	public void setUrlContexto(String urlContexto) {
		this.urlContexto = urlContexto;
	}
	
}
