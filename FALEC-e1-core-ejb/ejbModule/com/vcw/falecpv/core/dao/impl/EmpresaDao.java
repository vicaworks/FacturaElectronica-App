/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.servitec.common.dao.exception.DaoException;
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
	
	@SuppressWarnings("unchecked")
	public List<Empresa> getEmpresaActual()throws DaoException{
		List<Empresa> lista;
		try {
			Query q = getEntityManager().createQuery("SELECT e FROM Empresa e");
			lista = q.getResultList();
			return lista;
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
}
