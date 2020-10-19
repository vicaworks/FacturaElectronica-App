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
import com.vcw.falecpv.core.dao.impl.ComprobanterecibidoDao;
import com.vcw.falecpv.core.modelo.persistencia.Comprobanterecibido;
import com.xpert.persistence.query.QueryBuilder;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class ComprobanterecibidoServicio extends AppGenericService<Comprobanterecibido, String> {
	
	@Inject
	private ComprobanterecibidoDao  comprobanterecibidoDao;

	@Override
	public List<Comprobanterecibido> consultarActivos() {
		return null;
	}

	@Override
	public List<Comprobanterecibido> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Comprobanterecibido, String> getDao() {
		return comprobanterecibidoDao;
	}

	/**
	 * @return the comprobanterecibidoDao
	 */
	public ComprobanterecibidoDao getComprobanterecibidoDao() {
		return comprobanterecibidoDao;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEmpresa
	 * @param tipoComprobante
	 * @param claveAcceso
	 * @param numComprobante
	 * @return
	 * @throws DaoException
	 */
	public Comprobanterecibido getByComprobanteEmpresa(String idEmpresa,GenTipoDocumentoEnum tipoComprobante,String claveAcceso,String numComprobante)throws DaoException{
		try {
			
			QueryBuilder qb = new QueryBuilder(comprobanterecibidoDao.getEntityManager());
			
			List<Comprobanterecibido> lista =	qb.select("c")
					.from(Comprobanterecibido.class, "c")
					.equals("c.empresa.idempresa", idEmpresa)
					.equals("c.serieComprobante", numComprobante)
					.equals("c.tipocomprobante.identificador", tipoComprobante.getIdentificador())
					.equals("c.claveAcceso",claveAcceso).getResultList();
			
			if(lista.isEmpty()) {
				return null;
			}
			
			return lista.get(0);
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
