/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Detalle;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class DetalleDao extends AppGenericDao<Detalle, String> {

	/**
	 * @param type
	 */
	public DetalleDao() {
		super(Detalle.class);
	}

}
