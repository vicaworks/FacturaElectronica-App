/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.UtilMd5;
import com.vcw.falecpv.core.constante.contadores.TCUsuario;
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
	
	@EJB
	private ContadorPkServicio contadorPkServicio;
	
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

	/**
	 * @return the usuarioDao
	 */
	public UsuarioDao getUsuarioDao() {
		return usuarioDao;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param usuario
	 * @return
	 * @throws DaoException
	 */
	public Usuario guardar(Usuario usuario)throws DaoException{
		try {
			
			if (usuario.getIdusuario()==null) {
				usuario.setIdusuario(contadorPkServicio.generarContadorTabla(TCUsuario.USUARIO, usuario.getEstablecimiento().getIdestablecimiento()));
				usuario.setClave(UtilMd5.hash(usuario.getClave()));
				crear(usuario);
			}else {
				if (usuario.isActualizarCredenciales()) {
					usuario.setClave(UtilMd5.hash(usuario.getClave()));
				}
				actualizar(usuario);
			}
			return usuario;
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idUsuario
	 * @return
	 * @throws DaoException
	 */
	public boolean dependencias(String idUsuario)throws DaoException{
		return false;
	}

}
