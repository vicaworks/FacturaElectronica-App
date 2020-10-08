package com.vcw.falecpv.core.servicio;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.Query;

import com.servitec.common.dao.DaoGenerico;
import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.FechaUtil;
import com.servitec.common.util.exceptions.ParametroRequeridoException;
import com.vcw.falecpv.core.constante.ComprobanteEstadoEnum;
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;
import com.vcw.falecpv.core.constante.contadores.TCAleatorio;
import com.vcw.falecpv.core.constante.contadores.TCComprobanteEnum;
import com.vcw.falecpv.core.constante.contadores.TCGuiaRemision;
import com.vcw.falecpv.core.constante.contadores.TablaContadorBaseEnum;
import com.vcw.falecpv.core.dao.impl.CabeceraDao;
import com.vcw.falecpv.core.exception.EstadoComprobanteException;
import com.vcw.falecpv.core.exception.ExisteNumDocumentoException;
import com.vcw.falecpv.core.helper.ComprobanteHelper;
import com.vcw.falecpv.core.modelo.persistencia.Adquisicion;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Destinatario;
import com.vcw.falecpv.core.modelo.persistencia.Detalle;
import com.vcw.falecpv.core.modelo.persistencia.Detalledestinatario;
import com.vcw.falecpv.core.modelo.persistencia.Detalleimpuesto;
import com.vcw.falecpv.core.modelo.persistencia.Impuestoretencion;
import com.vcw.falecpv.core.modelo.persistencia.Infoadicional;
import com.vcw.falecpv.core.modelo.persistencia.KardexProducto;
import com.vcw.falecpv.core.modelo.persistencia.Motivo;
import com.vcw.falecpv.core.modelo.persistencia.Pago;
import com.vcw.falecpv.core.modelo.persistencia.Producto;
import com.vcw.falecpv.core.modelo.persistencia.Totalimpuesto;
import com.xpert.persistence.query.QueryBuilder;

