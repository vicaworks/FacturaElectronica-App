/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Categoria;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class CategoriaDao extends AppGenericDao<Categoria, String> {

	/**
	 * @param type
	 */
	public CategoriaDao() {
		super(Categoria.class);
	}

}
