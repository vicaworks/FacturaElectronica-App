/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Tipocomprobante;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class TipocomprobanteDao extends AppGenericDao<Tipocomprobante, String> {

	/**
	 * @param type
	 */
	public TipocomprobanteDao() {
		super(Tipocomprobante.class);
	}

}
