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
import com.vcw.falecpv.core.constante.ComprobanteEstadoEnum;
import com.vcw.falecpv.core.dao.impl.CabeceraDao;
import com.vcw.falecpv.core.dao.impl.MotivoDao;
import com.vcw.falecpv.core.dao.impl.PagoDao;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Motivo;
import com.vcw.falecpv.core.modelo.persistencia.Pago;
import com.xpert.persistence.query.QueryBuilder;

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
	 * @param idretencion
	 * @param idEstablecimiento
	 * @param accion
	 * @return
	 * @throws DaoException
	 */
	public String analizarEstado(String idretencion,String idEstablecimiento,String accion)throws DaoException{
		try {
			
			QueryBuilder q = new QueryBuilder(cabeceraDao.getEntityManager());
			Cabecera r = (Cabecera) q.select("r")
							.from(Cabecera.class,"r")
							.equals("r.idcabecera",idretencion)
							.equals("r.establecimiento.idestablecimiento",idEstablecimiento).getSingleResult();
			
			if(r==null) {
				return "NO EXISTE LA GUIA REMISION";
			}
			
			if(r!=null && accion.equals("ANULAR")) {
				List<ComprobanteEstadoEnum> lista = new ArrayList<>();
				lista.add(ComprobanteEstadoEnum.ANULADO);
				lista.add(ComprobanteEstadoEnum.AUTORIZADO);
				lista.add(ComprobanteEstadoEnum.RECIBIDO_SRI);
				lista.add(ComprobanteEstadoEnum.PENDIENTE);
				
				if(lista.contains(ComprobanteEstadoEnum.getByEstado(r.getEstado()))) {
					return "NO SE PUEDE REALIZAR NINGUNA MODIFICACION, POR QUE SE ENCUENTRA EN ESTADO: " + r.getEstado();
				}
				
			}
			
			if(r!=null && accion.equals("ELIMINAR_DETALLE")) {
				List<ComprobanteEstadoEnum> lista = new ArrayList<>();
				lista.add(ComprobanteEstadoEnum.ANULADO);
				lista.add(ComprobanteEstadoEnum.AUTORIZADO);
				lista.add(ComprobanteEstadoEnum.RECIBIDO_SRI);
				lista.add(ComprobanteEstadoEnum.PENDIENTE);
				
				if(lista.contains(ComprobanteEstadoEnum.getByEstado(r.getEstado()))) {
					return "NO SE PUEDE ELIMINAR, POR QUE SE ENCUENTRA EN ESTADO: " + r.getEstado();
				}
				
			}
			
			if(r!=null && accion.equals("GUARDAR")) {
				List<ComprobanteEstadoEnum> lista = new ArrayList<>();
				lista.add(ComprobanteEstadoEnum.ANULADO);
				lista.add(ComprobanteEstadoEnum.AUTORIZADO);
				lista.add(ComprobanteEstadoEnum.RECIBIDO_SRI);
				lista.add(ComprobanteEstadoEnum.PENDIENTE);
				
				if(lista.contains(ComprobanteEstadoEnum.getByEstado(r.getEstado()))) {
					return "NO SE PUEDE REALIZAR NINGUNA MODIFICACION, POR QUE SE ENCUENTRA EN ESTADO: " + r.getEstado();
				}
				
			}
			
			
			return null;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
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
