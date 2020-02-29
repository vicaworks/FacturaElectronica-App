/**
 * 
 */
package com.servitec.common.jsf.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FileTransferClient;
import com.servitec.common.jsf.FacesUtil;
import com.servitec.common.util.FileUtil;
import com.servitec.common.util.datamodel.CuentaFtp;

/**
 * @author cvillarreal
 *
 */
public abstract class BaseFtpController {

	/**
	 * 
	 */
	public BaseFtpController() {
	}

	public abstract CuentaFtp getCuentaFtp();
	public abstract String getNombreArchivo();
	public abstract String getSubDirectorio();
	
	
	@SuppressWarnings("unused")
	public void descargarArchivo() {

		FileTransferClient ftp = new FileTransferClient();
		File fl = null;
		int read = 0;
		byte[] bytes = new byte[4096];
		
		HttpServletResponse response = FacesUtil.getServletResponse();
		OutputStream os = null;
		String mimeType = FacesUtil.getServletContext().getMimeType(getNombreArchivo());
		response.setContentType(FileUtil.getMimeType(getNombreArchivo()));
		response.setHeader("Content-Disposition", "attachment;filename=\""
				+ getNombreArchivo() + "\"");
		response.setDateHeader("Expires", 0);

		
		
		try {

			os = response.getOutputStream();
			ftp.setRemoteHost(getCuentaFtp().getFtpServer());
			ftp.setUserName(getCuentaFtp().getFtpUser());
			ftp.setPassword(getCuentaFtp().getFtpPassword());
			ftp.connect();
			if (getSubDirectorio()!=null) {
				
				ftp.changeDirectory(getSubDirectorio());
				
			}

			if (!ftp.exists(getNombreArchivo())) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				ftp.disconnect();
				return;
			}

			response.setContentLength((int) ftp.getSize(getNombreArchivo()));
			os.write(ftp.downloadByteArray(getNombreArchivo()));
			os.flush();
			os.close();
			ftp.disconnect();
			FacesContext.getCurrentInstance().responseComplete();

		} catch (FTPException e) {
			// al seteo de usurio
			e.printStackTrace();
		} catch (IOException e) {
			// No se conecta
			e.printStackTrace();
		}

	}


}
