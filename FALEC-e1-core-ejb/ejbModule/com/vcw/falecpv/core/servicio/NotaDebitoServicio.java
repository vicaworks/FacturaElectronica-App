/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.constante.ComprobanteEstadoEnum;
import com.vcw.falecpv.core.dao.impl.CabeceraDao;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.xpert.persistence.query.QueryBuilder;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class NotaDebitoServicio {

	@Inject
	private CabeceraDao cabeceraDao;
	
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
				lista.add(ComprobanteEstadoEnum.AUTORIZACION);
				lista.add(ComprobanteEstadoEnum.SRI);
				
				if(lista.contains(ComprobanteEstadoEnum.getByEstado(r.getEstado()))) {
					return "NO SE PUEDE ANULAR, POR QUE SE ENCUENTRA EN ESTADO: " + r.getEstado();
				}
				
			}
			
			if(r!=null && accion.equals("ELIMINAR_DETALLE")) {
				List<ComprobanteEstadoEnum> lista = new ArrayList<>();
				lista.add(ComprobanteEstadoEnum.ANULADO);
				lista.add(ComprobanteEstadoEnum.AUTORIZACION);
				lista.add(ComprobanteEstadoEnum.SRI);
				
				if(lista.contains(ComprobanteEstadoEnum.getByEstado(r.getEstado()))) {
					return "NO SE PUEDE ELIMINAR, POR QUE SE ENCUENTRA EN ESTADO: " + r.getEstado();
				}
				
			}
			
			if(r!=null && accion.equals("GUARDAR")) {
				List<ComprobanteEstadoEnum> lista = new ArrayList<>();
				lista.add(ComprobanteEstadoEnum.ANULADO);
				lista.add(ComprobanteEstadoEnum.AUTORIZACION);
				lista.add(ComprobanteEstadoEnum.SRI);
				
				if(lista.contains(ComprobanteEstadoEnum.getByEstado(r.getEstado()))) {
					return "NO SE PUEDE REALIZAR NINGUNA MODIFICACION, POR QUE SE ENCUENTRA EN ESTADO: " + r.getEstado();
				}
				
			}
			
			
			return null;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
