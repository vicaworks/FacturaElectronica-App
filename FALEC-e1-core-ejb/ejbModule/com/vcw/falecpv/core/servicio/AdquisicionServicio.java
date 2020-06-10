/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.FechaUtil;
import com.vcw.falecpv.core.constante.ComprobanteEstadoEnum;
import com.vcw.falecpv.core.constante.contadores.TCAdquisicion;
import com.vcw.falecpv.core.dao.impl.AdquisicionDao;
import com.vcw.falecpv.core.dao.impl.AdquisiciondetalleDao;
import com.vcw.falecpv.core.modelo.persistencia.Adquisicion;
import com.vcw.falecpv.core.modelo.persistencia.Adquisiciondetalle;
import com.vcw.falecpv.core.modelo.persistencia.KardexProducto;
import com.vcw.falecpv.core.modelo.persistencia.Pagodetalle;
import com.vcw.falecpv.core.modelo.persistencia.Producto;
import com.xpert.persistence.query.QueryBuilder;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class AdquisicionServicio extends AppGenericService<Adquisicion, String> {
	
	@Inject
	private AdquisicionDao adquisicionDao;
	
	@Inject
	private AdquisiciondetalleDao adquisiciondetalleDao;
	
	@EJB
	private ContadorPkServicio contadorPkServicio;
	
	@EJB
	private KardexProductoServicio kardexProductoServicio;
	
	@EJB
	private ProductoServicio productoServicio;
	
	@EJB
	private AdquisiciondetalleServicio adquisiciondetalleServicio;
	
	@EJB
	private PagodetalleServicio pagodetalleServicio;
	
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param adquisicion
	 * @param adquisiciondetalleList
	 * @return
	 * @throws DaoException
	 */
	public Adquisicion guadarFacade(Adquisicion adquisicion,List<Adquisiciondetalle> adquisiciondetalleList,List<Pagodetalle> pagodetalleList)throws DaoException{
		try {
			
			// datos de la adquisicion
			
			if(adquisicion.getIdadquisicion()==null) {
				adquisicion.setIdadquisicion(contadorPkServicio.generarContadorTabla(TCAdquisicion.ADQUISICION,
						adquisicion.getEstablecimiento().getIdestablecimiento()));
				crear(adquisicion);
			}else {
				actualizar(adquisicion);
			}
			
			// detalle de cada adquisicion
			for (Adquisiciondetalle d : adquisiciondetalleList) {
				d.setAdquisicion(adquisicion);
				if(d.getIdadquisiciondetalle()==null || d.getIdadquisiciondetalle().contains("MM")) {
					d.setIdadquisiciondetalle(contadorPkServicio.generarContadorTabla(TCAdquisicion.ADQUISICIONDETALLE, adquisicion.getEstablecimiento().getIdestablecimiento()));
					adquisiciondetalleDao.guardar(d);
				}else {
					adquisiciondetalleDao.actualizar(d);
				}
				
				// registrar entrada de cada producto
				if(d.getProducto()!=null && d.getProducto().getTipoProducto().getNombre().equals("PRODUCTO")) {
					// ingreso de kardex
					KardexProducto k = kardexProductoServicio.getByEstablecimientoModulo(d.getProducto().getIdproducto(), adquisicion.getEstablecimiento().getIdestablecimiento(), d.getIdadquisiciondetalle(), "ADQ");
					int cantidad = d.getCantidad();
					if(k!=null && d.getCantidad()>k.getCantidad().intValue()) {
						cantidad = d.getCantidad() - k.getCantidad().intValue();
						
						k.setCantidad(BigDecimal.valueOf(d.getCantidad()));
						k.setSaldo(k.getSaldo().add(BigDecimal.valueOf(cantidad)));
						Producto p = productoServicio.consultarByPk(d.getProducto().getIdproducto());
						p.setStock(p.getStock().add(BigDecimal.valueOf(cantidad)));
						kardexProductoServicio.actualizar(k);
						productoServicio.actualizar(p);
					}else if(k!=null && d.getCantidad()<k.getCantidad().intValue()) {
						cantidad = k.getCantidad().intValue() - d.getCantidad();
						
						k.setCantidad(BigDecimal.valueOf(d.getCantidad()));
						k.setSaldo(k.getSaldo().add(BigDecimal.valueOf(cantidad).negate()));
						Producto p = productoServicio.consultarByPk(d.getProducto().getIdproducto());
						p.setStock(p.getStock().add(BigDecimal.valueOf(cantidad).negate()));
						kardexProductoServicio.actualizar(k);
						productoServicio.actualizar(p);
					}else if(k==null) {
						// registra la entrada de kardex
						k = new KardexProducto();
						k.setCantidad(BigDecimal.valueOf(d.getCantidad()));
						k.setEstablecimiento(adquisicion.getEstablecimiento());
						k.setFechacompra(adquisicion.getFecha());
						k.setFecharegistro(adquisicion.getFecha());
						k.setIdreferencia(d.getIdadquisiciondetalle());
						k.setIdusuario(adquisicion.getIdusuario());
						k.setModuloreferencia("ADQ");
						k.setProducto(d.getProducto());
						k.setTiporegistro("E");
						k.setUpdated(new Date());
						k.setCostounitario(d.getPreciounitario());
						k.setFechafabricacion(d.getProducto().getFechafabricacion());
						k.setFechavencimiento(d.getProducto().getFechavencimiento());
						StringBuilder obs = new StringBuilder();
						obs.append("COMPRA ");
						obs.append(" / PROVEEDOR : " + adquisicion.getProveedor().getNombrecomercial());
						obs.append(" / FACTURA : " + adquisicion.getNumfactura());
						obs.append(" / FECHA : " + FechaUtil.formatoFecha(adquisicion.getFecha()));
						obs.append(" / PAGO : " + adquisicion.getTipopago().getNombre());
						k.setObservacion(obs.toString());
						kardexProductoServicio.registrarKardexFacade(k);
					}
					
				}
				
			}
			
			// detalle del pago
			if(pagodetalleList!=null && !adquisicion.getTipopago().getNombre().equals("EFECTIVO")) {
				for (Pagodetalle pd : pagodetalleList) {
					pd.setUpdated(new Date());
					if(pd.getIdpagodetalle().contains("MM") || pd.getIdpagodetalle()==null) {
						pd.setIdpagodetalle(contadorPkServicio.generarContadorTabla(TCAdquisicion.ADQUISICIONDETALLE, adquisicion.getEstablecimiento().getIdestablecimiento()));
						pagodetalleServicio.crear(pd);
					}else {
						pagodetalleServicio.actualizar(pd);
					}
				}
				
			}
			
			// si pago efectivo 
			if(adquisicion.getTipopago().getNombre().equals("EFECTIVO")) {
				pagodetalleServicio.eliminarByAdquisicion(adquisicion.getIdadquisicion());
			}
			
			return adquisicion;
			
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
	public List<Adquisiciondetalle> getByAdquisicion(String idAdquisicion)throws DaoException{
		try {
			QueryBuilder q = new QueryBuilder(adquisicionDao.getEntityManager());
			return q.select("d")
				.from(Adquisiciondetalle.class,"d")
				.equals("d.adquisicion.idadquisicion",idAdquisicion)
				.orderBy("d.descripcion").getResultList();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idAdquisicion
	 * @param idUsuario
	 * @param eliminar
	 * @throws DaoException
	 */
	public void anularCompra(String idAdquisicion,String idUsuario,boolean eliminar)throws DaoException{
		try {
			
			Adquisicion a = consultarByPk(idAdquisicion);
			
			// reversa el kardes de cada uno de los detalles
			List<Adquisiciondetalle> adList = getByAdquisicion(idAdquisicion);
			for (Adquisiciondetalle adquisiciondetalle : adList) {
				anularDetalle(adquisiciondetalle.getIdadquisiciondetalle(), false, idUsuario);
			}
			
			if(a!=null) {
				if(eliminar) {
					eliminar(a);
				}else {
					a.setEstado(ComprobanteEstadoEnum.ANULADO.toString());
					a.setIdusuario(idUsuario);
					a.setUpdated(new Date());
					actualizar(a);
				}
			}
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idadquisiciondetalle
	 * @param eliminar
	 * @param idusuario
	 * @throws DaoException
	 */
	public void anularDetalle(String idadquisiciondetalle,boolean eliminar,String idusuario)throws DaoException{
		try {
			// crea la salida por cada uno
			Adquisiciondetalle ad = adquisiciondetalleServicio.consultarByPk(idadquisiciondetalle);
			if(ad!=null && ad.getProducto()!=null && ad.getProducto().getTipoProducto().getNombre().equals("PRODUCTO")) {
				
				KardexProducto k = new KardexProducto();
				BigDecimal cant = BigDecimal.ZERO;
				// datos de producto
				Producto p = productoServicio.consultarByPk(ad.getProducto().getIdproducto());
				if (p.getStock().compareTo(BigDecimal.valueOf(ad.getCantidad()))>=0) {
					if(p.getStock().add(BigDecimal.valueOf(ad.getCantidad()).negate()).doubleValue()<0.0) {
						cant = p.getStock();
					}else {
						cant = BigDecimal.valueOf(ad.getCantidad());
					}
				}
					// kardex
					k.setCantidad(cant);
					k.setEstablecimiento(ad.getAdquisicion().getEstablecimiento());
					k.setFechacompra(ad.getAdquisicion().getFecha());
					k.setFecharegistro(ad.getAdquisicion().getFecha());
	//				k.setIdreferencia(ad.getIdadquisiciondetalle());
	//				k.setModuloreferencia("ADQ");
					k.setIdusuario(idusuario);
					k.setProducto(ad.getProducto());
					k.setTiporegistro("S");
					k.setUpdated(new Date());
					k.setCostounitario(ad.getPreciounitario());
					k.setFechafabricacion(ad.getProducto().getFechafabricacion());
					k.setFechavencimiento(ad.getProducto().getFechavencimiento());
					StringBuilder obs = new StringBuilder();
					obs.append("ANULACION COMPRA ");
					obs.append(" / PROVEEDOR : " + ad.getAdquisicion().getProveedor().getNombrecomercial());
					obs.append(" / FACTURA : " + ad.getAdquisicion().getNumfactura());
					obs.append(" / FECHA : " + FechaUtil.formatoFecha(ad.getAdquisicion().getFecha()));
					obs.append(" / PAGO : " + ad.getAdquisicion().getTipopago().getNombre());
					k.setObservacion(obs.toString());
					kardexProductoServicio.registrarKardexFacade(k);
				
			}
			
			if(ad!=null && eliminar) {
				adquisiciondetalleServicio.eliminar(ad);
			}
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	
	@Override
	public List<Adquisicion> consultarActivos() {
		return null;
	}

	@Override
	public List<Adquisicion> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Adquisicion, String> getDao() {
		return adquisicionDao;
	}

	/**
	 * @return the adquisicionDao
	 */
	public AdquisicionDao getAdquisicionDao() {
		return adquisicionDao;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idProveedor
	 * @param numFactura
	 * @param idEstablecimiento
	 * @return
	 * @throws DaoException
	 */
	public Adquisicion getByFactura(String idProveedor,String numFactura,String idEstablecimiento)throws DaoException{
		try {
			
			QueryBuilder q = new QueryBuilder(adquisicionDao.getEntityManager());
			
			return (Adquisicion) q.select("a")
					.from(Adquisicion.class,"a")
					.equals("a.establecimiento.idestablecimiento",idEstablecimiento)
					.equals("a.proveedor.idproveedor",idProveedor)
					.equals("a.numfactura",numFactura)
					.notEquals("a.estado", "ANU").getSingleResult();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idadquisiicon
	 * @param idEstablecimeinto
	 * @param accion
	 * @return
	 * @throws DaoException
	 */
	public String analizarEstado(String idadquisiicon,String idEstablecimeinto,String accion)throws DaoException{
		
		try {
			
			QueryBuilder q = new QueryBuilder(adquisicionDao.getEntityManager());
			
			Adquisicion ad = (Adquisicion) q.select("a")
								.from(Adquisicion.class,"a")
								.equals("a.establecimiento.idestablecimiento",idEstablecimeinto)
								.equals("a.idadquisicion",idadquisiicon).getSingleResult();
			
			if (ad==null) {
				return "NO EXISTE LA COMPRA : " + idadquisiicon;
			}
			if (ad!=null && accion.equals("ANULAR")) {
				
				List<ComprobanteEstadoEnum> lista = new ArrayList<>();
				lista.add(ComprobanteEstadoEnum.ANULADO);
				lista.add(ComprobanteEstadoEnum.AUTORIZACION);
				lista.add(ComprobanteEstadoEnum.RETENCION);
				lista.add(ComprobanteEstadoEnum.SRI);
				
				if(lista.contains(ComprobanteEstadoEnum.getByEstado(ad.getEstado()))) {
					return "NO SE PUEDE ANULAR, POR QUE SE ENCUENTRA EN ESTADO: " + ad.getEstado();
				}
				
			}
			
			if (ad!=null && accion.equals("GUARDAR")) {
				
				List<ComprobanteEstadoEnum> lista = new ArrayList<>();
				lista.add(ComprobanteEstadoEnum.ANULADO);
				lista.add(ComprobanteEstadoEnum.AUTORIZACION);
				lista.add(ComprobanteEstadoEnum.RETENCION);
				lista.add(ComprobanteEstadoEnum.SRI);
				
				if(lista.contains(ComprobanteEstadoEnum.getByEstado(ad.getEstado()))) {
					return "NO SE PUEDE REALIZAR NINGUNA MODIFICACION, POR QUE SE ENCUENTRA EN ESTADO: " + ad.getEstado();
				}
				
			}
			
			if (ad!=null && accion.equals("ELIMINAR_PAGO")) {
				
				List<ComprobanteEstadoEnum> lista = new ArrayList<>();
				lista.add(ComprobanteEstadoEnum.ANULADO);
				lista.add(ComprobanteEstadoEnum.AUTORIZACION);
				lista.add(ComprobanteEstadoEnum.RETENCION);
				lista.add(ComprobanteEstadoEnum.SRI);
				
				if(lista.contains(ComprobanteEstadoEnum.getByEstado(ad.getEstado()))) {
					return "NO SE PUEDE ELIMINAR, POR QUE SE ENCUENTRA EN ESTADO: " + ad.getEstado();
				}
				
			}
			
			if (ad!=null && accion.equals("RETENCION")) {
				
				List<ComprobanteEstadoEnum> lista = new ArrayList<>();
				lista.add(ComprobanteEstadoEnum.ANULADO);
				lista.add(ComprobanteEstadoEnum.AUTORIZACION);
				lista.add(ComprobanteEstadoEnum.SRI);
				
				if(lista.contains(ComprobanteEstadoEnum.getByEstado(ad.getEstado()))) {
					return "NO SE PUEDE MODIFICAR LA RETENCION YA QUE LA COMPRA ASOCIADA SE ENCUENTRA EN ESTADO: " + ad.getEstado();
				}
				
			}
			
			return null;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
		
		
	}

}