@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
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
	
	@Inject
	private ImpuestoretencionServicio impuestoretencionServicio;
	
	@Inject
	private AdquisicionServicio adquisicionServicio;
	
	@Inject
	private DestinatarioServicio destinatarioServicio;
	
	@Inject
	private DetalledestinatarioServicio detalledestinatarioServicio;
	
	@Inject
	private MotivoServicio motivoServicio;
	
	@Inject
	private EstablecimientoServicio establecimientoServicio;
	
	private List<ComprobanteEstadoEnum> estadosAnulacion = Arrays.asList(new ComprobanteEstadoEnum[] {ComprobanteEstadoEnum.BORRADOR,ComprobanteEstadoEnum.ERROR,ComprobanteEstadoEnum.ERROR_SRI,ComprobanteEstadoEnum.AUTORIZADO,ComprobanteEstadoEnum.PENDIENTE});
	
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
	@Lock(LockType.WRITE)
	public Cabecera guardarComprobanteFacade(Cabecera cabecera)throws DaoException, ParametroRequeridoException,ExisteNumDocumentoException{
		
		TablaContadorBaseEnum aleatorio = null;
		
		switch (GenTipoDocumentoEnum.getEnumByIdentificador(cabecera.getTipocomprobante().getIdentificador())) {
		case COTIZACION:
			aleatorio = TCAleatorio.ALEATORIOCOTIZACION;
			break;
		case FACTURA:
			aleatorio = TCAleatorio.ALEATORIOFACTURA;
			break;
		case GUIA_REMISION:
			aleatorio = TCAleatorio.ALEATORIOGUIAREMISION;
			break;
		case LIQUIDACION_COMPRA:
			aleatorio = TCAleatorio.ALEATORIOLIQCOMPRA;
			break;	
		case NOTA_CREDITO:
			aleatorio = TCAleatorio.ALEATORIONOTACREDITO;
			break;
		case NOTA_DEBITO:
			aleatorio = TCAleatorio.ALEATORIONOTADEBITO;
			break;
		case RECIBO:
			aleatorio = TCAleatorio.ALEATORIORECIBO;
			break;
		case RETENCION:
			aleatorio = TCAleatorio.ALEATORIORETENCION;
			break;
		default:
			break;
		}
		
		// 0. Asignar secuencia
		if(cabecera.getSecuencial()==null && !cabecera.isEditarSecuencial()) {
			
			cabecera.setSecuencial(establecimientoServicio.generarNumeroDocumento(cabecera.getGenTipoDocumentoEnum(), cabecera.getEstablecimiento().getIdestablecimiento()));
			cabecera.setClaveacceso(ComprobanteHelper.generarAutorizacionFacade(cabecera, contadorPkServicio.generarContadorTabla(aleatorio, cabecera.getEstablecimiento().getIdestablecimiento(),new Object[] {false})));
			cabecera.setNumeroautorizacion(cabecera.getClaveacceso());
			cabecera.setNumdocumento(cabecera.getSecuencialEstablecimiento() + cabecera.getSecuencialCaja() + cabecera.getSecuencial());
			
		}else if(cabecera.isEditarSecuencial()) {
			
			cabecera.setSecuencial(cabecera.getSecuencialNumero());
			cabecera.setClaveacceso(ComprobanteHelper.generarAutorizacionFacade(cabecera, contadorPkServicio.generarContadorTabla(aleatorio, cabecera.getEstablecimiento().getIdestablecimiento(),new Object[] {false})));
			cabecera.setNumeroautorizacion(cabecera.getClaveacceso());
			cabecera.setNumdocumento(cabecera.getSecuencialEstablecimiento() + cabecera.getSecuencialCaja() + cabecera.getSecuencial());
		}
		
		switch (cabecera.getGenTipoDocumentoEnum()) {
		case FACTURA:
			cabecera.setNumfactura(cabecera.getNumdocumento());
			break;
		case LIQUIDACION_COMPRA:
			cabecera.setNumfactura(cabecera.getNumdocumento());
		case GUIA_REMISION:
			cabecera.setNumfactura(cabecera.getNumdocumento());	
			break;	
		case NOTA_DEBITO:
			cabecera.setNumfactura(cabecera.getNumdocumento());	
			break;	
		default:
			break;
		}
		
		// 0.1 Valida que no exista duplicado el comprobante
		if (existeNumComprobante(cabecera.getEstablecimiento().getIdestablecimiento(),
				cabecera.getGenTipoDocumentoEnum().getIdentificador(), cabecera.getSecuencial(),
				cabecera.getIdcabecera())) {
			
			throw new ExisteNumDocumentoException("EL SECUENCIAL : " + cabecera.getSecuencial() + " YA EXISTE NO SE PUEDE DUPLICAR.");
			
		}
		
		
		// 1. crear la cabecera
		if(cabecera.getIdcabecera()==null) {
			cabecera.setIdcabecera(contadorPkServicio.generarContadorTabla(TCComprobanteEnum.CABECERA, cabecera.getEstablecimiento().getIdestablecimiento()));
			crear(cabecera);
		}else {
			actualizar(cabecera);
		} 		
		
		// 2. total impuesto
		totalimpuestoServicio.getTotalimpuestoDao().eliminarByCabecera(cabecera.getIdcabecera());
		if(cabecera.getTotalimpuestoList()!=null) {
			for (Totalimpuesto	ti : cabecera.getTotalimpuestoList()) {
				ti.setCabecera(cabecera);
				if(ti.getIdtotalmpuesto()==null || ti.getIdtotalmpuesto().contains("M")) {
					ti.setIdtotalmpuesto(contadorPkServicio.generarContadorTabla(TCComprobanteEnum.TOTALIMPUESTO, cabecera.getEstablecimiento().getIdestablecimiento()));
				}
				totalimpuestoServicio.crear(ti);
			}
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
		if(cabecera.getDetalleList()!=null) {
			for (Detalle d : cabecera.getDetalleList()) {
				if(d.getIddetalle()==null || d.getIddetalle().contains("M")) {
					d.setIddetalle(contadorPkServicio.generarContadorTabla(TCComprobanteEnum.DETALLE, cabecera.getEstablecimiento().getIdestablecimiento()));
				}
				d.setCabecera(cabecera);
				detalleServicio.crear(d);
				// kardex producto
				if(d.getProducto()!=null && d.getProducto().getTipoProducto().getNombre().equals("PRODUCTO")) {
					
					switch (GenTipoDocumentoEnum.getEnumByIdentificador(cabecera.getTipocomprobante().getIdentificador())) {
					case FACTURA: case RECIBO:
						salidaKardex(d);
						break;
					default:
						break;
					}
				}
			}
		}
		
		// 4. detalle impuesto
		detalleimpuestoServicio.getDetalleimpuestoDao().eliminarByCabecera(cabecera.getIdcabecera());
		if(cabecera.getDetalleList()!=null) {
			for (Detalle d : cabecera.getDetalleList()) {
				for (Detalleimpuesto di : d.getDetalleimpuestoList()) {
					di.setDetalle(d);
					if(di.getIddetalleimpuesto()==null || di.getIddetalleimpuesto().contains("M")) {
						di.setIddetalleimpuesto(contadorPkServicio.generarContadorTabla(TCComprobanteEnum.DETALLEIMPUESTO, cabecera.getEstablecimiento().getIdestablecimiento()));
					}
					detalleimpuestoServicio.crear(di);
				}
			}
		}
		
		// 5. motivo
		motivoServicio.getMotivoDao().eliminarByCabecera(cabecera.getIdcabecera());
		if(cabecera.getMotivoList()!=null) {
			for (Motivo	m : cabecera.getMotivoList()) {
				m.setCabecera(cabecera);
				if(m.getIdmotivo()==null || m.getIdmotivo().contains("M")) {
					m.setIdmotivo(contadorPkServicio.generarContadorTabla(TCComprobanteEnum.MOTIVO, cabecera.getEstablecimiento().getIdestablecimiento()));
				}
				motivoServicio.crear(m);
			}
		}
		
		// 6. pago
		pagoServicio.getPagoDao().eliminarByCabecera(cabecera.getIdcabecera());
		if(cabecera.getPagoList()!=null) {
			for (Pago	p : cabecera.getPagoList()) {
				if(p.getIdpago()==null || p.getIdpago().contains("M")) {
					p.setIdpago(contadorPkServicio.generarContadorTabla(TCComprobanteEnum.PAGO, cabecera.getEstablecimiento().getIdestablecimiento()));
				}
				p.setCabecera(cabecera);
				pagoServicio.crear(p);
			}
		}
		
		// 7. infoadicional
		infoadicionalServicio.getInfoadicionalDao().eliminarByCabecera(cabecera.getIdcabecera());
		if(cabecera.getInfoadicionalList()!=null) {
			for (Infoadicional	ia : cabecera.getInfoadicionalList()) {
				if(ia.getIdinfoadicional()==null || ia.getIdinfoadicional().contains("M")) {
					ia.setIdinfoadicional(contadorPkServicio.generarContadorTabla(TCComprobanteEnum.INFOADICIONAL, cabecera.getEstablecimiento().getIdestablecimiento()));
				}
				ia.setCabecera(cabecera);
				infoadicionalServicio.crear(ia);
			}
		}
		
		// 8. impuestoretencion
		impuestoretencionServicio.getImpuestoretencionDao().eliminarByCabecera(cabecera.getIdcabecera());
		if(cabecera.getImpuestoretencionList()!=null) {
			for (Impuestoretencion ir : cabecera.getImpuestoretencionList()) {
				if(ir.getIdimpuestoretencion()==null || ir.getIdimpuestoretencion().contains("M")) {
					ir.setIdimpuestoretencion(contadorPkServicio.generarContadorTabla(TCComprobanteEnum.IMPUESTORETENCION, cabecera.getEstablecimiento().getIdestablecimiento()));
				}
				ir.setCabecera(cabecera);
				impuestoretencionServicio.crear(ir);
			}
		}
		
		// 9. destinatario guia remision
		destinatarioServicio.getDestinatarioDao().eliminarByCabecera(cabecera.getIdcabecera());
		if(cabecera.getDestinatarioList()!=null) {
			for (Destinatario des : cabecera.getDestinatarioList()) {
				if(des.getIddestinatario()==null || des.getIddestinatario().contains("M")) {
					des.setIddestinatario(contadorPkServicio.generarContadorTabla(TCGuiaRemision.DESTINATARIO, cabecera.getEstablecimiento().getIdestablecimiento()));
				}
				des.setCabecera(cabecera);
				destinatarioServicio.crear(des);
				// almacenar detalle
				for (Detalledestinatario dd : des.getDetalledestinatarioList()) {
					if(dd.getIddetalledestinatario()==null || dd.getIddetalledestinatario().contains("M")) {
						dd.setIddetalledestinatario(contadorPkServicio.generarContadorTabla(TCGuiaRemision.DETALLE_DESTINATARIO, cabecera.getEstablecimiento().getIdestablecimiento()));
					}
					dd.setDestinatario(des);
					detalledestinatarioServicio.crear(dd);
				}
				// actualizar la guia remision a la factura relacionada
				if(des.getIdVenta()!=null) {
					cabeceraDao.asignarRefGuiaRemicion(des.getIdVenta(), cabecera.getIdcabecera());
				}
			}
		}
		
		// 10. Si es retencion y tiene una compra actualiza los datos de la compra
		if(cabecera.getAdquisicion()!=null && cabecera.getTipocomprobante().getIdentificador().equals(GenTipoDocumentoEnum.RETENCION.getIdentificador())) {
			Adquisicion adquisicion = adquisicionServicio.consultarByPk(cabecera.getAdquisicion().getIdadquisicion());
			adquisicion.setEstado("RETENCION");
			adquisicionServicio.actualizar(adquisicion);
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
	public String analizarEstadoComprobante(String idcabecera,String accion)throws DaoException{
		try {
			
			QueryBuilder q = new QueryBuilder(cabeceraDao.getEntityManager());
			
			Cabecera c = (Cabecera) q.select("c")
							.from(Cabecera.class,"c")
							.equals("c.idcabecera",idcabecera).getSingleResult();
			
			if(c==null) {
				return null;
			}
			
			
			if(c!=null && accion.equals("ANULAR")) {
				List<ComprobanteEstadoEnum> lista = new ArrayList<>();
				lista.add(ComprobanteEstadoEnum.ANULADO);
				lista.add(ComprobanteEstadoEnum.PENDIENTE);
				
				if(lista.contains(ComprobanteEstadoEnum.getByEstado(c.getEstado()))) {
					return "NO SE PUEDE REALIZAR NINGUNA MODIFICACION, POR QUE SE ENCUENTRA EN ESTADO: " + c.getEstado();
				}
				
			}
			
			if(c!=null && accion.equals("ELIMINAR_DETALLE")) {
				List<ComprobanteEstadoEnum> lista = new ArrayList<>();
				lista.add(ComprobanteEstadoEnum.ANULADO);
				lista.add(ComprobanteEstadoEnum.AUTORIZADO);
				lista.add(ComprobanteEstadoEnum.RECIBIDO_SRI);
				lista.add(ComprobanteEstadoEnum.PENDIENTE);
				
				if(lista.contains(ComprobanteEstadoEnum.getByEstado(c.getEstado()))) {
					return "NO SE PUEDE ELIMINAR, POR QUE SE ENCUENTRA EN ESTADO: " + c.getEstado();
				}
				
			}
			
			if(c!=null && accion.equals("GUARDAR")) {
				List<ComprobanteEstadoEnum> lista = new ArrayList<>();
				lista.add(ComprobanteEstadoEnum.ANULADO);
				lista.add(ComprobanteEstadoEnum.AUTORIZADO);
				lista.add(ComprobanteEstadoEnum.RECIBIDO_SRI);
				lista.add(ComprobanteEstadoEnum.PENDIENTE);
				
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
	@Lock(LockType.WRITE)
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
	@Lock(LockType.WRITE)
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
			k.setProducto(detalleFac.getProducto());
			k.setTiporegistro("E");
			k.setUpdated(new Date());
			k.setCostounitario(detalleFac.getPreciounitario());
			k.setFechafabricacion(detalleFac.getProducto().getFechafabricacion());
			k.setFechavencimiento(detalleFac.getProducto().getFechavencimiento());
			StringBuilder obs = new StringBuilder();
			obs.append("CLIENTE : " + detalleFac.getCabecera().getCliente().getRazonsocial());
			switch (GenTipoDocumentoEnum.getEnumByIdentificador(detalleFac.getCabecera().getTipocomprobante().getIdentificador())) {
			case RECIBO:
				obs.append("ANULACION RECIBO : ");
				obs.append(" / NUM : " + detalleFac.getCabecera().getSecuencial());
				obs.append(" / FECHA : " + FechaUtil.formatoFecha(detalleFac.getCabecera().getFechaemision()));
				k.setIdusuario(detalleFac.getCabecera().getIdusuario());
				break;
			case FACTURA:
				obs.append("ANULACION " + detalleFac.getCabecera().getTipocomprobante().getComprobante() + " : ");
				obs.append(" / NUM : " + detalleFac.getCabecera().getSecuencial());
				obs.append(" / FECHA : " + FechaUtil.formatoFecha(detalleFac.getCabecera().getFechaemision()));
				k.setIdusuario(detalleFac.getIdUsuarioEliminacion());
				break;
			case NOTA_CREDITO:
				obs.append("ANULACION FACTURA");
				obs.append(" / NUM : " + detalleFac.getCabecera().getNumdocasociado());
				obs.append(" / FECHA : " + FechaUtil.formatoFecha(detalleFac.getCabecera().getFechaemisiondocasociado()));
				k.setIdusuario(detalleFac.getCabecera().getIdusuario());
				break;	

			default:
				break;
			}
			
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
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idCabecera
	 * @throws DaoException
	 */
	@Lock(LockType.WRITE)
	public int anularById(String idCabecera)throws DaoException{
		try {
			
			// 1. consultar cabecera 
			Cabecera c = consultarByPk(idCabecera);
			
			if(c==null ||  !estadosAnulacion.contains(ComprobanteEstadoEnum.getByEstado(c.getEstado()))) {
				throw new EstadoComprobanteException("NO SE PUEDE ANULAR SE ENCUENTRA EN ESTADO:" + c.getEstado());
			}
			
			// si es factura y estado borrador se elimina de la base de datos
			
			if (ComprobanteEstadoEnum.getByEstado(c.getEstado()).equals(ComprobanteEstadoEnum.BORRADOR)) {
				
				eliminar(c);
				
				return 1;
			}
			
			String sql = "UPDATE cabecera SET estado='" + ComprobanteEstadoEnum.ANULADO.toString() + "' WHERE idcabecera='" + idCabecera + "'";
			return execute(sql);
			
			
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
		
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idCabecera
	 * @throws DaoException
	 */
	@Lock(LockType.WRITE)
	public void anularComprobanteReversoKardex(String idCabecera,String idUsuarioEliminacion) throws DaoException,EstadoComprobanteException {
		try {
			
			// 1. consultar cabecera 
			Cabecera c = consultarByPk(idCabecera);
			
			if(c==null ||  !estadosAnulacion.contains(ComprobanteEstadoEnum.getByEstado(c.getEstado()))) {
				throw new EstadoComprobanteException("NO SE PUEDE ANULAR SE ENCUENTRA EN ESTADO:" + c.getEstado());
			}
			
			// si es factura y estado borrador se elimina de la base de datos
			
			if (ComprobanteEstadoEnum.getByEstado(c.getEstado()).equals(ComprobanteEstadoEnum.BORRADOR)
					&& GenTipoDocumentoEnum.getEnumByIdentificador(c.getTipocomprobante().getIdentificador())
							.equals(GenTipoDocumentoEnum.FACTURA)) {
				
				eliminar(c);
				
				return;
			}
			
			// si es anulacion se cambia de estado y se hace un reverso
			
			c.setEstado(ComprobanteEstadoEnum.ANULADO.toString());
			c.setGenTipoDocumentoEnum(GenTipoDocumentoEnum.getEnumByIdentificador(c.getTipocomprobante().getIdentificador()));
			List<Detalle> detalleList = detalleServicio.getDetalleDao().getByIdCabecera(idCabecera);
			for (Detalle d : detalleList) {
				d.setIdUsuarioEliminacion(idUsuarioEliminacion);
				if(d.getProducto()!=null && d.getProducto().getTipoProducto().getNombre().equals("PRODUCTO")) {
					
					switch (GenTipoDocumentoEnum.getEnumByIdentificador(c.getTipocomprobante().getIdentificador())) {
					case FACTURA: case RECIBO:
						entredaKardex(d);
						break;
					default:
						break;
					}
				}
				
			}
			
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
	@Lock(LockType.WRITE)
	public int anularGuiaRemisionFacade(String idCabecera)throws DaoException{
		try {
			
			// 1. consultar cabecera 
			Cabecera c = consultarByPk(idCabecera);
			
			if(c==null ||  !estadosAnulacion.contains(ComprobanteEstadoEnum.getByEstado(c.getEstado()))) {
				throw new EstadoComprobanteException("NO SE PUEDE ANULAR SE ENCUENTRA EN ESTADO:" + c.getEstado());
			}
			
			Query q = getCabeceraDao().getEntityManager().createNativeQuery("UPDATE cabecera set idguiaremision=null WHERE idguiaremision=:idcabecera");
			q.setParameter("idcabecera", idCabecera);
			q.executeUpdate();
			
			// si es factura y estado borrador se elimina de la base de datos
			
			if (ComprobanteEstadoEnum.getByEstado(c.getEstado()).equals(ComprobanteEstadoEnum.BORRADOR)) {
				
				eliminar(c);
				
				return 1;
			}
			
			String sql = "UPDATE cabecera SET estado='" + ComprobanteEstadoEnum.ANULADO.toString() + "' WHERE idcabecera='" + idCabecera + "'";
			return execute(sql);
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param iEstablecimeinto
	 * @param tipComprobanteIdentificador
	 * @param secuencial
	 * @param idCabecera
	 * @return
	 * @throws DaoException
	 */
	@Lock(LockType.READ)
	public boolean existeNumComprobante(String iEstablecimeinto,String tipComprobanteIdentificador, String secuencial,String idCabecera)throws DaoException{
		try {
			
			QueryBuilder q = new QueryBuilder(cabeceraDao.getEntityManager());
			
			List<Cabecera> lista = null;
			
			if(idCabecera!=null) {
				lista = q.select("c")
						.from(Cabecera.class,"c")
						.notEquals("c.idcabecera", idCabecera)
						.equals("c.establecimiento.idestablecimiento", iEstablecimeinto)
						.equals("c.tipocomprobante.identificador",tipComprobanteIdentificador)
						.equals("c.secuencial",secuencial).getResultList();
			}else {
				lista = q.select("c")
						.from(Cabecera.class,"c")
						.equals("c.establecimiento.idestablecimiento", iEstablecimeinto)
						.equals("c.tipocomprobante.identificador",tipComprobanteIdentificador)
						.equals("c.secuencial",secuencial).getResultList();
			}
			
			return !lista.isEmpty();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}