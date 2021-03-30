/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Segperfilusuario;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class SegperfilusuarioDao extends AppGenericDao<Segperfilusuario, String> {

	/**
	 * @param type
	 */
	public SegperfilusuarioDao() {
		super(Segperfilusuario.class);
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idList
	 * @return
	 */
	public int deleteByIds(List<String> idList) {
		if(idList==null || idList.isEmpty()) {
			idList = new ArrayList<>();
			idList.add("-x");
		}
		Query q = getEntityManager().createNativeQuery("DELETE FROM segperfilusuario WHERE idsegperfilusuario in :lista");
		q.setParameter("lista", idList);
		return q.executeUpdate();
	}

}
