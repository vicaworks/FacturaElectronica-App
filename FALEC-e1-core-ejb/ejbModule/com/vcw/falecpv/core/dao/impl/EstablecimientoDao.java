/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.NoResultException;
import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Establecimiento;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class EstablecimientoDao extends AppGenericDao<Establecimiento, String> {

	/**
	 * @param type
	 */
	public EstablecimientoDao() {
		super(Establecimiento.class);
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param estadoRegistroEnum
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings({ "null", "unchecked" })
	public List<Establecimiento> getByEstado(EstadoRegistroEnum estadoRegistroEnum)throws DaoException{
		try {
			
			Query q = null;
			if(!estadoRegistroEnum.equals(EstadoRegistroEnum.ACTIVO)) {
				q = getEntityManager().createQuery("SELECT e FROM Establecimiento e WHERE e.estado=:estado ORDER BY e.nombrecomercial");
				q.setParameter("estado", estadoRegistroEnum.getInicial());
			}else {
				q = getEntityManager().createQuery("SELECT e FROM Establecimiento e ORDER BY e.nombrecomercial");
			}
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
}
