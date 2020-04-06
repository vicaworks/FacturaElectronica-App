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
				sql += " OR c.razonsocial like :razonsocial ";
				sql += " OR c.telefono like :telefono ";
				sql += ") ";
			}
			sql += "ORDER BY c.razonsocial";
			
			Query q = getEntityManager().createQuery(sql);
			q.setParameter("idempresa", idempresa);
			if(criteria!=null && criteria.trim().length()>0) {
				q.setParameter("identificacion", "%".concat(criteria).concat("%"));
				q.setParameter("razonsocial", "%".concat(criteria).concat("%"));
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
	public boolean existeIdentificacion(String identificacion, String idempresa) throws DaoException {
		try {
			
			Query q = null;

			q = getEntityManager().createQuery("SELECT c.identificacion FROM Cliente c WHERE c.identificacion=:identificacion AND c.empresa.idempresa=:idempresa ");
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
}