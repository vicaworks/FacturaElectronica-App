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

}
