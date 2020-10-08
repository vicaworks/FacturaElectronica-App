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
import com.vcw.falecpv.core.dao.DBUtilGenericoApp;
import com.vcw.falecpv.core.dao.impl.CabeceraDao;
import com.vcw.falecpv.core.dao.impl.DetalleDao;
import com.vcw.falecpv.core.dao.impl.PagoDao;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Detalle;
import com.vcw.falecpv.core.modelo.persistencia.Pago;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class LiqCompraServicio extends DBUtilGenericoApp {
	
	@Inject
	private CabeceraDao cabeceraDao;
	
	@Inject
	private DetalleDao detalleDao;
	
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
	public List<Cabecera> getByLiqCompraCriteria(Date desde,Date hasta,String criteria,String idEstablecimiento,String estado)throws DaoException{
		try {
			
			List<Cabecera> liqCompraList = cabeceraDao.getByLiqCompraCriteria(desde, hasta, criteria, idEstablecimiento, estado);
			
			if(liqCompraList.isEmpty()) return new ArrayList<>();
			
			
			List<String> idCabeceraList = liqCompraList.stream().map(x->x.getIdcabecera()).distinct().collect(Collectors.toList());
			List<Detalle> detalleList = detalleDao.getByIdCabecera(idCabeceraList);
			List<Pago> pagoList = pagoDao.getByIdCabecera(idCabeceraList);
			
			if(liqCompraList!=null) {
				liqCompraList.stream().forEach(lc->{
					lc.setDetalleList(detalleList.stream().filter(x->x.getCabecera().getIdcabecera().equals(lc.getIdcabecera())).collect(Collectors.toList()));
					lc.setPagoList(pagoList.stream().filter(x->x.getCabecera().getIdcabecera().equals(lc.getIdcabecera())).collect(Collectors.toList()));
				});
			}
			
			return liqCompraList;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	
}
