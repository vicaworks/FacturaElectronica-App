/**
 * 
 */
package com.vcw.falecpv.web.ctrl.configuracion;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import com.vcw.falecpv.core.servicio.EmpresaServicio;
import com.vcw.falecpv.core.servicio.UsuarioServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;
import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.modelo.persistencia.Empresa;
import com.vcw.falecpv.core.modelo.persistencia.Usuario;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.primefaces.shaded.commons.io.IOUtils;

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
	
	private List<Empresa> empresaList;
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
		empresaSelected = new Empresa();
		refrescar();
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
			empresaList = null;
			consultarEmpresa();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	private void consultarEmpresa() throws DaoException {
		empresaList = empresaServicio.getEmpresaDao().getEmpresaActual();
		empresaList.clear(); // simular que no hay empresas en bdd
		if(!empresaList.isEmpty()) {
			bandera = true;
			empresa = empresaList.get(0);
		}
	}
	
	public void editar() {
		try {
			empresaSelected = empresaServicio.getEmpresaDao().getEmpresaActual().get(0);
			AppJsfUtil.showModalRender("dlgEmpresa", "frmEmpresa");
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMainEmpresa", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
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
			Usuario usuarioactual = usuarioServicio.getUsuarioDao().getByLogin(AppJsfUtil.getRemoteUser()); // Obtiene el usuario que inició sesión
			empresaSelected.setIdusuario(usuarioactual.getIdusuario());
			empresaSelected = empresaServicio.guardar(empresaSelected);
			consultarEmpresa();
			AppJsfUtil.addInfoMessage("frmEmpresa","OK", "REGISTRO ALMACENADO CORRECTAMENTE.");
			ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
			context.redirect("/falecpv/pages/configuracion/empresa.jsf");
		} catch (Exception e) {
			e.printStackTrace();			
			AppJsfUtil.addErrorMessage("frmEmpresa", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void upload(FileUploadEvent event)
	{
		file = event.getFile();
		try {
			if(file != null)
            {
				byte[] bytes;
		        bytes = IOUtils.toByteArray(file.getInputstream());
		        empresaSelected.setArchivofirmaelectronica(bytes);
            }
		} catch (IOException e) {
			e.printStackTrace();			
			AppJsfUtil.addErrorMessage("formMainEmpresa", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public List<Empresa> getEmpresaList() {
		return empresaList;
	}

	public void setEmpresaList(List<Empresa> empresaList) {
		this.empresaList = empresaList;
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
