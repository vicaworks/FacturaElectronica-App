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
import com.vcw.falecpv.core.modelo.persistencia.Retencionimpuestodet;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class RetencionimpuestodetDao extends AppGenericDao<Retencionimpuestodet, String> {

	/**
	 * @param type
	 */
	public RetencionimpuestodetDao() {
		super(Retencionimpuestodet.class);
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idRetencionImpuesto
	 * @param estadoRegistroEnum
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Retencionimpuestodet> getByRetencionEstado(String idRetencionImpuesto,EstadoRegistroEnum estadoRegistroEnum)throws DaoException{
		try {
			
			Query q = getEntityManager().createQuery("SELECT d FROM Retencionimpuestodet d WHERE d.retencionimpuesto.idretencionimpuesto=:id AND d.estado=:estado");
			q.setParameter("id", idRetencionImpuesto);
			q.setParameter("estado", estadoRegistroEnum.getInicial());
			
			return q.getResultList();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
