/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Segperfil;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class SegperfilDao extends AppGenericDao<Segperfil, String> {

	/**
	 * @param type
	 */
	public SegperfilDao() {
		super(Segperfil.class);
	}

	/**
	 * @author cristianvillarreal
	 * 
	 * @param estadoRegistroEnum
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Segperfil> getByEstado(EstadoRegistroEnum estadoRegistroEnum){
		
		Query q = getEntityManager().createQuery("SELECT p FROM Segperfil p WHERE " + (estadoRegistroEnum.equals(EstadoRegistroEnum.TODOS)?" ":"p.estado=:estado ") + " ORDER BY p.nombre ");
		if(!estadoRegistroEnum.equals(EstadoRegistroEnum.TODOS)) {
			q.setParameter("estado", estadoRegistroEnum.getInicial());
		}
		
		return q.getResultList();
	}
	
}
