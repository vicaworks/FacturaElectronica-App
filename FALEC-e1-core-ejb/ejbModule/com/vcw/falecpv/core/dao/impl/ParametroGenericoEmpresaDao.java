/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.ParametroGenericoEmpresa;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class ParametroGenericoEmpresaDao extends AppGenericDao<ParametroGenericoEmpresa, String> {

	/**
	 * @param type
	 */
	public ParametroGenericoEmpresaDao() {
		super(ParametroGenericoEmpresa.class);
	}

}
