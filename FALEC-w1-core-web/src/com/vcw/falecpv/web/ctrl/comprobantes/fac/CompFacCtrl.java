/**
 * 
 */
package com.vcw.falecpv.web.ctrl.comprobantes.fac;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.omnifaces.util.Ajax;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.TextoUtil;
import com.servitec.common.util.exceptions.ParametroRequeridoException;
import com.vcw.falecpv.core.constante.ComprobanteEstadoEnum;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;
import com.vcw.falecpv.core.constante.TipoPagoFormularioEnum;
import com.vcw.falecpv.core.constante.contadores.TCAleatorio;
import com.vcw.falecpv.core.helper.ComprobanteHelper;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Detalle;
import com.vcw.falecpv.core.modelo.persistencia.Ice;
import com.vcw.falecpv.core.modelo.persistencia.Iva;
import com.vcw.falecpv.core.modelo.persistencia.Pago;
import com.vcw.falecpv.core.modelo.persistencia.Producto;
import com.vcw.falecpv.core.modelo.persistencia.Tipopago;
import com.vcw.falecpv.core.modelo.persistencia.Totalimpuesto;
import com.vcw.falecpv.core.servicio.CabeceraServicio;
import com.vcw.falecpv.core.servicio.ClienteServicio;
import com.vcw.falecpv.core.servicio.ContadorPkServicio;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.falecpv.core.servicio.IceServicio;
import com.vcw.falecpv.core.servicio.IvaServicio;
import com.vcw.falecpv.core.servicio.ProductoServicio;
import com.vcw.falecpv.core.servicio.TipocomprobanteServicio;
import com.vcw.falecpv.core.servicio.TipopagoServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@SessionScoped
public class CompFacCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9045892564546644805L;
	
	@EJB
	private ClienteServicio clienteServicio;
	
	@EJB
	private TipopagoServicio tipopagoServicio;
	
	@EJB
	private TipocomprobanteServicio tipocomprobanteServicio;
	
	@EJB
	private ContadorPkServicio contadorPkServicio;
	
	@EJB
	private CabeceraServicio cabeceraServicio;
	
	@EJB
	private EstablecimientoServicio establecimientoServicio;
	
	@EJB
	private ProductoServicio productoServicio;
	
	@EJB
	private IvaServicio ivaServicio;
	
	@EJB
	private IceServicio iceServicio;
	
	private Cabecera cabecerSelected;
	private String criterioCliente;
	private List<Pago> pagoList;
	private Pago pagoSelected;
	private BigDecimal totalPago = BigDecimal.ZERO;
	private BigDecimal totalSaldo = BigDecimal.ZERO;
	private List<Detalle> detalleFacList;
	private Detalle detalleSelected;
	private List<Producto> productoList;
	private Producto productoSelected;
	private String criterioBusqueda;
	private List<Ice> iceList;
	private List<Iva> ivaList;
 	
	/**
	 * 
	 */
	public CompFacCtrl() {
		
	}
	
	@PostConstruct
	public void init() {
		try {
			
			productoSelected = null;
			nuevaFactura();
			detalleFacList = null;
			detalleSelected = null;
			criterioBusqueda = null;
			consultarProductos();
			consultarIce();
			consultarIva();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void encerar() {
		cabecerSelected = null;
		criterioCliente = null;
		pagoList = null;
		pagoSelected = null;
		totalPago = BigDecimal.ZERO;
	}
	
	public void buscarConsumidorFinal() {
		try {
			if(cabecerSelected==null) return;
			
			consultarCliente("9999999999999");
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void consultarCliente(String identificador) throws DaoException {
		cabecerSelected.setCliente(null);
		cabecerSelected.setCliente(clienteServicio.getClienteDao().getByIdentificador(identificador,
				AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa()));
	}
	
	public void consultarIce()throws DaoException{
		iceList = iceServicio.getIceDao().getByEstado(EstadoRegistroEnum.ACTIVO	, AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa());
	}
	
	public void consultarIva()throws DaoException {
		ivaList = ivaServicio.getIvaDao().getByEstado(EstadoRegistroEnum.ACTIVO, AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa());
	}
	
	public void buscarCliente() {
		try {
			
			if(cabecerSelected==null) return;
			
			if(criterioCliente==null || criterioCliente.trim().length()==0) {
				AppJsfUtil.addErrorMessage("formMain:inpCriterioCliente", "ERROR", "REQUERIDO");
				return;
			}
			
			consultarCliente(criterioCliente);
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	@Override
	public void nuevo() {
		try {
			
			productoSelected = null;
			nuevaFactura();
			detalleFacList = null;
			detalleSelected = null;
			criterioBusqueda = null;
			consultarProductos();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void consultarProductos()throws DaoException{
		productoList = null;
		productoList = productoServicio.getProductoDao().consultarAllImageEager(AppJsfUtil.getEstablecimiento().getIdestablecimiento(),criterioBusqueda);
	}
	
	public void nuevaFactura() throws DaoException {
		criterioBusqueda = null;
		criterioCliente = null;
		cabecerSelected = new Cabecera();
		cabecerSelected.setFechaemision(new Date());
		criterioCliente = null;
		pagoList = null;
		pagoSelected = null;
		totalPago = BigDecimal.ZERO;
		totalSaldo = BigDecimal.ZERO;
		consultarIce();
		consultarIva();
	}
	
	public void agregarProducto() {
		try {
			
			if(detalleFacList==null) {
				detalleFacList = new ArrayList<>();
			}
			
			detalleSelected = existeProductoLista();
			boolean existe = false;
			if(detalleSelected!=null) {
				detalleSelected.setCantidad(
						productoSelected.getCantidad() != null ? detalleSelected.getCantidad().add(BigDecimal.valueOf(productoSelected.getCantidad()))
								: BigDecimal.valueOf(1));
				existe = true;
			}else {
				detalleSelected = new Detalle();
				detalleSelected.setCantidad(
						productoSelected.getCantidad() != null ? BigDecimal.valueOf(productoSelected.getCantidad())
								: BigDecimal.valueOf(1));
				detalleSelected.setDescripcion(productoSelected.getNombregenerico());
				detalleSelected.setPreciounitario(productoSelected.getPreciouno());
				detalleSelected.setProducto(productoSelected);
				detalleSelected.setIva(productoSelected.getIva());
				detalleSelected.setIce(productoSelected.getIce());
			}
			
			calcularItem(detalleSelected,true);
			
			if(!existe) {
				detalleFacList.add(detalleSelected);
			}
			
			totalizar();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	
	public void calcularItemAction(boolean calcDescuento,Detalle det) {
		try {
			detalleSelected = det;
			calcularItem(detalleSelected,calcDescuento);
			totalizar();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	
	private void calcularItem(Detalle dFac,boolean calcDescuento) {
		if(calcDescuento) {
			dFac.setDescuento(productoSelected.getPorcentajedescuento().divide(BigDecimal.valueOf(100))
					.multiply(dFac.getPreciounitario()).multiply(dFac.getCantidad())
					.setScale(2, RoundingMode.HALF_UP));
		}
		dFac.setPreciototalsinimpuesto(dFac.getCantidad().multiply(dFac.getPreciounitario()).add(dFac.getDescuento().negate()).setScale(2, RoundingMode.HALF_UP));
		dFac.setValorice(dFac.getIce().getValor().divide(BigDecimal.valueOf(100)).multiply(dFac.getPreciototalsinimpuesto()).setScale(2, RoundingMode.HALF_UP));
		dFac.setValoriva(dFac.getIva().getValor().divide(BigDecimal.valueOf(100))
				.multiply(dFac.getPreciototalsinimpuesto().add(dFac.getValorice())).setScale(2, RoundingMode.HALF_UP));
		dFac.setPreciototal(dFac.getPreciototalsinimpuesto().add(dFac.getValoriva().add(dFac.getValorice())).setScale(2, RoundingMode.HALF_UP));
		
	}
	
	private void totalizar() throws DaoException {
		if(cabecerSelected==null) nuevaFactura();
		
		
		cabecerSelected.setTotaliva(BigDecimal.ZERO);
		cabecerSelected.setTotalice(BigDecimal.ZERO);
		cabecerSelected.setTotaldescuento(BigDecimal.ZERO);
		cabecerSelected.setTotalsinimpuestos(BigDecimal.ZERO);
		cabecerSelected.setTotalconimpuestos(BigDecimal.ZERO);
		
		int i= 1;
		for (Detalle d : detalleFacList) {
			
			cabecerSelected.setTotalsinimpuestos(cabecerSelected.getTotalsinimpuestos().add(d.getPreciototalsinimpuesto()));
			cabecerSelected.setTotaliva(cabecerSelected.getTotaliva().add(d.getValoriva()));
			cabecerSelected.setTotalice(cabecerSelected.getTotalice().add(d.getValorice()));
			cabecerSelected.setTotaldescuento(cabecerSelected.getTotaldescuento().add(d.getDescuento()));
			
			if(d.getIddetalle()==null || d.getIddetalle().contains("MM")) {
				d.setIddetalle("MM" + i);
			}
			
			i++;
		}
		
		cabecerSelected.setTotaliva(cabecerSelected.getTotaliva().setScale(2, RoundingMode.HALF_UP));
		cabecerSelected.setTotalice(cabecerSelected.getTotalice().setScale(2, RoundingMode.HALF_UP));
		cabecerSelected.setTotaldescuento(cabecerSelected.getTotaldescuento().setScale(2, RoundingMode.HALF_UP));
		cabecerSelected.setTotalsinimpuestos(cabecerSelected.getTotalsinimpuestos().setScale(2, RoundingMode.HALF_UP));
		cabecerSelected.setTotalconimpuestos(cabecerSelected.getTotalsinimpuestos().add(cabecerSelected.getTotaliva()).add(cabecerSelected.getTotalice()).setScale(2, RoundingMode.HALF_UP));
		
		if(totalPago!=null && totalPago.doubleValue()>0) {
			totalizarPago();
		}
		
	}
	
	private Detalle existeProductoLista() {
		for (Detalle d : detalleFacList) {
			if(d.getProducto()!=null && d.getProducto().getIdproducto().equals(productoSelected.getIdproducto())) {
				return d;
			}
		}
		
		return null;
	}
	
	public void agregarItem() {
		try {
			
			if(detalleFacList==null) {
				detalleFacList = new ArrayList<>();
			}
			
			consultarIce();
			consultarIva();
			
			detalleSelected = new Detalle();
			detalleSelected.setCantidad(BigDecimal.valueOf(1));
			detalleSelected.setDescuento(BigDecimal.ZERO);
			detalleSelected.setDescripcion("");
			detalleSelected.setPreciounitario(BigDecimal.ZERO);
			detalleSelected.setProducto(null);
			detalleSelected.setIva(ivaServicio.getIvaDao().getDefecto(AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa()));
			detalleSelected.setIce(iceList.stream().filter(x->x.getValor().doubleValue()==0d).findFirst().get());
			detalleFacList.add(detalleSelected);
			
			calcularItem(detalleSelected, false);
			totalizar();
			Ajax.oncomplete("PrimeFaces.focus('formMain:pvDetalleDT:" + (detalleFacList.size()-1) + ":intDetNombreProducto');");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void eliminarDetalle() {
		try {
			
			// eliminar datos de la base de datos
			
			// eliminar
			
			for (Detalle p : detalleFacList) {
				if(detalleSelected.getIddetalle().equals(p.getIddetalle())) {
					break;
				}
			}
			
			if(cabecerSelected.getDetalleEliminarList()==null) {
				cabecerSelected.setDetalleEliminarList(new ArrayList<>());
			}
			detalleSelected.setIdUsuarioEliminacion(AppJsfUtil.getUsuario().getIdusuario());
			cabecerSelected.getDetalleEliminarList().add(detalleSelected);
			detalleFacList.remove(detalleSelected);
			if(detalleFacList.isEmpty()) {
				detalleSelected=null;
			}else {
				detalleSelected = detalleFacList.get(detalleFacList.size()-1);
			}
			
			totalizar();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void agregarPago(TipoPagoFormularioEnum tipoPagoFormularioEnum) {
		try {
			
			if(cabecerSelected == null) {
				return;
			}
			
			if (cabecerSelected.getTotalsinimpuestos().doubleValue()<=0) {
				return;
			}
			
			if (pagoList==null) {
				pagoList = new ArrayList<>();
			}
			
			Tipopago tp = tipopagoServicio.getByCodINterno(tipoPagoFormularioEnum,
					AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa());
			
			boolean flag = false;
			for (Pago p : pagoList) {
				if(p.getTipopago().equals(tp) && !tipoPagoFormularioEnum.isRepetir()) {
					pagoSelected = p;
					flag = true;
					break;
				}
			}
			
			totalizarPago();
			if(!flag) {
				pagoSelected = new Pago();
				pagoSelected.setTipoPagoFormularioEnum(tipoPagoFormularioEnum);
				pagoSelected.setCabecera(cabecerSelected);
				pagoSelected.setTipopago(tp);
				pagoSelected.setTotal(cabecerSelected.getTotalconimpuestos().add(totalPago.negate()).setScale(2, RoundingMode.HALF_UP));
				pagoSelected.setPlazo(BigDecimal.ZERO);
				pagoSelected.setUnidadtiempo("DIAS");
				pagoList.add(pagoSelected);
				totalizarPago();
			}
			
			switch (tipoPagoFormularioEnum) {
			case EFECTIVO:
				Ajax.oncomplete("PrimeFaces.focus('formMain:pvPagoDetalleDT:" + (pagoList.size()-1) + ":ipsPagValorEntrega_input');");
				break;
			case CREDITO:
				Ajax.oncomplete("PrimeFaces.focus('formMain:pvPagoDetalleDT:" + (pagoList.size()-1) + ":ipsPagPlazo_input');");
				break;	
			default:
				Ajax.oncomplete("PrimeFaces.focus('formMain:pvPagoDetalleDT:" + (pagoList.size()-1) + ":ipsPagValor_input');");
				break;
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	private void totalizarPago() {
		
		totalPago = BigDecimal.ZERO;
		totalSaldo = BigDecimal.ZERO;
		if(pagoList==null) {
			return;
		}
		int cont = 1;
		for (Pago p : pagoList) {
			if(p.getIdpago()==null || p.getIdpago().contains("MM")) {
				p.setIdpago("MM" + cont);
			}
			totalPago = totalPago.add(p.getTotal());
			cont++;
		}
		
		totalPago = totalPago.setScale(2, RoundingMode.HALF_UP);
		
		totalSaldo = cabecerSelected.getTotalconimpuestos().add(totalPago.negate()).setScale(2, RoundingMode.HALF_UP);
		if(totalSaldo.doubleValue()<0) {
			totalSaldo = BigDecimal.ZERO;
		}
		
	}
	
	public void calcularCambioAction(Pago p) {
		try {
			pagoSelected = p;
			if(pagoSelected.getValorentrega().doubleValue()==0) {
				pagoSelected.setCambio(BigDecimal.ZERO);
			}else {
				pagoSelected.setCambio(pagoSelected.getValorentrega().add(pagoSelected.getTotal().negate()).setScale(2, RoundingMode.HALF_UP));
				if(pagoSelected.getCambio().doubleValue()<0) {
					pagoSelected.setCambio(BigDecimal.ZERO);
				}
			}
			totalizarPago();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void totalizarPagoaction(Pago p) {
		try {
			pagoSelected = p;
			if(pagoSelected.getTipoPagoFormularioEnum().equals(TipoPagoFormularioEnum.EFECTIVO)) {
				calcularCambioAction(p);
			}
			totalizarPago();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	
	public void eliminarDetallePago() {
		
		try {
			
			for (Pago p : pagoList) {
				if(pagoSelected.getIdpago().equals(p.getIdpago())) {
					break;
				}
			}
			
			pagoList.remove(pagoSelected);
			if(pagoList.isEmpty()) {
				pagoSelected=null;
			}else {
				pagoSelected = pagoList.get(pagoList.size()-1);
			}
			
			totalizarPago();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		
	}
	
	public void buscarCodigoProducto() {
		try {
			
			if(criterioBusqueda!=null && criterioBusqueda.trim().length()==0) {
				return;
			}
			
			productoSelected = productoServicio.getProductoDao().getByCodigoPrincipal(criterioBusqueda, AppJsfUtil.getEstablecimiento().getIdestablecimiento());
			
			if(productoSelected==null) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTE : " + criterioBusqueda);
				return;
			}
			productoSelected.setCantidad(1);
			agregarProducto();
			criterioBusqueda = null;
//			Ajax.oncomplete("PrimeFaces.focus('formMain:intCriterioBusqueda')");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void generarRecibo() {
		try {
			
			// validar cliente
			if(cabecerSelected.getCliente()==null) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTE DATOS DEL CLIENTE");
				return;
			}
			
			if(cabecerSelected.getTotalconimpuestos().doubleValue()<=0) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "FACTURA SIN DETALLE DE FACTURACION.");
				return;
			}
			
			// validar estado
			if(cabecerSelected.getIdcabecera()!=null) {
				
				String analisisEstado = cabeceraServicio.analizarEstadoRecibo(cabecerSelected.getIdcabecera(), "GUARDAR");
				
				if(analisisEstado!=null) {
					AppJsfUtil.addErrorMessage("formMain", "ERROR", analisisEstado);
					return;
				}
				
			}
			
			// validar el valor
			if(cabecerSelected.getTotalconimpuestos().doubleValue()<totalPago.doubleValue()) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "VALOR DE PAGO MENOR AL VALOR DE LA FACTURA.");
				return;
			}
			
			cabecerSelected.setDetalleList(detalleFacList);
			cabecerSelected.setPagoList(pagoList);
			populatefactura(GenTipoDocumentoEnum.RECIBO);
			cabecerSelected.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
			cabecerSelected.setUpdated(new Date());
			cabecerSelected = cabeceraServicio.guardarComprobanteFacade(cabecerSelected);
			
			AppJsfUtil.addInfoMessage("formMain", "OK", "GUARDADO GENERAR FACTURA");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void generarFactura() {
		try {
			
			// validar cliente
			if(cabecerSelected.getCliente()==null) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTE DATOS DEL CLIENTE");
				return;
			}
			
			if(cabecerSelected.getTotalconimpuestos().doubleValue()<=0) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "FACTURA SIN DETALLE DE FACTURACION.");
				return;
			}
			
			// validar estado
			if(cabecerSelected.getIdcabecera()!=null) {
				
				String analisisEstado = cabeceraServicio.analizarEstadoFactura(cabecerSelected.getIdcabecera(), "GUARDAR");
				
				if(analisisEstado!=null) {
					AppJsfUtil.addErrorMessage("formMain", "ERROR", analisisEstado);
					return;
				}
				
			}
			
			// validar el valor
			if(totalPago.doubleValue()<cabecerSelected.getTotalconimpuestos().doubleValue()) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "VALOR DE PAGO MENOR AL VALOR DE LA FACTURA.");
				return;
			}
			
			cabecerSelected.setDetalleList(detalleFacList);
			cabecerSelected.setPagoList(pagoList);	
			populatefactura(GenTipoDocumentoEnum.FACTURA);
			cabecerSelected.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
			cabecerSelected.setUpdated(new Date());
			cabecerSelected = cabeceraServicio.guardarComprobanteFacade(cabecerSelected);
			
			AppJsfUtil.addInfoMessage("formMain", "OK", "GUARDADO GENERAR FACTURA");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	private void populatefactura(GenTipoDocumentoEnum genTipoDocumentoEnum) throws DaoException, ParametroRequeridoException {
		
		cabecerSelected.setTipoemision("1");
		cabecerSelected.setTipocomprobante(tipocomprobanteServicio.getByTipoDocumento(genTipoDocumentoEnum,
				AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa()));
		
		cabecerSelected.setEstablecimiento(establecimientoServicio.consultarByPk(AppJsfUtil.getEstablecimiento().getIdestablecimiento()));
		cabecerSelected.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
		SimpleDateFormat sf = new SimpleDateFormat("MM-yyyy");
		cabecerSelected.setPeriodofiscal(sf.format(new Date()));
		cabecerSelected.setContribuyenteespecial("5368");
		cabecerSelected.setMoneda("DOLAR");
		if(cabecerSelected.getSecuencial()==null) {
			cabecerSelected.setSecuencial(contadorPkServicio.generarNumeroDocumento(genTipoDocumentoEnum,
					AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa()));
			// clave de acceso
			cabecerSelected.setClaveacceso(ComprobanteHelper.generarAutorizacionFacade(cabecerSelected, contadorPkServicio.generarContadorTabla(TCAleatorio.ALEATORIOFACTURA, cabecerSelected.getEstablecimiento().getIdestablecimiento(),new Object[] {false})));
			cabecerSelected.setNumdocumento(TextoUtil.leftPadTexto(cabecerSelected.getEstablecimiento().getCodigoestablecimiento(),3, "0").concat("001").concat(cabecerSelected.getSecuencial()));
			cabecerSelected.setNumfactura(cabecerSelected.getNumdocumento());
			
		}
		
		cabecerSelected.setPropina(BigDecimal.ZERO);
		cabecerSelected.setEstado(ComprobanteEstadoEnum.REGISTRADO.toString());
		
		
		// tabla de total impuesto
		List<Totalimpuesto> totalimpuestoList = new ArrayList<>();
		totalimpuestoList.addAll(ComprobanteHelper.determinarIva(cabecerSelected.getDetalleList()));
		totalimpuestoList.addAll(ComprobanteHelper.determinarIce(cabecerSelected.getDetalleList()));
		cabecerSelected.setTotalimpuestoList(totalimpuestoList);
		
		// detalle impuesto
		ComprobanteHelper.determinarDetalleImpuesto(cabecerSelected.getDetalleList());
		
		// infromacion adicional 
		
		cabecerSelected.setInfoadicionalList(ComprobanteHelper.determinarInfoAdicional(cabecerSelected));
		
	}

	/**
	 * @return the clienteServicio
	 */
	public ClienteServicio getClienteServicio() {
		return clienteServicio;
	}

	/**
	 * @param clienteServicio the clienteServicio to set
	 */
	public void setClienteServicio(ClienteServicio clienteServicio) {
		this.clienteServicio = clienteServicio;
	}

	/**
	 * @return the cabecerSelected
	 */
	public Cabecera getCabecerSelected() {
		return cabecerSelected;
	}

	/**
	 * @param cabecerSelected the cabecerSelected to set
	 */
	public void setCabecerSelected(Cabecera cabecerSelected) {
		this.cabecerSelected = cabecerSelected;
	}

	/**
	 * @return the criterioCliente
	 */
	public String getCriterioCliente() {
		return criterioCliente;
	}

	/**
	 * @param criterioCliente the criterioCliente to set
	 */
	public void setCriterioCliente(String criterioCliente) {
		this.criterioCliente = criterioCliente;
	}

	/**
	 * @return the pagoList
	 */
	public List<Pago> getPagoList() {
		return pagoList;
	}

	/**
	 * @param pagoList the pagoList to set
	 */
	public void setPagoList(List<Pago> pagoList) {
		this.pagoList = pagoList;
	}

	/**
	 * @return the pagoSelected
	 */
	public Pago getPagoSelected() {
		return pagoSelected;
	}

	/**
	 * @param pagoSelected the pagoSelected to set
	 */
	public void setPagoSelected(Pago pagoSelected) {
		this.pagoSelected = pagoSelected;
	}

	/**
	 * @return the totalPago
	 */
	public BigDecimal getTotalPago() {
		return totalPago;
	}

	/**
	 * @param totalPago the totalPago to set
	 */
	public void setTotalPago(BigDecimal totalPago) {
		this.totalPago = totalPago;
	}

	/**
	 * @return the detalleFacList
	 */
	public List<Detalle> getDetalleFacList() {
		return detalleFacList;
	}

	/**
	 * @param detalleFacList the detalleFacList to set
	 */
	public void setDetalleFacList(List<Detalle> detalleFacList) {
		this.detalleFacList = detalleFacList;
	}

	/**
	 * @return the detalleSelected
	 */
	public Detalle getDetalleSelected() {
		return detalleSelected;
	}

	/**
	 * @param detalleSelected the detalleSelected to set
	 */
	public void setDetalleSelected(Detalle detalleSelected) {
		this.detalleSelected = detalleSelected;
	}

	/**
	 * @return the productoList
	 */
	public List<Producto> getProductoList() {
		return productoList;
	}

	/**
	 * @param productoList the productoList to set
	 */
	public void setProductoList(List<Producto> productoList) {
		this.productoList = productoList;
	}

	/**
	 * @return the productoSelected
	 */
	public Producto getProductoSelected() {
		return productoSelected;
	}

	/**
	 * @param productoSelected the productoSelected to set
	 */
	public void setProductoSelected(Producto productoSelected) {
		this.productoSelected = productoSelected;
	}

	/**
	 * @return the criterioBusqueda
	 */
	public String getCriterioBusqueda() {
		return criterioBusqueda;
	}

	/**
	 * @param criterioBusqueda the criterioBusqueda to set
	 */
	public void setCriterioBusqueda(String criterioBusqueda) {
		this.criterioBusqueda = criterioBusqueda;
	}

	/**
	 * @return the iceList
	 */
	public List<Ice> getIceList() {
		return iceList;
	}

	/**
	 * @param iceList the iceList to set
	 */
	public void setIceList(List<Ice> iceList) {
		this.iceList = iceList;
	}

	/**
	 * @return the ivaList
	 */
	public List<Iva> getIvaList() {
		return ivaList;
	}

	/**
	 * @param ivaList the ivaList to set
	 */
	public void setIvaList(List<Iva> ivaList) {
		this.ivaList = ivaList;
	}

	/**
	 * @return the totalSaldo
	 */
	public BigDecimal getTotalSaldo() {
		return totalSaldo;
	}

	/**
	 * @param totalSaldo the totalSaldo to set
	 */
	public void setTotalSaldo(BigDecimal totalSaldo) {
		this.totalSaldo = totalSaldo;
	}

}