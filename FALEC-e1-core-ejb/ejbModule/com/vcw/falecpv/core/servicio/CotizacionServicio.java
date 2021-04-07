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
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Detalle;
import com.vcw.falecpv.core.modelo.query.EstadisticoQuery;
import com.vcw.falecpv.core.servicio.seg.SegperfilusuarioServicio;
import com.xpert.persistence.query.QueryBuilder;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class CotizacionServicio extends DBUtilGenericoApp {

	@Inject
	private CabeceraDao cabeceraDao;
	
	@Inject
	private DetalleDao detalleDao;
	
	@Inject
	private SegperfilusuarioServicio segperfilusuarioServicio;
	
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
	 * @param idEmpresa
	 * @param idusuario
	 * @param desde
	 * @param hasta
	 * @param criteria
	 * @param idEstablecimiento
	 * @param estadoList
	 * @return
	 * @throws DaoException
	 */
	public List<Cabecera> getByCriteria(String idEmpresa,String idusuario,String idCliente, Date desde,Date hasta,String criteria,String idEstablecimiento,List<String> estadoList)throws DaoException{
		try {
			
			List<Cabecera> ncList = cabeceraDao.getByCotizacionCriteria(idEmpresa,idusuario,idCliente,desde,hasta,criteria,idEstablecimiento,estadoList);
			
			if(ncList.isEmpty()) return new ArrayList<>();
			
			List<String> idCabeceraList = ncList.stream().map(x->x.getIdcabecera()).distinct().collect(Collectors.toList());
			List<Detalle> detalleList = detalleDao.getByIdCabecera(idCabeceraList);
			
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
	 * 
	 * @param desde
	 * @param hasta
	 * @param criteria
	 * @param idEstablecimiento
	 * @param estado
	 * @return
	 * @throws DaoException
	 */
	public List<Cabecera> getByCriteria(Date desde,Date hasta,String criteria,String idEstablecimiento,List<String> estadoList)throws DaoException{
		try {
			
			List<Cabecera> ncList = cabeceraDao.getByCotizacionCriteria(desde, hasta, criteria, idEstablecimiento,estadoList);
			
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
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEmpresa
	 * @param idEstablecimiento
	 * @param desde
	 * @param hasta
	 * @return
	 * @throws DaoException
	 */
	public List<EstadisticoQuery> getFacturadoValor(String idEmpresa,String idEstablecimiento, Date desde,Date hasta)throws DaoException{
		try {
			
			String sql = "select " + 
				"	c.estado as label1, " +
				" 	cast(sum(c.totalsinimpuestos) as numeric(12,2)) as valor1 " +
				" from  " +
				" 	cabecera c inner join cliente cl on c.idcliente = cl.idcliente " +
				" 	inner join tipocomprobante tc on tc.idtipocomprobante = c.idtipocomprobante " +
				" 	inner join establecimiento et on c.idestablecimiento=et.idestablecimiento  " +
				" where " +
				" 	c.estado in ('FACTURADO','SEGUIMIENTO','ARCHIVADO') " +
				" 	and tc.identificador = '99' " +
				" 	and c.fechaemision between '" + formatoFecha(desde) + "' and '" + formatoFecha(hasta) + "' " +
				(idEstablecimiento!=null? " 	and c.idestablecimiento = '" + idEstablecimiento + "' ": " ") +
				(idEstablecimiento==null?" 	and et.idempresa = '" + idEmpresa + "' ":" ") +
				" group by  " +
				" 	c.estado ";
			
			return resultList(sql, EstadisticoQuery.class, false);
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEmpresa
	 * @param idEstablecimiento
	 * @param desde
	 * @param hasta
	 * @return
	 * @throws DaoException
	 */
	public List<EstadisticoQuery> getFacturadoContador(String idEmpresa,String idEstablecimiento, Date desde,Date hasta)throws DaoException{
		try {
			
			String sql = "select " + 
					"	c.estado as label1, " +
					" 	cast(count(c.totalsinimpuestos) as numeric(12,0)) as valor1 " +
					" from  " +
					" 	cabecera c inner join cliente cl on c.idcliente = cl.idcliente " +
					" 	inner join tipocomprobante tc on tc.idtipocomprobante = c.idtipocomprobante " +
					" 	inner join establecimiento et on c.idestablecimiento=et.idestablecimiento  " +
					" where " +
					" 	c.estado in ('FACTURADO','SEGUIMIENTO','ARCHIVADO') " +
					" 	and tc.identificador = '99' " +
					" 	and c.fechaemision between '" + formatoFecha(desde) + "' and '" + formatoFecha(hasta) + "' " +
					(idEstablecimiento!=null? " 	and c.idestablecimiento = '" + idEstablecimiento + "' ": " ") +
					(idEstablecimiento==null?" 	and et.idempresa = '" + idEmpresa + "' ":" ") +
					" group by  " +
					" 	c.estado ";
			
			return resultList(sql, EstadisticoQuery.class, false);
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEmpresa
	 * @param idEstablecimiento
	 * @param desde
	 * @param hasta
	 * @return
	 * @throws DaoException
	 */
	public List<EstadisticoQuery> getFacturadoVendedorContador(String idEmpresa,String idEstablecimiento, Date desde,Date hasta)throws DaoException{
		try {
			
			String sql = "select " +
					"	us.nombrepantalla as label1, " +
					"	cast(count(c.totalsinimpuestos) filter (where c.estado='FACTURADO') as numeric(12,0)) as valor1, " +
					"	cast(count(c.totalsinimpuestos) filter (where c.estado='ARCHIVADO') as numeric(12,0)) as valor2, " +
					"	cast(count(c.totalsinimpuestos) filter (where c.estado='SEGUIMIENTO') as numeric(12,0)) as valor3 " +
					" from  " +
					"	cabecera c inner join cliente cl on c.idcliente = cl.idcliente " +
					"	inner join usuario us on us.idusuario = c.idusuario  " +
					"	inner join tipocomprobante tc on tc.idtipocomprobante = c.idtipocomprobante " +
					"	inner join establecimiento et on c.idestablecimiento=et.idestablecimiento  " +
					" where " +
					" 	c.estado in ('FACTURADO','SEGUIMIENTO','ARCHIVADO') " +
					" 	and tc.identificador = '99' " +
					" 	and c.fechaemision between '" + formatoFecha(desde) + "' and '" + formatoFecha(hasta) + "' " +
					(idEstablecimiento!=null? " 	and c.idestablecimiento = '" + idEstablecimiento + "' ": " ") +
					(idEstablecimiento==null?" 	and et.idempresa = '" + idEmpresa + "' ":" ") +
					" group by  " +
					"	us.nombrepantalla " + 
					" order by  " +
					"	us.nombrepantalla";
			
			return resultList(sql, EstadisticoQuery.class, false);
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEmpresa
	 * @param idEstablecimiento
	 * @param desde
	 * @param hasta
	 * @return
	 * @throws DaoException
	 */
	public List<EstadisticoQuery> getFacturadoVendedorValor(String idEmpresa,String idEstablecimiento, Date desde,Date hasta)throws DaoException{
		try {
			
			String sql = "select " +
					"	us.nombrepantalla as label1, " +
					"	coalesce(cast(sum(c.totalsinimpuestos) filter (where c.estado='FACTURADO') as numeric(12,0)),0) as valor1, " +
					"	coalesce(cast(sum(c.totalsinimpuestos) filter (where c.estado='ARCHIVADO') as numeric(12,0)),0) as valor2, " +
					"	coalesce(cast(sum(c.totalsinimpuestos) filter (where c.estado='SEGUIMIENTO') as numeric(12,0)),0) as valor3 " +
					" from  " +
					"	cabecera c inner join cliente cl on c.idcliente = cl.idcliente " +
					"	inner join usuario us on us.idusuario = c.idusuario  " +
					"	inner join tipocomprobante tc on tc.idtipocomprobante = c.idtipocomprobante " +
					"	inner join establecimiento et on c.idestablecimiento=et.idestablecimiento  " +
					" where " +
					" 	c.estado in ('FACTURADO','SEGUIMIENTO','ARCHIVADO') " +
					" 	and tc.identificador = '99' " +
					" 	and c.fechaemision between '" + formatoFecha(desde) + "' and '" + formatoFecha(hasta) + "' " +
					(idEstablecimiento!=null? " 	and c.idestablecimiento = '" + idEstablecimiento + "' ": " ") +
					(idEstablecimiento==null?" 	and et.idempresa = '" + idEmpresa + "' ":" ") +
					" group by  " +
					"	us.nombrepantalla " + 
					" order by  " +
					"	us.nombrepantalla";
			
			return resultList(sql, EstadisticoQuery.class, false);
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idUsuario
	 * @return
	 * @throws DaoException
	 */
	public boolean tienePerfilAdministrador(String idUsuario)throws DaoException{
		try {
			return segperfilusuarioServicio.getPerfilByUsuario(idUsuario, "1").stream().filter(x->x.getIdsegperfil().equals("4")).findFirst().orElse(null)!=null;
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
