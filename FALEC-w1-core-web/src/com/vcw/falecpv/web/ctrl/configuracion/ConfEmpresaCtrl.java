/**
 * 
 */
package com.vcw.falecpv.web.ctrl.configuracion;

import java.io.IOException;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.shaded.commons.io.IOUtils;

import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.FechaUtil;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.modelo.persistencia.Empresa;
import com.vcw.falecpv.core.modelo.persistencia.Establecimiento;
import com.vcw.falecpv.core.modelo.persistencia.Usuario;
import com.vcw.falecpv.core.servicio.EmpresaServicio;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.falecpv.core.servicio.UsuarioServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@ViewScoped
public class ConfEmpresaCtrl extends BaseCtrl {
	
	@EJB
	private EmpresaServicio empresaServicio;
	
	@EJB
	private EstablecimientoServicio establecimientoServicio;
	
	@EJB
	private UsuarioServicio usuarioServicio;

	/**
	 * 
	 */
	private static final long serialVersionUID = 6793595774124920786L;
	
	private Establecimiento establecimientoSelected;
	private Empresa empresaSelected;
	private UploadedFile file;
	
	

	/**
	 * 
	 */
	public ConfEmpresaCtrl() {
	}
	
	@PostConstruct
	private void init() {
		try {
			establecimientoFacade(establecimientoServicio, false);
			empresaSelected = empresaServicio.consultarByPk(establecimientoMain.getEmpresa().getIdempresa());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void upload(FileUploadEvent event)
	{
		file = event.getFile();
		try {
			if(file != null)
            {
				byte[] bytes;
		        bytes = IOUtils.toByteArray(file.getInputStream());
		        empresaSelected.setNombrearchivo(file.getFileName());
		        empresaSelected.setArchivofirmaelectronica(bytes);
		        AppJsfUtil.ajaxUpdate("fsvConfiguracion:TabConfEmpresa:frmCertificado");
            }
		} catch (IOException e) {
			e.printStackTrace();			
			AppJsfUtil.addErrorMessage("frmCertificado", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void limpiarArchivo() {
		try {
			empresaSelected.setArchivofirmaelectronica(null);
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmCertificado", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void guardarArchivoFirma() {
		try {
			
			if(empresaSelected.getArchivofirmaelectronica()==null) {
				AppJsfUtil.addErrorMessage("fsvConfiguracion:TabConfEmpresa:frmCertificado:intNameFileFirmaDigital", "ERROR", "NO EXISTE ARCHIVO, DEBE SELECCIONAR.");
				return;
			}
			
			if(FechaUtil.comparaFechas(empresaSelected.getFechaexpiracion(),new Date())<0) {
				AppJsfUtil.addErrorMessage("frmCertificado", "ERROR", msg.getString("error.comprarfecha"));
				return;
			}
			
			if(FechaUtil.comparaFechas(empresaSelected.getFechaexpiracion(),empresaSelected.getFechaemision())<0) {
				AppJsfUtil.addErrorMessage("frmCertificado", "ERROR", msg.getString("error.comprarfecha2"));
				return;
			}
			
			empresaSelected.setUpdated(new Date());
			Usuario usuarioactual = usuarioServicio.getUsuarioDao().getByLogin(AppJsfUtil.getRemoteUser()); 
			empresaSelected.setIdusuario(usuarioactual.getIdusuario());
			empresaSelected = empresaServicio.guardar(empresaSelected);
			AppJsfUtil.addInfoMessage("frmCertificado","OK", "CERTIFICADO ALMACENADO CORRECTAMENTE.");
		} catch (Exception e) {
			e.printStackTrace();			
			AppJsfUtil.addErrorMessage("frmCertificado", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	/**
	 * @return the empresaSelected
	 */
	public Empresa getEmpresaSelected() {
		return empresaSelected;
	}

	/**
	 * @param empresaSelected the empresaSelected to set
	 */
	public void setEmpresaSelected(Empresa empresaSelected) {
		this.empresaSelected = empresaSelected;
	}

	/**
	 * @return the establecimientoSelected
	 */
	public Establecimiento getEstablecimientoSelected() {
		return establecimientoSelected;
	}

	/**
	 * @param establecimientoSelected the establecimientoSelected to set
	 */
	public void setEstablecimientoSelected(Establecimiento establecimientoSelected) {
		this.establecimientoSelected = establecimientoSelected;
	}

	/**
	 * @return the file
	 */
	public UploadedFile getFile() {
		return file;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(UploadedFile file) {
		this.file = file;
	}

}
