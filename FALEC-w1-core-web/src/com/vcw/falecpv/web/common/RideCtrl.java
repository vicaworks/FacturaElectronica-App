/**
 * 
 */
package com.vcw.falecpv.web.common;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.commons.io.IOUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.servitec.common.jsf.FacesUtil;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.TextoUtil;
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

	private StreamedContent rideFile;
	private String idCabecera;
	private String downloadFileName;
	
	/**
	 * 
	 */
	public RideCtrl() {
	}

	
	private void cargarFile() throws FileNotFoundException,IOException{
		String path = FacesUtil.getServletContext().getRealPath(
				AppConfiguracion.getString("dir.base.reporte") + "3768600815.pdf");
		
		File pdf = new File(path);
		byte[] pdfFile = IOUtils.toByteArray(new FileInputStream(pdf));
		rideFile = DefaultStreamedContent.builder().contentType("application/pdf").stream(() -> new ByteArrayInputStream(pdfFile)).build();
		
		
		downloadFileName = "3768600815.pdf";
	}

	public void showRide() {
		try {
			cargarFile();
			Thread.sleep(2000);
			AppJsfUtil.showModalRender("dlgRide", "formRide");
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formRide", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
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
	
	
}
