/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.constante.contadores.TipoPagoEnum;
import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Tipopago;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class TipopagoDao extends AppGenericDao<Tipopago, String> {

	/**
	 * @param type
	 */
	public TipopagoDao() {
		super(Tipopago.class);
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEmpresa
	 * @param tipoPago
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Tipopago> getByEmpresaFormulario(TipoPagoEnum tipoPago)throws DaoException{
		try {
			
			String sql = "SELECT t FROM Tipopago t WHERE t.formulario like :formulario ORDER BY t.nombre";
			Query q = getEntityManager().createQuery(sql);
			q.setParameter("formulario", "%".concat(tipoPago.getFormulario()).concat("%"));
			
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idempresa
	 * @param nombre
	 * @return
	 * @throws DaoException
	 */
	public Tipopago getByNombre(String nombre)throws DaoException{
		try {
			
			Query q = getEntityManager().createQuery("SELECT p FROM Tipopago p WHERE p.nombre=:nombre");
			q.setParameter("nombre", nombre);
			return (Tipopago) q.getSingleResult();
			
		} catch (NonUniqueResultException e) {
			return null;
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
