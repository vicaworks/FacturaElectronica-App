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

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.FechaUtil;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.modelo.persistencia.Empresa;
import com.vcw.falecpv.core.modelo.persistencia.Usuario;
import com.vcw.falecpv.core.servicio.EmpresaServicio;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.falecpv.core.servicio.UsuarioServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.servicio.DatosEmpresaServicio;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@ViewScoped
public class EmpresaCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8788719067123516137L;
	
	@EJB
	private EmpresaServicio empresaServicio;
	
	@EJB
	private UsuarioServicio usuarioServicio;
	
	@EJB
	private EstablecimientoServicio establecimientoServicio;
	
	@EJB
	private DatosEmpresaServicio datosEmpresaServicio;
	
	private Empresa empresa;
	private Empresa empresaSelected;
	private boolean bandera;
	private UploadedFile file;

	/**
	 * 
	 */
	public EmpresaCtrl() {
		bandera = false;
	}
	
	@PostConstruct
	public void init() {
		try {
			establecimientoFacade(establecimientoServicio, false);
			// carga los datos iniciales si la empresa tine en S campo actualizardatos
			datosEmpresaServicio.cargarDatosEmpresaFacade(establecimientoMain.getEmpresa().getIdempresa());
			empresaSelected = new Empresa();
			refrescar();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	@Override
	public void limpiar() {
		super.limpiar();
	}

	@Override
	public void buscar() {
		super.buscar();
	}

	@Override
	public void refrescar() {
		try {
			empresa = null;
			consultarEmpresa();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	private void consultarEmpresa() throws DaoException {
		empresa = empresaServicio.consultarByPk(AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa());
		empresaSelected = empresaServicio.consultarByPk(AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa()); 
		bandera = empresa!=null;
	}
	
	public void editar() {
		try {
			empresaSelected = empresaServicio.consultarByPk(AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa());
			AppJsfUtil.showModalRender("dlgEmpresa", "formMain");
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	@Override
	public void eliminar() {
		super.eliminar();
	}

	@Override
	public void guardar() {
		try {
			
			if(empresaSelected.getArchivofirmaelectronica()!=null && empresaSelected.getFechavigencia()==null) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTE FECHA DE VIGENCIA");
				return;
			}
			
			if(empresaSelected.getArchivofirmaelectronica()!=null && (empresaSelected.getClavefirmaelectronica()==null || empresaSelected.getClavefirmaelectronica().length()==0)) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTE CLAVE DE FIRMA " + msg.getString("label.electronica"));
				return;
			}
			
			if(empresaSelected.getArchivofirmaelectronica()==null) {
				empresaSelected.setFechavigencia(null);
				empresaSelected.setClavefirmaelectronica(null);
			}
			
			if(empresaSelected.getFechavigencia()!=null && FechaUtil.comparaFechas(empresaSelected.getFechavigencia(),new Date())<0) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "LA FECHA DE VIGENCIA NO PUEDE SER MENOR QUE LA FECHA ACTUAL.");
				return;
			}
			
			empresaSelected.setUpdated(new Date());
			Usuario usuarioactual = usuarioServicio.getUsuarioDao().getByLogin(AppJsfUtil.getRemoteUser()); // Obtiene el usuario que inici� sesi�n
			empresaSelected.setIdusuario(usuarioactual.getIdusuario());
			empresaSelected = empresaServicio.guardar(empresaSelected);
			consultarEmpresa();
			AppJsfUtil.addInfoMessage("formMain","OK", "REGISTRO ALMACENADO CORRECTAMENTE.");
		} catch (Exception e) {
			e.printStackTrace();			
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
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
		        AppJsfUtil.ajaxUpdate("formMain:gridFE");
//		        AppJsfUtil.addInfoMessage("formMain", "OK", msg.getString("mensaje.archivofirmaelectronica"));
            }
		} catch (IOException e) {
			e.printStackTrace();			
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void limpiarArchivo() {
		try {
			empresaSelected.setArchivofirmaelectronica(null);
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Empresa getEmpresaSelected() {
		return empresaSelected;
	}

	public void setEmpresaSelected(Empresa empresaSelected) {
		this.empresaSelected = empresaSelected;
	}

	public boolean isBandera() {
		return bandera;
	}

	public void setBandera(boolean bandera) {
		this.bandera = bandera;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}
}
