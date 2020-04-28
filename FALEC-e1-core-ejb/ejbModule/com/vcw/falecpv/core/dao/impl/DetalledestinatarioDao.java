/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Detalledestinatario;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class DetalledestinatarioDao extends AppGenericDao<Detalledestinatario, String> {

	/**
	 * @param type
	 */
	public DetalledestinatarioDao() {
		super(Detalledestinatario.class);
	}

}
