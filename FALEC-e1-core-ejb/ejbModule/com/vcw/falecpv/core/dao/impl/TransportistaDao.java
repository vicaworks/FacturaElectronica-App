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
import com.vcw.falecpv.core.modelo.persistencia.Transportista;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class TransportistaDao extends AppGenericDao<Transportista, String> {

	public TransportistaDao() {
		super(Transportista.class);
	}

	/**
	 * @author cristianvillarreal
	 * 
	 * @param estadoRegistroEnum
	 * @param idEmpresa
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Transportista> getByEstado(EstadoRegistroEnum estadoRegistroEnum, String idEmpresa)
			throws DaoException {
		try {
			Query q = null;

			if (estadoRegistroEnum == null) {
				q = getEntityManager().createQuery(
						"SELECT t FROM Transportista t WHERE t.empresa.idempresa=:idempresa ORDER BY t.razonsocial");
			} else {
				q = getEntityManager().createQuery(
						"SELECT t FROM Transportista t WHERE t.estado=:estado AND t.empresa.idempresa=:idempresa ORDER BY t.razonsocial");
				q.setParameter("estado", estadoRegistroEnum.getInicial());
			}

			q.setParameter("idempresa", idEmpresa);

			return q.getResultList();

		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEmpresa
	 * @param criteria
	 * @param estadoRegistroEnum
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Transportista> getByCriteria(String idEmpresa,String criteria,EstadoRegistroEnum estadoRegistroEnum)throws DaoException{
		
		String sql = "SELECT t FROM Transportista t WHERE t.empresa.idempresa=:idempresa ";
		
		sql += !estadoRegistroEnum.equals(EstadoRegistroEnum.TODOS)?" AND t.estado=:estado ":" ";
		
		if(criteria!=null && criteria.trim().length()>0) {
			sql += "AND (";
			sql += " t.identificacion like :identificacion ";
			sql += " OR t.razonsocial like :razonsocial ";
			sql += ") ";
		}
		sql += "ORDER BY t.razonsocial";
		
		Query q = getEntityManager().createQuery(sql);
		q.setParameter("idempresa", idEmpresa);
		if(estadoRegistroEnum!=null) {
			q.setParameter("estado", estadoRegistroEnum.getInicial());
		}
		
		if(criteria!=null && criteria.trim().length()>0) {
			q.setParameter("identificacion", "%".concat(criteria).concat("%"));
			q.setParameter("razonsocial", "%".concat(criteria).concat("%"));
		}
		
		return q.getResultList();
	}

}
