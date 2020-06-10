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
import com.vcw.falecpv.core.modelo.persistencia.Fabricante;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class FabricanteDao extends AppGenericDao<Fabricante, String> {

	/**
	 * @param type
	 */
	public FabricanteDao() {
		super(Fabricante.class);
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param estadoRegistroEnum
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Fabricante> getByEstado(EstadoRegistroEnum estadoRegistroEnum,String idEstablecimiento)throws DaoException{
		try {
			
			Query q = null;
			if(!estadoRegistroEnum.equals(EstadoRegistroEnum.TODOS)) {
				q = getEntityManager().createQuery("SELECT f FROM Fabricante f WHERE f.establecimiento.idestablecimiento=:idestablecimiento AND f.estado=:estado ORDER BY f.nombrecomercial");
				q.setParameter("estado", estadoRegistroEnum.getInicial());
			}else {
				q = getEntityManager().createQuery("SELECT f FROM Fabricante f WHERE f.establecimiento.idestablecimiento=:idestablecimiento ORDER BY f.nombrecomercial");
			}
			
			q.setParameter("idestablecimiento", idEstablecimiento);
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param fabricante
	 * @param idFabricante
	 * @return
	 * @throws DaoException
	 */
	public boolean existeNombre(String fabricante,String idFabricante,String idEstablecimiento)throws DaoException{
		try {
			
			Query q = null;
			
			if(idFabricante!=null) {
				q = getEntityManager().createQuery("SELECT f FROM Fabricante f WHERE f.establecimiento.idestablecimiento=:idestablecimiento AND upper(f.nombrecomercial)=:fabricante AND f.idfabricante<>:idFabricante");
				q.setParameter("idFabricante", idFabricante);
			}else {
				q = getEntityManager().createQuery("SELECT f FROM Fabricante f WHERE f.establecimiento.idestablecimiento=:idestablecimiento AND upper(f.nombrecomercial)=:fabricante");
			}
			
			q.setParameter("fabricante", fabricante.toUpperCase());
			q.setParameter("idestablecimiento", idEstablecimiento);
			
			if(q.getResultList().size()>0) {
				return true;
			}
			
			
			return false;
			
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
