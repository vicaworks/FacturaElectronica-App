/**
 * 
 */
package com.vcw.falecpv.web.ctrl.usuario;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.modelo.persistencia.Establecimiento;
import com.vcw.falecpv.core.modelo.persistencia.Usuario;
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
public class UsuarioCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8543804720463095574L;
	
	@EJB
	private UsuarioServicio usuarioServicio;
	
	@EJB
	private EstablecimientoServicio establecimientoServicio;
	
	
	private List<Usuario> usuarioList;
	private String estadoRegBusqueda;
	private Usuario usuarioSelected;
	private List<Establecimiento> establecimientoList;
	
	/**
	 * 
	 */
	public UsuarioCtrl() {
	}


	@Override
	public void limpiar() {
		usuarioList = null;
		estadoRegBusqueda = "A";
	}



	@Override
	public void refrescar() {
		try {
			usuarioList = null;
			consultarUsuario();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	private void consultarUsuario() throws DaoException {
		usuarioList = usuarioServicio.getUsuarioDao().getByEstado(EstadoRegistroEnum.getByInicial(estadoRegBusqueda));
	}
	
	private void consultarEstablecimientos() throws DaoException {
		establecimientoList = null;
		establecimientoList = establecimientoServicio.getEstablecimientoDao().getByEstado(EstadoRegistroEnum.ACTIVO);
	}

	@Override
	public void eliminar() {
		super.eliminar();
	}

	@Override
	public void guardar() {
		try {
			
			if(usuarioSelected.isActualizarCredenciales()) {
				if(!usuarioSelected.getClave().equals(usuarioSelected.getClave2())) {
					AppJsfUtil.addErrorMessage("frmUsuario", "ERROR","LAS CREDENCIALES NO COINCIDEN.");
					AppJsfUtil.addErrorMessage("frmUsuario:intClave1","ERROR CONFIRMACION (LAS CREDENCIALES NO COINCIDEN).");
					return;
				}
			}
			
			usuarioSelected.setUpdated(new Date());			
			usuarioServicio.guardar(usuarioSelected);
			usuarioList = null;
			consultarUsuario();
			AppJsfUtil.addInfoMessage("frmUsuario","OK", "REGISTRO ALMACENADO CORRECTAMENTE.");
		} catch (Exception e) {
			e.printStackTrace();			
			AppJsfUtil.addErrorMessage("frmUsuario", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	public void nuevo() {
		try {
			usuarioSelected = new Usuario();
			usuarioSelected.setEmpresa(AppJsfUtil.getEstablecimiento().getEmpresa());
			usuarioSelected.setAdministrador("N");
			usuarioSelected.setActualizarCredenciales(true);
			usuarioSelected.setEstado("A");
			consultarEstablecimientos();
			AppJsfUtil.showModalRender("dlgUsuario", "frmUsuario");
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void editar() {
		try {
			usuarioSelected.setEmpresa(usuarioSelected.getEstablecimiento().getEmpresa());
			consultarEstablecimientos();
			AppJsfUtil.showModalRender("dlgUsuario", "frmUsuario");
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	/**
	 * @return the usuarioList
	 */
	public List<Usuario> getUsuarioList() {
		return usuarioList;
	}


	/**
	 * @param usuarioList the usuarioList to set
	 */
	public void setUsuarioList(List<Usuario> usuarioList) {
		this.usuarioList = usuarioList;
	}


	/**
	 * @return the estadoRegBusqueda
	 */
	public String getEstadoRegBusqueda() {
		return estadoRegBusqueda;
	}


	/**
	 * @param estadoRegBusqueda the estadoRegBusqueda to set
	 */
	public void setEstadoRegBusqueda(String estadoRegBusqueda) {
		this.estadoRegBusqueda = estadoRegBusqueda;
	}


	/**
	 * @return the usuarioSelected
	 */
	public Usuario getUsuarioSelected() {
		return usuarioSelected;
	}


	/**
	 * @param usuarioSelected the usuarioSelected to set
	 */
	public void setUsuarioSelected(Usuario usuarioSelected) {
		this.usuarioSelected = usuarioSelected;
	}


	/**
	 * @return the establecimientoList
	 */
	public List<Establecimiento> getEstablecimientoList() {
		return establecimientoList;
	}


	/**
	 * @param establecimientoList the establecimientoList to set
	 */
	public void setEstablecimientoList(List<Establecimiento> establecimientoList) {
		this.establecimientoList = establecimientoList;
	}
	

}
