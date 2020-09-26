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
import com.vcw.falecpv.core.dao.impl.DetalleDao;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Detalle;
import com.xpert.persistence.query.QueryBuilder;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class CotizacionServicio {

	@Inject
	private CabeceraDao cabeceraDao;
	
	@Inject
	private DetalleDao detalleDao;
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idCotizacion
	 * @param idEstablecimiento
	 * @param accion
	 * @return
	 * @throws DaoException
	 */
	public String analizarEstado(String idCotizacion,String accion)throws DaoException{
		try {
			
			QueryBuilder q = new QueryBuilder(cabeceraDao.getEntityManager());
			Cabecera r = (Cabecera) q.select("r")
							.from(Cabecera.class,"r")
							.equals("r.idcabecera",idCotizacion).getSingleResult();
			
			if(r==null) {
				return "NO EXISTE LA NOTA CREDITO";
			}
			
			if(r!=null && accion.equals("ARCHIVADO")) {
				List<String> lista = new ArrayList<>();
				lista.add("ARCHIVADO");
				lista.add("FACTURADO");
				
				if(lista.contains(r.getEstado())) {
					return "NO SE PUEDE ANULAR, POR QUE SE ENCUENTRA EN ESTADO: " + r.getEstado();
				}
				
			}
			
			if(r!=null && accion.equals("SEGUIMIENTO")) {
				List<String> lista = new ArrayList<>();
				lista.add("SEGUIMIENTO");
				
				if(lista.contains(r.getEstado())) {
					return "NO SE PUEDE ELIMINAR, POR QUE SE ENCUENTRA EN ESTADO: " + r.getEstado();
				}
				
			}
			
			if(r!=null && accion.equals("GUARDAR")) {
				List<String> lista = new ArrayList<>();
				lista.add("ARCHIVADO");
				lista.add("FACTURADO");
				
				if(lista.contains(r.getEstado())) {
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
			
			List<Cabecera> ncList = cabeceraDao.getByCotizacionCriteria(desde, hasta, criteria, idEstablecimiento,estado);
			
			if(ncList.isEmpty()) return new ArrayList<>();
			
			List<String> idCabeceraList = ncList.stream().map(x->x.getIdcabecera()).distinct().collect(Collectors.toList());
			List<Detalle> detalleList = detalleDao.getByIdCabecera(idCabeceraList);
//			List<Pago> pagoList = pagoDao.getByIdCabecera(idCabeceraList);
			
			ncList.stream().forEach(x->{
				x.setDetalleList(detalleList.stream().filter(y->y.getCabecera().getIdcabecera().equals(x.getIdcabecera())).collect(Collectors.toList()));
			});
			
			return ncList;
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * @param idCotizacion
	 * @throws DaoException
	 */
	public void archivarCotizacion(String idCotizacion)throws DaoException{
		try {
			
			Cabecera p = cabeceraDao.cargar(idCotizacion);
			p.setEstado("ARCHIVADO");
			
			cabeceraDao.actualizar(p);
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
		
	}

}
