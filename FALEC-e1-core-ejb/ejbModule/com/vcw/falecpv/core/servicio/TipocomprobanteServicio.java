/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;
import com.vcw.falecpv.core.dao.impl.TipocomprobanteDao;
import com.vcw.falecpv.core.modelo.persistencia.Tipocomprobante;
import com.xpert.persistence.query.QueryBuilder;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class TipocomprobanteServicio extends AppGenericService<Tipocomprobante, String> {

	@Inject
	private TipocomprobanteDao tipocomprobanteDao;
	
	@Override
	public List<Tipocomprobante> consultarActivos() {
		return null;
	}

	@Override
	public List<Tipocomprobante> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Tipocomprobante, String> getDao() {
		return tipocomprobanteDao;
	}

	/**
	 * @return the tipocomprobanteDao
	 */
	public TipocomprobanteDao getTipocomprobanteDao() {
		return tipocomprobanteDao;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param genTipoDocumentoEnum
	 * @param idEmpresa
	 * @return
	 * @throws DaoException
	 */
	public Tipocomprobante getByTipoDocumento(GenTipoDocumentoEnum genTipoDocumentoEnum)throws DaoException{
		try {
			
			QueryBuilder q = new QueryBuilder(tipocomprobanteDao.getEntityManager());
			
			return (Tipocomprobante) q.select("d")
						.from(Tipocomprobante.class,"d")
						.equals("d.identificador",genTipoDocumentoEnum.getIdentificador()).getSingleResult();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
