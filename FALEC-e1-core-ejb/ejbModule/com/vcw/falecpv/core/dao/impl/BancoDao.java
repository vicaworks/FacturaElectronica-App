/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Banco;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class BancoDao extends AppGenericDao<Banco, String> {

	/**
	 * @param type
	 */
	public BancoDao() {
		super(Banco.class);
	}

}
