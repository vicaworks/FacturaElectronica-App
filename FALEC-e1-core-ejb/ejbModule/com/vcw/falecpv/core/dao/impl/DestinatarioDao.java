/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Destinatario;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class DestinatarioDao extends AppGenericDao<Destinatario, String> {

	/**
	 * @param type
	 */
	public DestinatarioDao() {
		super(Destinatario.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<Destinatario> getIdDestinatarioByCabeceraIdList(List<String> idsList)throws DaoException{
		try {
			
			Query q = getEntityManager().createQuery("SELECT d FROM Destinatario d WHERE d.cabecera.idcabecera in :idsList ORDER BY d.cabecera.idcabecera,d.razonsocialdestinatario");
			q.setParameter("idsList", idsList);
			
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
