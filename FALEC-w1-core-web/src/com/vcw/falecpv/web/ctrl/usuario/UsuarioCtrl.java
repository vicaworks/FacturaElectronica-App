/**
 * 
 */
package com.vcw.falecpv.web.ctrl.usuario;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.modelo.persistencia.Usuario;
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
	
	
	private List<Usuario> usuarioList;
	private String estadoRegBusqueda;
	
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

	@Override
	public void eliminar() {
		super.eliminar();
	}

	@Override
	public void guardar() {
		super.guardar();
	}

	public void nuevo() {
		
	}
	
	public void editar() {
		
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
