/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Detalledestinatario;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class DetalledestinatarioDao extends AppGenericDao<Detalledestinatario, String> {

	/**
	 * @param type
	 */
	public DetalledestinatarioDao() {
		super(Detalledestinatario.class);
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idDestinatarioList
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Detalledestinatario> getByIdListDestinatario(List<String> idDestinatarioList)throws DaoException{
		try {
			
			Query q = getEntityManager().createQuery("SELECT d FROM Detalledestinatario d WHERE d.destinatario.iddestinatario in :idsList ORDER BY d.descripcion");
			q.setParameter("idsList", idDestinatarioList);
			return q.getResultList();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idDestinatario
	 * @return
	 * @throws DaoException
	 */
	public int eliminarByDestinatario(String idDestinatario)throws DaoException{
		try {
			
			Query q = getEntityManager().createNativeQuery("DELETE FROM detalledestinatario WHERE iddestinatario=:id");
			q.setParameter("id", idDestinatario);
			
			return q.executeUpdate();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
