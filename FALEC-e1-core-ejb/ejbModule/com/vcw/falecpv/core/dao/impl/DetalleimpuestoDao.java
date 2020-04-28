/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Detalleimpuesto;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class DetalleimpuestoDao extends AppGenericDao<Detalleimpuesto, String> {

	/**
	 * @param type
	 */
	public DetalleimpuestoDao() {
		super(Detalleimpuesto.class);
	}

}
