/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import java.util.Date;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Tareaetiqueta;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class TareaetiquetaDao extends AppGenericDao<Tareaetiqueta, String> {

	/**
	 * @param type
	 */
	public TareaetiquetaDao() {
		super(Tareaetiqueta.class);
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEmpresa
	 * @return
	 * @throws DaoException
	 */
	public int actualizarEstadoVencido(String idEmpresa)throws DaoException{
		try {
			
			Query q = getEntityManager().createQuery("UPDATE tareacabecera "
					+ " SET estado='VENCIDO' "
					+ " WHERE idcabecera in (select distinct idcabecera from cabecera c inner join establecimiento e2 on c.idestablecimiento = e2.idestablecimiento where idempresa = :idempresa and idtipocomprobante = '10' and c.estado <> 'ARCHIVADO') "
					+ " and estado='PENDIENTE' "
					+ " and fechalimite<:fecha");
			q.setParameter("idempresa", idEmpresa);
			q.setParameter("fecha", new Date());
			return q.executeUpdate();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
