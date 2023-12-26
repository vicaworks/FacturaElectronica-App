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
import org.primefaces.util.IOUtils;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.modelo.persistencia.Empresa;
import com.vcw.falecpv.core.modelo.persistencia.Usuario;
import com.vcw.falecpv.core.servicio.EmpresaServicio;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.falecpv.core.servicio.UsuarioServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.ctrl.common.MessageCommonCtrl.Message;
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
			empresaSelected = new Empresa();
			refrescar();
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
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
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
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
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	@Override
	public void eliminar() {
		super.eliminar();
	}

	@Override
	public void guardar() {
		try {
			
			empresaSelected.setUpdated(new Date());
			Usuario usuarioactual = usuarioServicio.getUsuarioDao().getByLogin(AppJsfUtil.getRemoteUser()); // Obtiene el usuario que inici� sesi�n
			empresaSelected.setIdusuario(usuarioactual.getIdusuario());
			empresaSelected = empresaServicio.guardar(empresaSelected);
			consultarEmpresa();
			getMessageCommonCtrl().crearMensaje("Ok", 
					"Registro almacenado correctamente", 
					Message.OK);
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
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
            }
		} catch (IOException e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	public void limpiarArchivo() {
		try {
			empresaSelected.setArchivofirmaelectronica(null);
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
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
