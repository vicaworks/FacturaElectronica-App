/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Usuario;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class UsuarioDao extends AppGenericDao<Usuario, String> {

	/**
	 * @param type
	 */
	public UsuarioDao() {
		super(Usuario.class);
	}

}
