/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.dao.impl.CabeceraDao;
import com.vcw.falecpv.core.dao.impl.MotivoDao;
import com.vcw.falecpv.core.dao.impl.PagoDao;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Motivo;
import com.vcw.falecpv.core.modelo.persistencia.Pago;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class NotaDebitoServicio {

	@Inject
	private CabeceraDao cabeceraDao;
	
	@Inject
	private MotivoDao motivoDao;
	
	@Inject
	private PagoDao pagoDao;
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param desde
	 * @param hasta
	 * @param criteria
	 * @param idEstablecimiento
	 * @param estado
	 * @return
	 * @throws DaoException
	 */
	public List<Cabecera> getByCriteria(Date desde,Date hasta,String criteria,String idEstablecimiento,String estado)throws DaoException{
		try {
			
			List<Cabecera> ndList = cabeceraDao.getByNotDebitoCriteria(desde, hasta, criteria, idEstablecimiento, estado);
			
			if(ndList.isEmpty()) return new ArrayList<>();
			
			List<String> idCabeceraList = ndList.stream().map(x->x.getIdcabecera()).distinct().collect(Collectors.toList());
			// motivo
			List<Motivo> motivoList = motivoDao.getByIdCabecera(idCabeceraList);
			List<Pago> pagoList = pagoDao.getByIdCabecera(idCabeceraList);
			
			ndList.stream().forEach(x->{
				x.setMotivoList(motivoList.stream().filter(y->y.getCabecera().getIdcabecera().equals(x.getIdcabecera())).collect(Collectors.toList()));
				x.setPagoList(pagoList.stream().filter(y->y.getCabecera().getIdcabecera().equals(x.getIdcabecera())).collect(Collectors.toList()));
			});
			
			return ndList;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
