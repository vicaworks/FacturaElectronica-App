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
import com.vcw.falecpv.core.modelo.persistencia.TipoProducto;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class TipoProductoDao extends AppGenericDao<TipoProducto, String> {

	/**
	 * @param type
	 */
	public TipoProductoDao() {
		super(TipoProducto.class);
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param estadoRegistroEnum
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<TipoProducto> getByEstado(EstadoRegistroEnum estadoRegistroEnum)throws DaoException{
		try {
			Query q = null;
			if(!estadoRegistroEnum.equals(EstadoRegistroEnum.TODOS)) {
				
				q = getEntityManager().createQuery("SELECT t FROM TipoProducto t WHERE t.estado=:estado ORDER BY t.nombre");
				q.setParameter("estado", estadoRegistroEnum.getInicial());
			}else {
				q = getEntityManager().createQuery("SELECT t FROM TipoProducto t ORDER BY t.nombre");
			}
			
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
