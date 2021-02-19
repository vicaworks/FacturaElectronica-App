/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Segperfilusuario;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class SegperfilusuarioDao extends AppGenericDao<Segperfilusuario, String> {

	/**
	 * @param type
	 */
	public SegperfilusuarioDao() {
		super(Segperfilusuario.class);
	}

}
