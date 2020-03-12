/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.vcw.falecpv.core.dao.impl.UsuarioDao;
import com.vcw.falecpv.core.modelo.persistencia.Usuario;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class UsuarioServicio extends AppGenericService<Usuario, String> {

	@Inject
	private UsuarioDao usuarioDao;
	
	/**
	 * 
	 */
	public UsuarioServicio() {
	}

	@Override
	public List<Usuario> consultarActivos() {
		return null;
	}

	@Override
	public List<Usuario> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Usuario, String> getDao() {
		return usuarioDao;
	}

}
