/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.KardexProducto;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class KardexProductoDao extends AppGenericDao<KardexProducto, String> {

	/**
	 * @param type
	 */
	public KardexProductoDao() {
		super(KardexProducto.class);
	}

	/**
	 * @author cristianvillarreal
	 * 
	 * @param idProducto
	 * @param idEstablecimiento
	 * @param desde
	 * @param hasta
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<KardexProducto> consultar(String idProducto,String idEstablecimiento,Date desde,Date hasta)throws DaoException{
		try {
			String sql = "SELECT k FROM KardexProducto k WHERE k.producto.idproducto=:idproducto "
					+ "AND k.establecimiento.idestablecimiento=:idestablecimiento "
					+ "AND k.fecharegistro BETWEEN :desde AND :hasta "
					+ "ORDER BY k.orden DESC";
			Query q = getEntityManager().createQuery(sql);
			q.setParameter("idproducto", idProducto);
			q.setParameter("idestablecimiento", idEstablecimiento);
			q.setParameter("desde", desde);
			q.setParameter("hasta", hasta);
			
			return q.getResultList();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEstablecimiento
	 * @param desde
	 * @param hasta
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<KardexProducto> consultar(String idEstablecimiento,Date desde,Date hasta)throws DaoException{
		try {
			String sql = "SELECT k FROM KardexProducto k WHERE  "
					+ "k.establecimiento.idestablecimiento=:idestablecimiento "
					+ "AND k.fecharegistro BETWEEN :desde AND :hasta "
					+ "ORDER BY k.producto.nombregenerico ASC, k.orden DESC";
			Query q = getEntityManager().createQuery(sql);
			q.setParameter("idestablecimiento", idEstablecimiento);
			q.setParameter("desde", desde);
			q.setParameter("hasta", hasta);
			
			return q.getResultList();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idProducto
	 * @param idEstablecimiento
	 * @return
	 * @throws DaoException
	 */
	public Date getMaxFechaRegistro(String idProducto,String idEstablecimiento)throws DaoException{
		try {
			
			Query q = getEntityManager().createQuery("SELECT MAX(k.fecharegistro) FROM KardexProducto k WHERE k.producto.idproducto=:idproducto AND k.establecimiento.idestablecimiento=:idestablecimiento ");
			q.setParameter("idestablecimiento", idEstablecimiento);
			q.setParameter("idproducto", idProducto);
			return (Date) q.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
}
