/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Destinatario;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class DestinatarioDao extends AppGenericDao<Destinatario, String> {

	/**
	 * @param type
	 */
	public DestinatarioDao() {
		super(Destinatario.class);
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idsList
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Destinatario> getIdDestinatarioByCabeceraIdList(List<String> idsList)throws DaoException{
		try {
			
			Query q = getEntityManager().createQuery("SELECT d FROM Destinatario d WHERE d.cabecera.idcabecera in :idsList ORDER BY d.razonsocialdestinatario,cabecera.fechaemision");
			q.setParameter("idsList", idsList);
			
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idCabecera
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Destinatario> getByIdCabecera(String idCabecera)throws DaoException{
		try {
			
			Query q = getEntityManager().createQuery("SELECT d FROM Destinatario d WHERE d.cabecera.idcabecera =:idCabecera ORDER BY d.razonsocialdestinatario,cabecera.fechaemision");
			q.setParameter("idCabecera", idCabecera);
			
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idsList
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Destinatario> getById(List<String> idsList)throws DaoException{
		try {
			
			Query q = getEntityManager().createQuery("SELECT d FROM Destinatario d WHERE d.iddestinatario in :idsList ORDER BY d.razonsocialdestinatario,cabecera.fechaemision");
			q.setParameter("idsList", idsList);
			
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idCabecera
	 * @return
	 * @throws DaoException
	 */
	public int eliminarByCabecera(String idCabecera)throws DaoException{
		try {
			
			// encera las referencias a esta guia de remision
			Query q = getEntityManager().createNativeQuery("UPDATE cabecera set idguiaremision=null WHERE idguiaremision=:idcabecera");
			q.setParameter("idcabecera", idCabecera);
			q.executeUpdate();
			
			q = getEntityManager().createNativeQuery("DELETE FROM destinatario WHERE idcabecera=:id");
			q.setParameter("id", idCabecera);
			
			return q.executeUpdate();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
