package com.vcw.falecpv.core.servicio;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.FechaUtil;
import com.servitec.common.util.exceptions.ParametroRequeridoException;
import com.vcw.falecpv.core.constante.ComprobanteEstadoEnum;
import com.vcw.falecpv.core.constante.contadores.TCComprobanteEnum;
import com.vcw.falecpv.core.dao.impl.CabeceraDao;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Detalle;
import com.vcw.falecpv.core.modelo.persistencia.Detalleimpuesto;
import com.vcw.falecpv.core.modelo.persistencia.Infoadicional;
import com.vcw.falecpv.core.modelo.persistencia.KardexProducto;
import com.vcw.falecpv.core.modelo.persistencia.Pago;
import com.vcw.falecpv.core.modelo.persistencia.Producto;
import com.vcw.falecpv.core.modelo.persistencia.Totalimpuesto;
import com.xpert.persistence.query.QueryBuilder;

@Stateless
public class CabeceraServicio extends AppGenericService<Cabecera, String> {

	@Inject
	private CabeceraDao cabeceraDao;
	
	@Inject
	private DetalleServicio detalleServicio;
	
	@Inject
	private DetalleimpuestoServicio detalleimpuestoServicio;
	
	@Inject
	private PagoServicio pagoServicio;
	
	@Inject
	private InfoadicionalServicio infoadicionalServicio;
	
	@Inject
	private TotalimpuestoServicio totalimpuestoServicio;
	
	@Inject
	private ContadorPkServicio contadorPkServicio;
	
	@Inject
	private KardexProductoServicio kardexProductoServicio;
	
	@Inject
	private ProductoServicio productoServicio;
	
	
	
	public CabeceraServicio() {
	}

	@Override
	public List<Cabecera> consultarActivos() {
		return null;
	}

	@Override
	public List<Cabecera> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Cabecera, String> getDao() {
		return cabeceraDao;
	}

