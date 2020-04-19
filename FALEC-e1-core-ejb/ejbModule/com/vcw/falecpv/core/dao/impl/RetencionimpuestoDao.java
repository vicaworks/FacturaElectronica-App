/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Retencionimpuesto;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class RetencionimpuestoDao extends AppGenericDao<Retencionimpuesto, String> {

	public RetencionimpuestoDao() {
		super(Retencionimpuesto.class);
	}

}
