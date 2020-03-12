/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Empresa;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class EmpresaDao extends AppGenericDao<Empresa, String> {

	/**
	 * @param type
	 */
	public EmpresaDao() {
		super(Empresa.class);
	}

}
