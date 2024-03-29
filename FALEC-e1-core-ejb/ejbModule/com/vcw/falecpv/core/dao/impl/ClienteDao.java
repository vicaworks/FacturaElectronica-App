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
import com.vcw.falecpv.core.modelo.persistencia.Cliente;
import com.xpert.persistence.query.QueryBuilder;

/**
 * @author Jorge Toaza
 *
 */
@Stateless
public class ClienteDao extends AppGenericDao<Cliente, String> {

	/**
	 * @param type
	 */
	public ClienteDao() {
		super(Cliente.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<Cliente> getByConsulta(String idEmpresa,EstadoRegistroEnum estadoRegistroEnum,String criterioBusqueda)throws DaoException{
		try {
			
			
			String sql = "SELECT p FROM Cliente p WHERE p.empresa.idempresa=:idempresa ";
			
			if(!estadoRegistroEnum.equals(EstadoRegistroEnum.TODOS)) {
				sql += " AND p.estado=:estado ";
			}
			
			if(criterioBusqueda!=null && criterioBusqueda.trim().length()>0) {
				sql += " AND (p.identificacion like :identificacion OR  upper(p.razonsocial) like :razonsocial) ";
			}
			
			sql += " ORDER BY p.razonsocial";
			
			Query q = getEntityManager().createQuery(sql);
			q.setParameter("idempresa", idEmpresa);
			
			if(!estadoRegistroEnum.equals(EstadoRegistroEnum.TODOS)) {
				q.setParameter("estado", estadoRegistroEnum.getInicial());
			}
			
			if(criterioBusqueda!=null && criterioBusqueda.trim().length()>0) {
				q.setParameter("identificacion", "%".concat(criterioBusqueda.toUpperCase()).concat("%"));
				q.setParameter("razonsocial", "%".concat(criterioBusqueda.toUpperCase()).concat("%"));
			}
			
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	
	/**
	 * @author Jorge Toaza
	 * @param idEstablecimiento
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Cliente> consultarClientes(String idempresa, String criteria) throws DaoException {
		try {
			
			String sql = "SELECT DISTINCT c "
					+ "FROM Cliente c "
					+ "WHERE "
					+ "c.empresa.idempresa=:idempresa ";
			
			if(criteria!=null && criteria.trim().length()>0) {
				sql += "AND (";
				sql += "c.identificacion like :identificacion ";
				sql += " OR UPPER(c.razonsocial) like :razonsocial ";
				sql += " OR c.telefono like :telefono ";
				sql += ") ";
			}
			sql += "ORDER BY c.razonsocial";
			
			Query q = getEntityManager().createQuery(sql);
			q.setParameter("idempresa", idempresa);
			if(criteria!=null && criteria.trim().length()>0) {
				q.setParameter("identificacion", "%".concat(criteria).concat("%"));
				q.setParameter("razonsocial", "%".concat(criteria.toUpperCase()).concat("%"));
				q.setParameter("telefono", "%".concat(criteria).concat("%"));
			}
			
			
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author Jorge Toaza
	 * @param identificacion
	 * @param idempresa
	 * @return
	 * @throws DaoException
	 */
	public boolean existeIdentificacion(String id, String identificacion, String idempresa) throws DaoException {
		try {
			
			Query q = null;
			
			if(id!=null) {
				q = getEntityManager().createQuery("SELECT c.identificacion FROM Cliente c WHERE c.idcliente<>:id AND c.identificacion=:identificacion AND c.empresa.idempresa=:idempresa ");
				q.setParameter("id", id);
			}else {
				q = getEntityManager().createQuery("SELECT c.identificacion FROM Cliente c WHERE c.identificacion=:identificacion AND c.empresa.idempresa=:idempresa ");
				
			}
			
			q.setParameter("identificacion", identificacion);
			q.setParameter("idempresa", idempresa);
			
			if(q.getResultList().size()>0) {
				return true;
			}
			
			return false;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @param identificador
	 * @param idEmpresa
	 * @return
	 * @throws DaoException
	 */
	public Cliente getByIdentificador(String identificador,String idEmpresa)throws DaoException{
		try {
			
			QueryBuilder q = new QueryBuilder(getEntityManager());
			return (Cliente) q.select("c")
				.from(Cliente.class,"c")
				.equals("c.empresa.idempresa",idEmpresa)
				.equals("c.identificacion",identificador).getSingleResult();
			
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
	public List<Cliente> getByEstado(EstadoRegistroEnum estadoRegistroEnum,String idEmpresa)throws DaoException{
		try {
			
			Query q = null;
			
			if(estadoRegistroEnum==null) {
				q = getEntityManager().createQuery("SELECT c FROM Cliente c WHERE c.empresa.idempresa=:idempresa ORDER BY c.razonsocial");
			}else {
				q = getEntityManager().createQuery("SELECT c FROM Cliente c WHERE c.estado=:estado AND c.empresa.idempresa=:idempresa ORDER BY c.razonsocial");
				q.setParameter("estado", estadoRegistroEnum.getInicial());
			}
			
			q.setParameter("idempresa", idEmpresa);
			
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
}