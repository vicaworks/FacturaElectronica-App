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
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param estadoRegistroEnum
	 * @param idEmpresa
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Establecimiento> getByEmpresa(String idEmpresa)throws DaoException{
		try {
			
			Query q = null;
			q = getEntityManager().createQuery("SELECT e FROM Establecimiento e WHERE e.empresa.idempresa=:id ORDER BY e.codigoestablecimiento");
			q.setParameter("id", idEmpresa);
			
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	

	/**
	 * @author Isabel Lobato
	 * @param idestablecimiento
	 * @param matriz
	 * @return
	 * @throws DaoException
	 */
	public boolean existeMatriz(String idestablecimiento, String matriz, String idempresa)throws DaoException{
		try {
			
			Query q = null;
			
			if(idestablecimiento!=null) {
				
				q = getEntityManager().createQuery("SELECT e FROM Establecimiento e WHERE e.matriz=:matriz AND e.idestablecimiento<>:idestablecimiento AND e.empresa.idempresa=:idempresa");
				q.setParameter("matriz", matriz);
				q.setParameter("idestablecimiento", idestablecimiento);
				q.setParameter("idempresa", idempresa);
			}else {
				
				q = getEntityManager().createQuery("SELECT e FROM Establecimiento e WHERE e.matriz=:matriz AND e.empresa.idempresa=:idempresa");
				q.setParameter("matriz", matriz);
				q.setParameter("idempresa", idempresa);
			}
			
			if(q.getResultList().size()>0) {
				return true;
			}
			
			return false;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author Isabel Lobato
	 * @param idestablecimiento
	 * @param matriz
	 * @return
	 * @throws DaoException
	 */
	public boolean existeCodEstablecimiento(String idestablecimiento, String codigoestablecimiento,String idEmpresa)throws DaoException{
		try {
			
			Query q = null;
			
			if(idestablecimiento!=null) {
				
				q = getEntityManager().createQuery("SELECT e FROM Establecimiento e WHERE e.empresa.idempresa=:id and e.codigoestablecimiento=:codigoestablecimiento AND e.idestablecimiento<>:idestablecimiento");
				q.setParameter("codigoestablecimiento", codigoestablecimiento);
				q.setParameter("idestablecimiento", idestablecimiento);
			}else {
				
				q = getEntityManager().createQuery("SELECT e FROM Establecimiento e WHERE e.empresa.idempresa=:id and e.codigoestablecimiento=:codigoestablecimiento");
				q.setParameter("codigoestablecimiento", codigoestablecimiento);
			}
			
			q.setParameter("id", idEmpresa);
			
			if(q.getResultList().size()>0) {
				return true;
			}
			
			return false;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	
	
	/**
	 * @author Isabel Lobato
	 * @param matriz
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public  List<Establecimiento> getEstablecimientobyMatriz(String matriz,String idEmpresa) throws DaoException{
		try {

			Query q = null;

			q = getEntityManager().createQuery("SELECT e FROM Establecimiento e WHERE e.empresa.idempresa=:id and e.matriz=:matriz");
			q.setParameter("matriz", matriz);
			q.setParameter("id", idEmpresa);

			if (q.getResultList().size() > 0) {
				return q.getResultList();
			}

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return null;
		
	}
}
