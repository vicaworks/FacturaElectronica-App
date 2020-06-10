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
import com.vcw.falecpv.core.modelo.persistencia.Proveedor;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class ProveedorDao extends AppGenericDao<Proveedor, String> {

	public ProveedorDao() {
		super(Proveedor.class);
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEmpresa
	 * @param estadoRegistroEnum
	 * @param criterioBusqueda
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Proveedor> getByConsulta(String idEmpresa,EstadoRegistroEnum estadoRegistroEnum,String criterioBusqueda)throws DaoException{
		try {
			
			
			String sql = "SELECT p FROM Proveedor p WHERE p.empresa.idempresa=:idempresa ";
			
			if(!estadoRegistroEnum.equals(EstadoRegistroEnum.TODOS)) {
				sql += " AND p.estado=:estado ";
			}
			
			if(criterioBusqueda!=null && criterioBusqueda.trim().length()>0) {
				sql += " AND (p.identificacion like :identificacion OR  upper(p.razonsocial) like :razonsocial OR upper(p.nombrecomercial) like :nombrecomercial OR upper(p.contactonombre) like :nombrecontacto) ";
			}
			
			sql += " ORDER BY p.nombrecomercial";
			
			Query q = getEntityManager().createQuery(sql);
			q.setParameter("idempresa", idEmpresa);
			
			if(!estadoRegistroEnum.equals(EstadoRegistroEnum.TODOS)) {
				q.setParameter("estado", estadoRegistroEnum.getInicial());
			}
			
			if(criterioBusqueda!=null && criterioBusqueda.trim().length()>0) {
				q.setParameter("identificacion", "%".concat(criterioBusqueda.toUpperCase()).concat("%"));
				q.setParameter("razonsocial", "%".concat(criterioBusqueda.toUpperCase()).concat("%"));
				q.setParameter("nombrecomercial", "%".concat(criterioBusqueda.toUpperCase()).concat("%"));
				q.setParameter("nombrecontacto", "%".concat(criterioBusqueda.toUpperCase()).concat("%"));
			}
			
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
