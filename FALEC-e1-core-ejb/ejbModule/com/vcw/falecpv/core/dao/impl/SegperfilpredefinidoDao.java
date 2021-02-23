/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Segperfilpredefinido;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class SegperfilpredefinidoDao extends AppGenericDao<Segperfilpredefinido, String> {

	/**
	 * @param type
	 */
	public SegperfilpredefinidoDao() {
		super(Segperfilpredefinido.class);
	}

	/**
	 * @author cristianvillarreal
	 * 
	 * @param estadoRegistroEnum
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Segperfilpredefinido> getByEstado(EstadoRegistroEnum estadoRegistroEnum){
		
		Query q = getEntityManager().createQuery("SELECT p FROM Segperfilpredefinido p WHERE " + (estadoRegistroEnum.equals(EstadoRegistroEnum.TODOS)?" ":"p.estado=:estado ") + " ORDER BY p.orden");
		if(!estadoRegistroEnum.equals(EstadoRegistroEnum.TODOS)) {
			q.setParameter("estado", estadoRegistroEnum.getInicial());
		}
		
		return q.getResultList();
	}
	
}
