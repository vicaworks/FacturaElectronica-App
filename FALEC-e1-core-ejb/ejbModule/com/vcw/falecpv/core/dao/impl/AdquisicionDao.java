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
	public List<Adquisicion> getByDateCriteria(String idEstablecimiento,Date fechaIni,Date fechaFin,String criteria,String estado)throws DaoException{
		try {
			
			Query q = null;
			
			if(criteria==null || criteria.trim().isEmpty()) {
				
				if(estado!=null) {
					q = getEntityManager().createQuery("SELECT a FROM Adquisicion a WHERE a.estado" + (estado.equals("I")?"=":"<>")  + "'ANULADO' AND a.establecimiento.idestablecimiento=:idestablecimeinto AND a.fecha BETWEEN :fechaIni AND :fechaFin ORDER BY a.fecha,a.cliente.razonsocial");
					
				}else {
					q = getEntityManager().createQuery("SELECT a FROM Adquisicion a WHERE a.establecimiento.idestablecimiento=:idestablecimeinto AND a.fecha BETWEEN :fechaIni AND :fechaFin ORDER BY a.fecha,a.cliente.razonsocial");
				}
				
				q.setParameter("fechaIni", fechaIni);
				q.setParameter("fechaFin", fechaFin);
				
			}else {
				
				q = getEntityManager().createQuery("SELECT a FROM Adquisicion a WHERE a.establecimiento.idestablecimiento=:idestablecimeinto AND (UPPER(a.cliente.razonsocial) like :nombrecomercial OR UPPER(a.cliente.razonsocial) like :razonsocial OR UPPER(a.cliente.identificacion) like :identificacion OR UPPER(a.numfactura) like :numfactura) ORDER BY a.fecha,a.cliente.razonsocial");
				q.setParameter("nombrecomercial", "%".concat(criteria.toUpperCase()).concat("%"));
				q.setParameter("razonsocial", "%".concat(criteria.toUpperCase()).concat("%"));
				q.setParameter("identificacion", "%".concat(criteria.toUpperCase()).concat("%"));
				q.setParameter("numfactura", "%".concat(criteria.toUpperCase()).concat("%"));
				
			}
			
			q.setParameter("idestablecimeinto", idEstablecimiento);
			
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
				q = getEntityManager().createQuery("SELECT a FROM Adquisicion a WHERE a.estado<>'ANULADO' AND a.establecimiento.idestablecimiento=:idestablecimiento AND a.cliente.idcliente=:idproveedor AND a.numfactura=:numfactura AND a.idadquisicion<>:idadquisicion");
				q.setParameter("idadquisicion", idadquisicion);
			}else {
				q = getEntityManager().createQuery("SELECT a FROM Adquisicion a WHERE a.estado<>'ANULADO' AND a.establecimiento.idestablecimiento=:idestablecimiento AND a.cliente.idcliente=:idproveedor AND a.numfactura=:numfactura");
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
