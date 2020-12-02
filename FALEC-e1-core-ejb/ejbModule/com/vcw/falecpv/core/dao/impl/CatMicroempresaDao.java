/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.CatMicroempresa;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class CatMicroempresaDao extends AppGenericDao<CatMicroempresa, String> {

	/**
	 * @param type
	 */
	public CatMicroempresaDao() {
		super(CatMicroempresa.class);
	}

}
