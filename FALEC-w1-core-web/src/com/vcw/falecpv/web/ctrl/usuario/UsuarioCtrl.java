/**
 * 
 */
package com.vcw.falecpv.web.ctrl.usuario;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.vcw.falecpv.core.modelo.persistencia.Usuario;
import com.vcw.falecpv.core.servicio.UsuarioServicio;
import com.vcw.falecpv.web.common.BaseCtrl;

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
	
	
	private List<Usuario> usuarioList;
	private String estadoRegBusqueda;
	
	/**
	 * 
	 */
	public UsuarioCtrl() {
	}


	@Override
	public void limpiar() {
		super.limpiar();
	}



	@Override
	public void refrescar() {
		super.refrescar();
	}

	private void consultarUsuario() {
		//usuarioList = UsuarioServicio.get
	}

	@Override
	public void eliminar() {
		super.eliminar();
	}



	@Override
	public void guardar() {
		super.guardar();
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
	

}
