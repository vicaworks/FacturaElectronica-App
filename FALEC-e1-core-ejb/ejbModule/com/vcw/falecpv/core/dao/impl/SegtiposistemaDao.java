/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Segtiposistema;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class SegtiposistemaDao extends AppGenericDao<Segtiposistema, String> {

	public SegtiposistemaDao() {
		super(Segtiposistema.class);
	}

}
