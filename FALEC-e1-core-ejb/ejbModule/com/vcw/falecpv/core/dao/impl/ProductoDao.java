/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Producto;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class ProductoDao extends AppGenericDao<Producto, String> {

	/**
	 * @param type
	 */
	public ProductoDao() {
		super(Producto.class);
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param estadoRegistroEnum
	 * @param idEstablecimiento
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Producto> getByEstado(EstadoRegistroEnum estadoRegistroEnum,String idEstablecimiento)throws DaoException{
		try {
			
			Query q = null;
			if(!estadoRegistroEnum.equals(EstadoRegistroEnum.TODOS)) {
				q = getEntityManager().createQuery("SELECT p FROM Producto p WHERE p.establecimiento.idestablecimiento=:idestablecimiento AND  p.estado=:estado ORDER BY p.nombregenerico");
				q.setParameter("estado", estadoRegistroEnum.getInicial());
			}else {
				q = getEntityManager().createQuery("SELECT p FROM Producto p WHERE p.establecimiento.idestablecimiento=:idestablecimiento ORDER BY p.nombregenerico");
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
	 * @param tipoproducto
	 * @param estadoRegistroEnum
	 * @param idEstablecimiento
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Producto> getByEstado(String tipoproducto ,EstadoRegistroEnum estadoRegistroEnum,String idEstablecimiento)throws DaoException{
		try {
			
			Query q = null;
			if(!estadoRegistroEnum.equals(EstadoRegistroEnum.TODOS)) {
				q = getEntityManager().createQuery("SELECT p FROM Producto p WHERE p.tipoProducto.nombre=:tipoproducto AND p.establecimiento.idestablecimiento=:idestablecimiento AND  p.estado=:estado ORDER BY p.nombregenerico");
				q.setParameter("estado", estadoRegistroEnum.getInicial());
			}else {
				q = getEntityManager().createQuery("SELECT p FROM Producto p WHERE p.tipoProducto.nombre=:tipoproducto AND p.establecimiento.idestablecimiento=:idestablecimiento ORDER BY p.nombregenerico");
			}
			
			q.setParameter("idestablecimiento", idEstablecimiento);
			q.setParameter("tipoproducto", tipoproducto);
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEstablecimiento
	 * @param criteria
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Producto> getByCriteriaEstado(String idEstablecimiento,String criteria)throws DaoException{
		try {
			
			String sql = "SELECT p FROM Producto p WHERE p.establecimiento.idestablecimiento=:idestablecimiento AND p.estado='A' "; 
			if(criteria!=null && criteria.trim().length()>0) {
				sql +="AND ( UPPER(p.categoria.categoria) like :categoria OR UPPER(p.nombre) like :nombre OR UPPER(p.nombregenerico) like :nombregenerico OR p.codigoprincipal=:codigoprincipal) ";
			}
			
			sql += " ORDER BY p.nombregenerico";
			
			Query q = getEntityManager().createQuery(sql);
			q.setParameter("idestablecimiento", idEstablecimiento);
			if(criteria!=null && criteria.trim().length()>0) {
				q.setParameter("categoria", "%".concat(criteria.toUpperCase()).concat("%"));
				q.setParameter("nombre", "%".concat(criteria.toUpperCase()).concat("%"));
				q.setParameter("nombregenerico", "%".concat(criteria.toUpperCase()).concat("%"));
				q.setParameter("codigoprincipal", criteria.toUpperCase());
			}
			
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEstablecimiento
	 * @param criteria
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Producto> getByCodigoBarraEstado(String idEstablecimiento,String criteria)throws DaoException{
		try {
			
			String sql = "SELECT p FROM Producto p WHERE p.establecimiento.idestablecimiento=:idestablecimiento AND p.estado='A' AND p.codigoprincipal=:codigobarra ORDER BY p.nombre"; 
			
			Query q = getEntityManager().createQuery(sql);
			q.setParameter("idestablecimiento", idEstablecimiento);
			q.setParameter("codigobarra", criteria);
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @param idEstablecimiento
	 * @param idCategoria
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Producto> getByCategoriaEstado(String idEstablecimiento,String idCategoria)throws DaoException{
		try {
			
			String sql = "SELECT p FROM Producto p WHERE p.establecimiento.idestablecimiento=:idestablecimiento AND p.estado='A' AND p.categoria.idcategoria=:idcategoria ORDER BY p.nombre";
						
			Query q = getEntityManager().createQuery(sql);
			q.setParameter("idestablecimiento", idEstablecimiento);
			q.setParameter("idcategoria", idCategoria);			
			
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param codigoPrincipal
	 * @param idEstablecimiento
	 * @return
	 * @throws DaoException
	 */
	public Producto getByCodigoPrincipal(String codigoPrincipal,String idEstablecimiento)throws DaoException{
		try {
			
			String sql = "SELECT p FROM Producto p WHERE p.establecimiento.idestablecimiento=:idestablecimiento AND p.estado='A' AND p.codigoprincipal=:codigoprincipal "; 
			Query q = getEntityManager().createQuery(sql);
			q.setParameter("idestablecimiento", idEstablecimiento);
			q.setParameter("codigoprincipal", codigoPrincipal);
			
			return (Producto) q.getSingleResult();
			
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			throw new DaoException(e);
		} 
	}
	
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param estadoRegistroEnum
	 * @param idEstablecimiento
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Producto> getByQuery(EstadoRegistroEnum estadoRegistroEnum,String idEstablecimiento)throws DaoException{
		try {
			
			Query q = null;
			if(!estadoRegistroEnum.equals(EstadoRegistroEnum.TODOS)) {
//				q = getEntityManager().createQuery("SELECT p.idproducto, p.nombre, p.nombregenerico FROM Producto p WHERE p.establecimiento.idestablecimiento=:idestablecimiento AND  p.estado=:estado ORDER BY p.nombregenerico");
				q = getEntityManager().createQuery("SELECT p FROM Producto p WHERE p.establecimiento.idestablecimiento=:idestablecimiento AND  p.estado=:estado ORDER BY p.nombregenerico");
				q.setParameter("estado", estadoRegistroEnum.getInicial());
			}else {
//				q = getEntityManager().createQuery("SELECT p.idproducto, p.nombre, p.nombregenerico FROM Producto p WHERE p.establecimiento.idestablecimiento=:idestablecimiento ORDER BY p.nombregenerico");
				q = getEntityManager().createQuery("SELECT p FROM Producto p WHERE p.establecimiento.idestablecimiento=:idestablecimiento ORDER BY p.nombregenerico");
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
	 * @param tipoProducto
	 * @param estadoRegistroEnum
	 * @param idEstablecimiento
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Producto> getByQuery(String tipoProducto,EstadoRegistroEnum estadoRegistroEnum,String idEstablecimiento)throws DaoException{
		try {
			
			Query q = null;
			if(!estadoRegistroEnum.equals(EstadoRegistroEnum.TODOS)) {
				q = getEntityManager().createQuery("SELECT p FROM Producto p WHERE p.tipoProducto.nombre=:tipoproducto AND p.establecimiento.idestablecimiento=:idestablecimiento AND  p.estado=:estado ORDER BY p.nombregenerico");
				q.setParameter("estado", estadoRegistroEnum.getInicial());
			}else {
				q = getEntityManager().createQuery("SELECT p FROM Producto p WHERE p.tipoProducto.nombre=:tipoproducto AND  p.establecimiento.idestablecimiento=:idestablecimiento ORDER BY p.nombregenerico");
			}
			
			q.setParameter("idestablecimiento", idEstablecimiento);
			q.setParameter("tipoproducto", tipoProducto);
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param tipoProducto
	 * @param estadoRegistroEnum
	 * @param idEstablecimiento
	 * @param idEmpresa
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Producto> getByQuery(EstadoRegistroEnum estadoRegistroEnum,String idEstablecimiento,String idEmpresa)throws DaoException{
		try {
			
			Query q = null;
			if(!estadoRegistroEnum.equals(EstadoRegistroEnum.TODOS)) {
				q = getEntityManager().createQuery("SELECT p FROM Producto p WHERE " +
					(idEstablecimiento!=null?" p.establecimiento.idestablecimiento=:idestablecimiento ":" p.establecimiento.empresa.idempresa=:idempresa ") + 
					" AND  p.estado=:estado ORDER BY p.nombregenerico");
				q.setParameter("estado", estadoRegistroEnum.getInicial());
			}else {
				q = getEntityManager().createQuery("SELECT p FROM Producto p WHERE  " +
						(idEstablecimiento!=null?" p.establecimiento.idestablecimiento=:idestablecimiento ":" p.establecimiento.empresa.idempresa=:idempresa ") +
						" ORDER BY p.nombregenerico");
			}
			if(idEstablecimiento!=null) {
				q.setParameter("idestablecimiento", idEstablecimiento);
			}else {
				q.setParameter("idempresa", idEmpresa);
			}
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEstablecimiento
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Producto> consultarAllImageEager(String idEstablecimiento,String criteria)
			throws DaoException {
		try {
			
			String sql = "SELECT DISTINCT p "
					+ "FROM Producto p "
					+ "WHERE "
					+ "p.establecimiento.idestablecimiento=:idestablecimiento ";
			
			if(criteria!=null && criteria.trim().length()>0) {
				sql += "AND (";
				sql += " UPPER(p.fabricante.nombrecomercial) like :fabricante ";
				sql += " OR UPPER(p.nombregenerico) like :nombregenerico ";
				sql += " OR UPPER(p.categoria.categoria) like :categoria  ";
				sql += " OR p.codigoprincipal like :codigoprincipal ";
				sql += ") ";
			}
			sql += "ORDER BY p.nombregenerico";
			
			Query q = getEntityManager().createQuery(sql);
			q.setParameter("idestablecimiento", idEstablecimiento);
			if(criteria!=null && criteria.trim().length()>0) {
				q.setParameter("fabricante", "%".concat(criteria.toUpperCase()).concat("%"));
				q.setParameter("nombregenerico", "%".concat(criteria.toUpperCase()).concat("%"));
				q.setParameter("categoria", "%".concat(criteria.toUpperCase()).concat("%"));
				q.setParameter("codigoprincipal", "%".concat(criteria.toUpperCase()).concat("%"));
			}
			
			
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param nombreGenerico
	 * @param idProducto
	 * @param idEstablecimiento
	 * @return
	 * @throws DaoException
	 */
	public boolean existeNombreGenerico(String nombreGenerico,String idProducto,String idEstablecimiento)throws DaoException{
		try {
			
			Query q = null;
			
			if(idProducto!=null) {
				q = getEntityManager().createQuery("SELECT p.idproducto FROM Producto p WHERE p.establecimiento.idestablecimiento=:idestablecimiento AND upper(p.nombregenerico)=:nombregenerico AND p.idproducto<>:idproducto");
				q.setParameter("idproducto", idProducto);
			}else {
				q = getEntityManager().createQuery("SELECT p.idproducto FROM Producto p WHERE p.establecimiento.idestablecimiento=:idestablecimiento AND upper(p.nombregenerico)=:nombregenerico ");
			}
			q.setParameter("nombregenerico", nombreGenerico);
			q.setParameter("idestablecimiento", idEstablecimiento);
			
			if(q.getResultList().size()>0) {
				return true;
			}
			
			return false;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param nombre
	 * @param idProducto
	 * @param idEstablecimiento
	 * @return
	 * @throws DaoException
	 */
	public boolean existeNombre(String nombre,String idProducto,String idEstablecimiento)throws DaoException{
		try {
			
			Query q = null;
			
			if(idProducto!=null) {
				q = getEntityManager().createQuery("SELECT p.idproducto FROM Producto p WHERE p.establecimiento.idestablecimiento=:idestablecimiento AND upper(p.nombre)=:nombre AND p.idproducto<>:idproducto");
				q.setParameter("idproducto", idProducto);
			}else {
				q = getEntityManager().createQuery("SELECT p.idproducto FROM Producto p WHERE p.establecimiento.idestablecimiento=:idestablecimiento AND upper(p.nombre)=:nombre ");
			}
			
			q.setParameter("nombre", nombre);
			q.setParameter("idestablecimiento", idEstablecimiento);
			
			if(q.getResultList().size()>0) {
				return true;
			}
			
			return false;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param codigoproducto
	 * @param idProducto
	 * @param idEstablecimiento
	 * @return
	 * @throws DaoException
	 */
	public boolean existeCodigoProducto(String codigoproducto,String idProducto,String idEstablecimiento)throws DaoException{
		try {
			
			Query q = null;
			
			if(idProducto!=null) {
				q = getEntityManager().createQuery("SELECT p.idproducto FROM Producto p WHERE p.establecimiento.idestablecimiento=:idestablecimiento AND upper(p.codigoprincipal)=:codigoprincipal AND p.idproducto<>:idproducto");
				q.setParameter("idproducto", idProducto);
			}else {
				q = getEntityManager().createQuery("SELECT p.idproducto FROM Producto p WHERE p.establecimiento.idestablecimiento=:idestablecimiento AND upper(p.codigoprincipal)=:codigoprincipal ");
			}
			
			q.setParameter("codigoprincipal", codigoproducto);
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
