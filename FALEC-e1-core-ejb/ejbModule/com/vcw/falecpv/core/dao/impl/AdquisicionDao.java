/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Adquisicion;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class AdquisicionDao extends AppGenericDao<Adquisicion, String> {

	/**
	 * @param type
	 */
	public AdquisicionDao() {
		super(Adquisicion.class);
	}

	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEstablecimiento
	 * @param fechaIni
	 * @param fechaFin
	 * @param criteria
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Adquisicion> getByDateCriteria(String idEstablecimiento,Date fechaIni,Date fechaFin,String criteria)throws DaoException{
		try {
			
			String sql = "SELECT a FROM Adquisicion a WHERE a.establecimiento.idestablecimiento=:idestablecimeinto AND a.fecha BETWEEN :fechaIni AND :fechaFin ";
			if(criteria!=null && criteria.trim().length()>0) {
				sql += " AND (UPPER(a.proveedor.nombrecomercial) like :nombrecomercial OR UPPER(a.proveedor.razonsocial) like :razonsocial OR UPPER(a.proveedor.identificacion) like :identificacion OR UPPER(a.numfactura) like :numfactura) ";
			}
			sql += " ORDER BY a.fecha,a.proveedor.nombrecomercial";
			Query q = getEntityManager().createQuery(sql);
			q.setParameter("idestablecimeinto", idEstablecimiento);
			q.setParameter("fechaIni", fechaIni);
			q.setParameter("fechaFin", fechaFin);
			if(criteria!=null && criteria.trim().length()>0) {
				q.setParameter("nombrecomercial", "%".concat(criteria.toUpperCase()).concat("%"));
				q.setParameter("razonsocial", "%".concat(criteria.toUpperCase()).concat("%"));
				q.setParameter("identificacion", "%".concat(criteria.toUpperCase()).concat("%"));
				q.setParameter("numfactura", "%".concat(criteria.toUpperCase()).concat("%"));
			}
			
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idestablecimiento
	 * @param idproveedor
	 * @param idadquisicion
	 * @param numfactura
	 * @return
	 * @throws DaoException
	 */
	public boolean existeFacturaProveedor(String idestablecimiento,String idproveedor,String idadquisicion,String numfactura)throws DaoException{
		try {
			
			Query q = null;
			if(idadquisicion!=null) {
				q = getEntityManager().createQuery("SELECT a FROM Adquisicion a WHERE a.estado='GEN' AND a.establecimiento.idestablecimiento=:idestablecimiento AND a.proveedor.idproveedor=:idproveedor AND a.numfactura=:numfactura AND a.idadquisicion<>:idadquisicion");
				q.setParameter("idadquisicion", idadquisicion);
			}else {
				q = getEntityManager().createQuery("SELECT a FROM Adquisicion a WHERE a.estado='GEN' AND a.establecimiento.idestablecimiento=:idestablecimiento AND a.proveedor.idproveedor=:idproveedor AND a.numfactura=:numfactura");
			}
			
			q.setParameter("idestablecimiento", idestablecimiento);
			q.setParameter("idproveedor", idproveedor);
			q.setParameter("numfactura", numfactura);
			
			if(q.getResultList().size()>0) {
				return true;
			}
			
			return false;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
}
