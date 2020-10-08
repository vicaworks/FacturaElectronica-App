/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.constante.ComprobanteEstadoEnum;
import com.vcw.falecpv.core.dao.impl.CabeceraDao;
import com.vcw.falecpv.core.exception.EstadoComprobanteException;
import com.vcw.falecpv.core.modelo.persistencia.Adquisicion;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Impuestoretencion;
import com.xpert.persistence.query.QueryBuilder;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class CabeceraRetencionServicio {

	
	@Inject
	private CabeceraDao cabeceraDao;
	
	@Inject
	private AdquisicionServicio adquisicionServicio;
	
	private List<ComprobanteEstadoEnum> estadosAnulacion = Arrays.asList(new ComprobanteEstadoEnum[] {ComprobanteEstadoEnum.BORRADOR,ComprobanteEstadoEnum.ERROR,ComprobanteEstadoEnum.ERROR_SRI,ComprobanteEstadoEnum.AUTORIZADO});
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idRetencion
	 * @return
	 * @throws DaoException
	 */
	public Cabecera anularRetencion(String idRetencion)throws DaoException{
		try {
			
			QueryBuilder q = new QueryBuilder(cabeceraDao.getEntityManager());
			Cabecera r = (Cabecera) q.select("r")
					.from(Cabecera.class,"r")
					.equals("r.idcabecera",idRetencion).getSingleResult();
			
			if(r==null ||  !estadosAnulacion.contains(ComprobanteEstadoEnum.getByEstado(r.getEstado()))) {
				throw new EstadoComprobanteException("NO SE PUEDE ANULAR SE ENCUENTRA EN ESTADO:" + r.getEstado());
			}
			
			r.setEstado("ANULADO");
			if (r.getAdquisicion()!=null) {
				Adquisicion a = adquisicionServicio.consultarByPk(r.getAdquisicion().getIdadquisicion());
				a.setEstado(ComprobanteEstadoEnum.REGISTRADO.toString());
				a.setTotalretencion(BigDecimal.ZERO);
				a.setTotalpagar(a.getTotalfactura());
				adquisicionServicio.actualizar(a);
			}
			
			r.setAdquisicion(null);
			cabeceraDao.actualizar(r);
			
			if (ComprobanteEstadoEnum.getByEstado(r.getEstado()).equals(ComprobanteEstadoEnum.BORRADOR)) {
				
				cabeceraDao.eliminacionFisica(r);
				
				return null;
			}
			
			return r;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idCabecera
	 * @return
	 * @throws DaoException
	 */
	public List<Impuestoretencion> getDetalleById(String idCabecera)throws DaoException{
		try {
			
			QueryBuilder q = new QueryBuilder(cabeceraDao.getEntityManager());
			return q.select("d")
				.from(Impuestoretencion.class,"d")
				.equals("d.cabecera.idcabecera", idCabecera).getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idAdquisicion
	 * @return
	 * @throws DaoException
	 */
	public Cabecera getByAdquisicionEstado(String idAdquisicion)throws DaoException{
		try {
			QueryBuilder q = new QueryBuilder(cabeceraDao.getEntityManager());
			return (Cabecera) q.select("r")
					.from(Cabecera.class,"r")
					.equals("r.adquisicion.idadquisicion",idAdquisicion)
					.notEquals("r.estado", ComprobanteEstadoEnum.ANULADO.toString()).getSingleResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idRetencion
	 * @param idEstablecimiento
	 * @param idProveedor
	 * @param numFactura
	 * @return
	 * @throws DaoException
	 */
	public boolean existeRetencionProveedor(String idRetencion,String idEstablecimiento,String idProveedor,String numFactura)throws DaoException{
		try {
			
			QueryBuilder q = new QueryBuilder(cabeceraDao.getEntityManager());
			
			return q.select("r")
					.from(Cabecera.class,"r")
					.equals("r.establecimiento.idestablecimiento",idEstablecimiento)
					.equals("r.cliente.idcliente",idProveedor)
					.equals("r.numfactura",numFactura)
					.notEquals("r.idcabecera", idRetencion==null?"-1":idRetencion)
					.notEquals("r.estado", ComprobanteEstadoEnum.ANULADO.toString()).getResultList().size()>0;
			
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	

}
