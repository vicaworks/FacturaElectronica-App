/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.constante.contadores.TipoComprobanteEnum;
import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Tipocomprobante;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class TipocomprobanteDao extends AppGenericDao<Tipocomprobante, String> {

	/**
	 * @param type
	 */
	public TipocomprobanteDao() {
		super(Tipocomprobante.class);
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEmpresa
	 * @param tipoComprobanteEnum
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Tipocomprobante> getByEmpresaFormulario(String idEmpresa, TipoComprobanteEnum tipoComprobanteEnum)throws DaoException{
		try {
			
			String sql = "SELECT t FROM Tipocomprobante t WHERE t.empresa.idempresa=:idempresa AND t.formularios like :formulario ORDER BY t.comprobante";
			Query q = getEntityManager().createQuery(sql);
			q.setParameter("idempresa", idEmpresa);
			q.setParameter("formulario", "%".concat(tipoComprobanteEnum.getSigla()).concat("%"));
			
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
