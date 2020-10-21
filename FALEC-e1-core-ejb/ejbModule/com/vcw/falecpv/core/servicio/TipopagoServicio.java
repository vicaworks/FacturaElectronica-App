/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.constante.TipoPagoFormularioEnum;
import com.vcw.falecpv.core.dao.impl.TipopagoDao;
import com.vcw.falecpv.core.modelo.persistencia.Tipopago;
import com.xpert.persistence.query.QueryBuilder;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class TipopagoServicio extends AppGenericService<Tipopago, String> {

	
	@Inject
	private TipopagoDao tipopagoDao; 
	
	@Override
	public List<Tipopago> consultarActivos() {
		return null;
	}

	@Override
	public List<Tipopago> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Tipopago, String> getDao() {
		return null;
	}

	/**
	 * @return the tipopagoDao
	 */
	public TipopagoDao getTipopagoDao() {
		return tipopagoDao;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param tipoPagoEnum
	 * @param idEmpresa
	 * @return
	 * @throws DaoException
	 */
	public Tipopago getByCodINterno(TipoPagoFormularioEnum tipoPagoFormularioEnum)throws DaoException{
		try {
			
			QueryBuilder q = new QueryBuilder(tipopagoDao.getEntityManager());
			
			return (Tipopago)q.select("p")
						.from(Tipopago.class,"p")
						.equals("p.codinterno",tipoPagoFormularioEnum.getCodInterno()).getSingleResult();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @return
	 * @throws DaoException
	 */
	public List<Tipopago> getALL()throws DaoException{
		try {
			QueryBuilder q = new QueryBuilder(tipopagoDao.getEntityManager());
			return q.select("t")
					.from(Tipopago.class,"t")
					.orderBy("t.orden").getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param codigo
	 * @return
	 * @throws DaoException
	 */
	public String tipopagoSri(String codigo)throws DaoException{
		try {
			
			QueryBuilder qb = new QueryBuilder(tipopagoDao.getEntityManager());
			List<Tipopago> r = qb.select("p")
				.from(Tipopago.class,"p")
				.equals("p.codigo",codigo).getResultList();
			
			if(r.isEmpty()) {
				return "";
			}
			
			return r.get(0).getDescripcion();
			
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}


}
