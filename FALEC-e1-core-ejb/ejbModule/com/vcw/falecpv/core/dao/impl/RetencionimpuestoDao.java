/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Retencionimpuesto;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class RetencionimpuestoDao extends AppGenericDao<Retencionimpuesto, String> {

	public RetencionimpuestoDao() {
		super(Retencionimpuesto.class);
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param estadoRegistroEnum
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Retencionimpuesto> getByEstado(EstadoRegistroEnum estadoRegistroEnum)throws DaoException{
		try {
			
			Query q = getEntityManager().createQuery("SELECT r FROM Retencionimpuesto r WHERE r.estado=:estado ");
			q.setParameter("estado", estadoRegistroEnum.getInicial());
			
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
