/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Retenciondetalle;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class RetenciondetalleDao extends AppGenericDao<Retenciondetalle, String> {

	/**
	 * @param type
	 */
	public RetenciondetalleDao() {
		super(Retenciondetalle.class);
	}
	
}
