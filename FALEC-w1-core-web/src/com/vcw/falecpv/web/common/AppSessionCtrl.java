/**
 * 
 */
package com.vcw.falecpv.web.common;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import com.servitec.common.jsf.FacesUtil;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.modelo.persistencia.Usuario;
import com.vcw.falecpv.core.servicio.UsuarioServicio;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named(value="appSessionCtrl")
@SessionScoped
public class AppSessionCtrl implements Serializable {

	
	@EJB
	private UsuarioServicio usuarioServicio;
	
	private String nombreEstablecimiento;
	private String nombreEmpresa;
	private String nombreDisplay;
	private Boolean administrador;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6792152616234338008L;

	/**
	 * 
	 */
	public AppSessionCtrl() {
	}
	
	private void establecerdatosIniciales() {
		try {
			
			if (AppJsfUtil.getLoginUser()!=null) {
				// 1. datos del usuario
				Usuario usuario = usuarioServicio.getUsuarioDao().getByLogin(AppJsfUtil.getRemoteUser());
				nombreDisplay = usuario.getNombrepantalla();
				administrador = usuario.getAdministrador().equals("S")?true:false;
				nombreEstablecimiento = usuario.getEstablecimiento().getNombrecomercial();
				nombreEmpresa = usuario.getEstablecimiento().getEmpresa().getNombrecomercial();
				
				// 2. guardar en session los datos de empresa y establecimeinto
				FacesUtil.getHttpSession(false).setAttribute("establecimiento", usuario.getEstablecimiento());
				FacesUtil.getHttpSession(false).setAttribute("usuario", usuario);
			}
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage(null,TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	/**
	 * @return the nombreEstablecimiento
	 */
	public String getNombreEstablecimiento() {
		
		if(nombreEstablecimiento==null) {
			establecerdatosIniciales();
		}
		
		return nombreEstablecimiento;
	}

	/**
	 * @param nombreEstablecimiento the nombreEstablecimiento to set
	 */
	public void setNombreEstablecimiento(String nombreEstablecimiento) {
		this.nombreEstablecimiento = nombreEstablecimiento;
	}

	/**
	 * @return the nombreDisplay
	 */
	public String getNombreDisplay() {
		if (nombreDisplay==null) {
			establecerdatosIniciales();
		}
		return nombreDisplay;
	}

	/**
	 * @param nombreDisplay the nombreDisplay to set
	 */
	public void setNombreDisplay(String nombreDisplay) {
		this.nombreDisplay = nombreDisplay;
	}

	/**
	 * @return the usuarioServicio
	 */
	public UsuarioServicio getUsuarioServicio() {
		return usuarioServicio;
	}

	/**
	 * @param usuarioServicio the usuarioServicio to set
	 */
	public void setUsuarioServicio(UsuarioServicio usuarioServicio) {
		this.usuarioServicio = usuarioServicio;
	}

	/**
	 * @return the administrador
	 */
	public Boolean getAdministrador() {
		if(administrador==null) {
			establecerdatosIniciales();
		}
		return administrador;
	}

	/**
	 * @param administrador the administrador to set
	 */
	public void setAdministrador(Boolean administrador) {
		this.administrador = administrador;
	}

	/**
	 * @return the nombreEmpresa
	 */
	public String getNombreEmpresa() {
		if(nombreEmpresa==null) {
			establecerdatosIniciales();
		}
		return nombreEmpresa;
	}

	/**
	 * @param nombreEmpresa the nombreEmpresa to set
	 */
	public void setNombreEmpresa(String nombreEmpresa) {
		this.nombreEmpresa = nombreEmpresa;
	}	

}
