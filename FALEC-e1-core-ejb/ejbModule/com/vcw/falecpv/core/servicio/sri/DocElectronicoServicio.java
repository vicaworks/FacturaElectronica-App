/**
 * 
 */
package com.vcw.falecpv.core.servicio.sri;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.dao.impl.CabeceraDao;
import com.vcw.falecpv.core.exception.NoExisteRegistroException;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.xpert.persistence.query.QueryBuilder;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class DocElectronicoServicio {
	
	@Inject
	private CabeceraDao cabeceraDao;
	
	/**
	 * @param tipoComprobanteIdentificador
	 * @param idEstablecimiento
	 * @param numDocumento
	 * @return
	 * @throws DaoException
	 * @throws NoExisteRegistroException
	 */
	public String getIdDocElectronico(String tipoComprobanteIdentificador,String idEstablecimiento,String numDocumento)throws DaoException,NoExisteRegistroException{
		
		List<Cabecera> lista =  null;
		
		try {
			
			QueryBuilder qb = new QueryBuilder(cabeceraDao.getEntityManager());
			
			lista =  qb.select("c")
							.from(Cabecera.class,"c")
							.notEquals("c.estado", "ANULADO")
							.equals("c.establecimiento.idestablecimiento",idEstablecimiento)
							.equals("c.tipocomprobante.identificador",tipoComprobanteIdentificador)
							.equals("c.numdocumento",numDocumento).getResultList();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		
		if(lista.size()==0) {
			throw new NoExisteRegistroException("NO EXISTE : " + numDocumento);
		}
		
		return lista.get(0).getIdcabecera();
		
	}
	

}