	/**
	 * @return the cabeceraDao
	 */
	public CabeceraDao getCabeceraDao() {
		return cabeceraDao;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param cabecera
	 * @return
	 * @throws DaoException
	 * @throws ParametroRequeridoException
	 */
	public Cabecera guardarComprobanteFacade(Cabecera cabecera)throws DaoException, ParametroRequeridoException{
		
		// 1. crear la cabecera
		if(cabecera.getIdcabecera()==null) {
			cabecera.setIdcabecera(contadorPkServicio.generarContadorTabla(TCComprobanteEnum.CABECERA, cabecera.getEstablecimiento().getIdestablecimiento()));
			crear(cabecera);
		}else {
			actualizar(cabecera);
		} 		
		
		// 2. total impuesto
		totalimpuestoServicio.getTotalimpuestoDao().eliminarByCabecera(cabecera.getIdcabecera());
		for (Totalimpuesto	ti : cabecera.getTotalimpuestoList()) {
			ti.setCabecera(cabecera);
			if(ti.getIdtotalmpuesto()==null || ti.getIdtotalmpuesto().contains("M")) {
				ti.setIdtotalmpuesto(contadorPkServicio.generarContadorTabla(TCComprobanteEnum.TOTALIMPUESTO, cabecera.getEstablecimiento().getIdestablecimiento()));
			}
			totalimpuestoServicio.crear(ti);
		}
		
		
		// 3. guardar detalle
		
		// anular kardex si existe
		if(cabecera.getDetalleEliminarList()!=null) {
			for (Detalle det : cabecera.getDetalleEliminarList()) {
				if(det.getIddetalle()!=null && !det.getIddetalle().contains("M") && det.getProducto()!=null) {
					if(det.getProducto().getTipoProducto().getNombre().equals("PRODUCTO")) {
						entredaKardex(det);
					}
				}
			}
			cabecera.setDetalleEliminarList(null);
		}
		// eliminar el detale anterior
		detalleServicio.getDetalleDao().eliminarByCabecera(cabecera.getIdcabecera());
		for (Detalle d : cabecera.getDetalleList()) {
			if(d.getIddetalle()==null || d.getIddetalle().contains("M")) {
				d.setIddetalle(contadorPkServicio.generarContadorTabla(TCComprobanteEnum.DETALLE, cabecera.getEstablecimiento().getIdestablecimiento()));
			}
			d.setCabecera(cabecera);
			detalleServicio.crear(d);
			// kardex producto
			if(d.getProducto()!=null && d.getProducto().getTipoProducto().getNombre().equals("PRODUCTO")) {
				salidaKardex(d);
			}
		}
		
		// 4. detalle impuesto
		detalleimpuestoServicio.getDetalleimpuestoDao().eliminarByCabecera(cabecera.getIdcabecera());
		for (Detalle d : cabecera.getDetalleList()) {
			for (Detalleimpuesto di : d.getDetalleimpuestoList()) {
				di.setDetalle(d);
				if(di.getIddetalleimpuesto()==null || di.getIddetalleimpuesto().contains("M")) {
					di.setIddetalleimpuesto(contadorPkServicio.generarContadorTabla(TCComprobanteEnum.DETALLEIMPUESTO, cabecera.getEstablecimiento().getIdestablecimiento()));
				}
				detalleimpuestoServicio.crear(di);
			}
		}
		
		// 5. pago
		pagoServicio.getPagoDao().eliminarByCabecera(cabecera.getIdcabecera());
		for (Pago	p : cabecera.getPagoList()) {
			if(p.getIdpago()==null || p.getIdpago().contains("M")) {
				p.setIdpago(contadorPkServicio.generarContadorTabla(TCComprobanteEnum.PAGO, cabecera.getEstablecimiento().getIdestablecimiento()));
			}
			p.setCabecera(cabecera);
			pagoServicio.crear(p);
		}
		
		// 6. infoadicional
		infoadicionalServicio.getInfoadicionalDao().eliminarByCabecera(cabecera.getIdcabecera());
		for (Infoadicional	ia : cabecera.getInfoadicionalList()) {
			if(ia.getIdinfoadicional()==null || ia.getIdinfoadicional().contains("M")) {
				ia.setIdinfoadicional(contadorPkServicio.generarContadorTabla(TCComprobanteEnum.INFOADICIONAL, cabecera.getEstablecimiento().getIdestablecimiento()));
			}
			ia.setCabecera(cabecera);
			infoadicionalServicio.crear(ia);
		}
		
		return cabecera;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idcabecera
	 * @param idEstablecimiento
	 * @param accion
	 * @return
	 * @throws DaoException
	 */
	public String analizarEstadoFactura(String idcabecera,String accion)throws DaoException{
		try {
			
			QueryBuilder q = new QueryBuilder(cabeceraDao.getEntityManager());
			
			Cabecera c = (Cabecera) q.select("c")
							.from(Cabecera.class,"c")
							.equals("c.idcabecera",idcabecera).getSingleResult();
			
			if(c==null) {
				return "NO EXISTE LA FACTURA";
			}
			
			if(c!=null && accion.equals("GUARDAR")) {
				
				List<ComprobanteEstadoEnum> lista = new ArrayList<>();
				lista.add(ComprobanteEstadoEnum.ANULADO);
				lista.add(ComprobanteEstadoEnum.AUTORIZACION);
				lista.add(ComprobanteEstadoEnum.SRI);
				
				if(lista.contains(ComprobanteEstadoEnum.getByEstado(c.getEstado()))) {
					return "NO SE PUEDE REALIZAR NINGUNA MODIFICACION, POR QUE SE ENCUENTRA EN ESTADO: " + c.getEstado();
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
	 * @param idcabecera
	 * @param accion
	 * @return
	 * @throws DaoException
	 */
	public String analizarEstadoRecibo(String idcabecera,String accion)throws DaoException{
		try {
			
			QueryBuilder q = new QueryBuilder(cabeceraDao.getEntityManager());
			
			Cabecera c = (Cabecera) q.select("c")
							.from(Cabecera.class,"c")
							.equals("c.idcabecera",idcabecera).getSingleResult();
			
			if(c==null) {
				return "NO EXISTE EL RECIBO";
			}
			
			if(c!=null && accion.equals("GUARDAR")) {
				
				List<ComprobanteEstadoEnum> lista = new ArrayList<>();
				lista.add(ComprobanteEstadoEnum.ANULADO);
				
				if(lista.contains(ComprobanteEstadoEnum.getByEstado(c.getEstado()))) {
					return "NO SE PUEDE REALIZAR NINGUNA MODIFICACION, POR QUE SE ENCUENTRA EN ESTADO: " + c.getEstado();
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
	 * @param detalleFac
	 * @throws DaoException
	 */
	public void salidaKardex(Detalle detalleFac)throws DaoException{
		try {
			
			KardexProducto k = kardexProductoServicio.getByEstablecimientoModulo(detalleFac.getProducto().getIdproducto(),
					detalleFac.getCabecera().getEstablecimiento().getIdestablecimiento(), detalleFac.getIddetalle(), "FAC");
			
			BigDecimal cantidad = detalleFac.getCantidad();
			
			if(k!=null && detalleFac.getCantidad().doubleValue()>k.getCantidad().doubleValue()) {
				
				cantidad = detalleFac.getCantidad().add(k.getCantidad().negate()).setScale(2, RoundingMode.HALF_UP);
				k.setCantidad(detalleFac.getCantidad());
				k.setSaldo(k.getSaldo().add(cantidad.negate()).setScale(2, RoundingMode.HALF_UP));
				Producto p = productoServicio.consultarByPk(detalleFac.getProducto().getIdproducto());
				p.setStock(p.getStock().add(cantidad.negate()));
				kardexProductoServicio.actualizar(k);
				productoServicio.actualizar(p);
				
			}else if(k!=null && detalleFac.getCantidad().doubleValue()<k.getCantidad().doubleValue()) {
				
				cantidad = k.getCantidad().add(detalleFac.getCantidad().negate()).setScale(2, RoundingMode.HALF_UP);
				k.setCantidad(detalleFac.getCantidad());
				k.setSaldo(k.getSaldo().add(cantidad));
				Producto p = productoServicio.consultarByPk(detalleFac.getProducto().getIdproducto());
				p.setStock(p.getStock().add(cantidad));
				kardexProductoServicio.actualizar(k);
				productoServicio.actualizar(p);
				
			}else if(k==null) {
				
				// registra la salida de kardex
				k = new KardexProducto();
				k.setCantidad(detalleFac.getCantidad());
				k.setEstablecimiento(detalleFac.getCabecera().getEstablecimiento());
				k.setFechacompra(detalleFac.getCabecera().getFechaemision());
				k.setFecharegistro(detalleFac.getCabecera().getFechaemision());
				k.setIdreferencia(detalleFac.getIddetalle());
				k.setIdusuario(detalleFac.getCabecera().getIdusuario());
				k.setModuloreferencia("FAC");
				k.setProducto(detalleFac.getProducto());
				k.setTiporegistro("S");
				k.setUpdated(new Date());
				k.setCostounitario(detalleFac.getPreciounitario());
				k.setFechafabricacion(detalleFac.getProducto().getFechafabricacion());
				k.setFechavencimiento(detalleFac.getProducto().getFechavencimiento());
				StringBuilder obs = new StringBuilder();
				obs.append(detalleFac.getCabecera().getTipocomprobante().getComprobante() + " : ");
				obs.append(" / CLIENTE : " + detalleFac.getCabecera().getCliente().getRazonsocial());
				obs.append(" / NUM : " + detalleFac.getCabecera().getSecuencial());
				obs.append(" / FECHA : " + FechaUtil.formatoFecha(detalleFac.getCabecera().getFechaemision()));
				if (detalleFac.getCabecera() != null && detalleFac.getCabecera().getPagoList() != null
						&& detalleFac.getCabecera().getPagoList().size() > 0) {
					
					obs.append(" / PAGO : " + (detalleFac.getCabecera().getPagoList().size() > 1 ? "VARIOS"
							: detalleFac.getCabecera().getPagoList().get(0).getTipopago().getNombre()));
					
				}
				k.setObservacion(obs.toString());
				kardexProductoServicio.registrarKardexFacade(k);
				
			}
			
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param detalleFac
	 * @throws DaoException
	 */
	public void entredaKardex(Detalle detalleFac)throws DaoException{
		try {
			
			if(detalleServicio.consultarByPk(detalleFac.getIddetalle())==null) {
				return;
			}
			
			
			KardexProducto k = new KardexProducto();
			BigDecimal cant = BigDecimal.ZERO;
			// datos de producto
			Producto p = productoServicio.consultarByPk(detalleFac.getProducto().getIdproducto());
			if(p.getStock().add(detalleFac.getCantidad().negate()).doubleValue()<0.0) {
				cant = p.getStock();
			}else {
				cant = detalleFac.getCantidad();
			}
			k.setCantidad(cant);
			k.setEstablecimiento(detalleFac.getCabecera().getEstablecimiento());
			k.setFechacompra(detalleFac.getCabecera().getFechaemision());
			k.setFecharegistro(detalleFac.getCabecera().getFechaemision());
//			k.setIdreferencia(detalleFac.getIddetalle());
//			k.setModuloreferencia("FAC");
			k.setIdusuario(detalleFac.getIdUsuarioEliminacion());
			k.setProducto(detalleFac.getProducto());
			k.setTiporegistro("E");
			k.setUpdated(new Date());
			k.setCostounitario(detalleFac.getPreciounitario());
			k.setFechafabricacion(detalleFac.getProducto().getFechafabricacion());
			k.setFechavencimiento(detalleFac.getProducto().getFechavencimiento());
			StringBuilder obs = new StringBuilder();
			obs.append("ANULACION " + detalleFac.getCabecera().getTipocomprobante().getComprobante() + " : ");
			obs.append(" / CLIENTE : " + detalleFac.getCabecera().getCliente().getRazonsocial());
			obs.append(" / NUM : " + detalleFac.getCabecera().getSecuencial());
			obs.append(" / FECHA : " + FechaUtil.formatoFecha(detalleFac.getCabecera().getFechaemision()));
			if (detalleFac.getCabecera() != null && detalleFac.getCabecera().getPagoList() != null
					&& detalleFac.getCabecera().getPagoList().size() > 0) {
				
				obs.append(" / PAGO : " + (detalleFac.getCabecera().getPagoList().size() > 1 ? "VARIOS"
						: detalleFac.getCabecera().getPagoList().get(0).getTipopago().getNombre()));
				
			}
			k.setObservacion(obs.toString());
			kardexProductoServicio.registrarKardexFacade(k);
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}