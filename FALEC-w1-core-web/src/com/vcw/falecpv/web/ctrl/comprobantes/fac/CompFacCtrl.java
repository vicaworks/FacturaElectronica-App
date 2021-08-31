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

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.TextoUtil;
import com.servitec.common.util.exceptions.ParametroRequeridoException;
import com.vcw.falecpv.core.constante.ComprobanteEstadoEnum;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;
import com.vcw.falecpv.core.constante.TipoPagoFormularioEnum;
import com.vcw.falecpv.core.constante.parametrosgenericos.PGEmpresaSucursal;
import com.vcw.falecpv.core.exception.ExisteNumDocumentoException;
import com.vcw.falecpv.core.helper.ComprobanteHelper;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Detalle;
import com.vcw.falecpv.core.modelo.persistencia.Ice;
import com.vcw.falecpv.core.modelo.persistencia.Iva;
import com.vcw.falecpv.core.modelo.persistencia.Pago;
import com.vcw.falecpv.core.modelo.persistencia.Producto;
import com.vcw.falecpv.core.modelo.persistencia.Totalimpuesto;
import com.vcw.falecpv.core.servicio.CabeceraServicio;
import com.vcw.falecpv.core.servicio.ClienteServicio;
import com.vcw.falecpv.core.servicio.ConfiguracionServicio;
import com.vcw.falecpv.core.servicio.ContadorPkServicio;
import com.vcw.falecpv.core.servicio.DetalleServicio;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.falecpv.core.servicio.IceServicio;
import com.vcw.falecpv.core.servicio.InfoadicionalServicio;
import com.vcw.falecpv.core.servicio.IvaServicio;
import com.vcw.falecpv.core.servicio.PagoServicio;
import com.vcw.falecpv.core.servicio.ParametroGenericoEmpresaServicio;
import com.vcw.falecpv.core.servicio.ParametroGenericoEmpresaServicio.TipoRetornoParametroGenerico;
import com.vcw.falecpv.core.servicio.ProductoServicio;
import com.vcw.falecpv.core.servicio.TipocomprobanteServicio;
import com.vcw.falecpv.core.servicio.TipopagoServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.common.RideCtrl;
import com.vcw.falecpv.web.ctrl.facturacion.FacEmitidaCtrl;
import com.vcw.falecpv.web.ctrl.facturacion.RecEmitidoCtrl;
import com.vcw.falecpv.web.servicio.SriDispacher;
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
	
	@EJB
	private DetalleServicio detalleServicio;
	
	@EJB
	private PagoServicio pagoServicio;
	
	@EJB
	private InfoadicionalServicio infoadicionalServicio;
	
	@EJB
	private ParametroGenericoEmpresaServicio parametroGenericoEmpresaServicio;
	
	@EJB
	private ConfiguracionServicio configuracionServicio;
	
	@EJB
	private SriDispacher sriDispacher;
	
	
	private Cabecera cabecerSelected;
	private String criterioCliente;
	private List<Pago> pagoList;
	private Pago pagoSelected;
	private BigDecimal totalPago = BigDecimal.ZERO;
	private BigDecimal totalSaldo = BigDecimal.ZERO;
	private List<Detalle> detalleFacList;
	private Detalle detalleSelected;
	private String criterioBusqueda;
	private List<Ice> iceList;
	private List<Iva> ivaList;
	private BigDecimal porcentajeRenta;
	private BigDecimal porcentajeIva;
	private String callModule;
	private RideCtrl rideCtrl;
 	
	/**
	 * 
	 */
	public CompFacCtrl() {
		
	}
	
	@PostConstruct
	public void init() {
		try {
			detalleFacList = null;
			detalleSelected = null;
			criterioBusqueda = null;
			
			if(establecimientoMain!=null) {
				nuevaFactura();
				consultarIce();
				consultarIva();
			}
			
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
				establecimientoMain.getEmpresa().getIdempresa()));
	}
	
	public void consultarIce()throws DaoException{
		iceList = iceServicio.getIceDao().getByEstado(EstadoRegistroEnum.ACTIVO	, establecimientoMain.getEmpresa().getIdempresa());
	}
	
	public void consultarIva()throws DaoException {
		ivaList = ivaServicio.getIvaDao().getByEstado(EstadoRegistroEnum.ACTIVO, establecimientoMain.getEmpresa().getIdempresa());
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
			
			nuevaFactura();
			detalleFacList = null;
			detalleSelected = null;
			criterioBusqueda = null;
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public String nuevoFromMain() {
		try {
			
			nuevaFactura();
			detalleFacList = null;
			detalleSelected = null;
			criterioBusqueda = null;
			
			return "./factura.jsf?faces-redirect=true";
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}

		return null;
	}
	
	public void nuevaFactura() throws DaoException, NumberFormatException, ParametroRequeridoException {
		
		criterioBusqueda = null;
		criterioCliente = null;
		cabecerSelected = new Cabecera();
		cabecerSelected.setFechaemision(new Date());
		infoadicionalList = null;
		enableAccion = false;
		inicializarSecuencia(cabecerSelected);
		// estado borrador
		cabecerSelected.setBorrador(parametroGenericoEmpresaServicio.consultarParametroEstablecimiento(PGEmpresaSucursal.ESTADO_BORRADOR, TipoRetornoParametroGenerico.BOOLEAN, establecimientoMain.getIdestablecimiento()));
		criterioCliente = null;
		pagoList = null;
		pagoSelected = null;
		totalPago = BigDecimal.ZERO;
		totalSaldo = BigDecimal.ZERO;
		porcentajeIva = null;
		porcentajeRenta = null;
		descripcionIva = "(0%)";
		consultarIce();
		consultarIva();
		populateTipoPago();
		// infoadicional configuracion
		cabecerSelected.setTipocomprobante(tipocomprobanteServicio.getByTipoDocumento(GenTipoDocumentoEnum.FACTURA));
		cabecerSelected.setEstablecimiento(establecimientoServicio.consultarByPk(establecimientoMain.getIdestablecimiento()));
		configuracionServicio.populateInformacionAdicional(cabecerSelected);
		infoadicionalList = cabecerSelected.getInfoadicionalList();
	}
	
	public void agregarProducto(Producto productoSelected) {
		try {
			
			detalleSelected = new Detalle();
			detalleSelected.setCodproducto(productoSelected.getCodigoprincipal());			
			detalleSelected.setCantidad(BigDecimal.valueOf(1));
			detalleSelected.setDescripcion(productoSelected.getNombre());
			detalleSelected.setPreciounitario(productoSelected.getPreciouno());
			detalleSelected.setProducto(productoSelected);
			detalleSelected.setPreciounitario(productoSelected.getPreciouno());
			detalleSelected.setPorcentajeDescuento(productoSelected.getPorcentajedescuento());
			detalleSelected.setPreciocompra(productoSelected.getPreciounitario());
			detalleSelected.setIva(productoSelected.getIva());
			detalleSelected.setIce(productoSelected.getIce());
			detalleSelected.setPrecioVenta(1);
			calcularItem(detalleSelected,true);
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmListProducto", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void cambiarPrecioVenta(Integer precio) {
		switch (precio) {
		case 1:
			detalleSelected.setPreciounitario(detalleSelected.getProducto().getPreciouno());
			detalleSelected.setPrecioVenta(1);
			break;
		case 2:
			detalleSelected.setPreciounitario(detalleSelected.getProducto().getPreciodos());
			detalleSelected.setPrecioVenta(2);
			break;	
		case 3:
			detalleSelected.setPreciounitario(detalleSelected.getProducto().getPreciotres());
			detalleSelected.setPrecioVenta(3);
			break;
		default:
			break;
		}
		calcularItem(detalleSelected, true);		
		if(detalleSelected.getProducto().getTipoventa()==2) {
			AppJsfUtil.executeJavaScript("PrimeFaces.focus('frmListProducto:tvPLmain:fsvFactura:innCantFrm')");						
		}else {
			AppJsfUtil.executeJavaScript("PrimeFaces.focus('frmListProducto:tvPLmain:fsvFactura:innCantFrm2')");
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
			dFac.setDescuento(dFac.getPorcentajeDescuento().divide(BigDecimal.valueOf(100))
					.multiply(dFac.getPreciounitario()).multiply(dFac.getCantidad())
					.setScale(2, RoundingMode.HALF_UP));
		}
		dFac.setPreciototalsinimpuesto(dFac.getCantidad().multiply(dFac.getPreciounitario()).add(dFac.getDescuento().negate()).setScale(2, RoundingMode.HALF_UP));
		dFac.setValorice(dFac.getIce().getValor().divide(BigDecimal.valueOf(100)).multiply(dFac.getPreciototalsinimpuesto()).setScale(2, RoundingMode.HALF_UP));
		dFac.setValoriva(dFac.getIva().getValor().divide(BigDecimal.valueOf(100))
				.multiply(dFac.getPreciototalsinimpuesto().add(dFac.getValorice())).setScale(2, RoundingMode.HALF_UP));
		dFac.setPreciototal(dFac.getPreciototalsinimpuesto().add(dFac.getValoriva().add(dFac.getValorice())).setScale(2, RoundingMode.HALF_UP));
		cabecerSelected.setValorretenidoiva(BigDecimal.ZERO);
		cabecerSelected.setValorretenidorenta(BigDecimal.ZERO);
		
	}
	
	private void totalizar() throws DaoException, NumberFormatException, ParametroRequeridoException {
		if(cabecerSelected==null) nuevaFactura();
		
		
		cabecerSelected.setTotaliva(BigDecimal.ZERO);
		cabecerSelected.setTotalice(BigDecimal.ZERO);
		cabecerSelected.setTotaldescuento(BigDecimal.ZERO);
		cabecerSelected.setTotalsinimpuestos(BigDecimal.ZERO);
		cabecerSelected.setTotalconimpuestos(BigDecimal.ZERO);
		
		int i= 1;
		if(detalleFacList!=null) {
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
		}
		
		cabecerSelected.setTotaliva(cabecerSelected.getTotaliva().setScale(2, RoundingMode.HALF_UP));
		cabecerSelected.setTotalice(cabecerSelected.getTotalice().setScale(2, RoundingMode.HALF_UP));
		cabecerSelected.setTotaldescuento(cabecerSelected.getTotaldescuento().setScale(2, RoundingMode.HALF_UP));
		cabecerSelected.setTotalsinimpuestos(cabecerSelected.getTotalsinimpuestos().setScale(2, RoundingMode.HALF_UP));
		cabecerSelected.setTotalconimpuestos(cabecerSelected.getTotalsinimpuestos().add(cabecerSelected.getTotaliva()).add(cabecerSelected.getTotalice()).setScale(2, RoundingMode.HALF_UP));
		calcularRettencion("RENTA");
		calcularRettencion("IVA");
		cabecerSelected.setValorretenido(cabecerSelected.getValorretenidorenta().add(cabecerSelected.getValorretenidoiva()).setScale(2, RoundingMode.HALF_UP));
		cabecerSelected.setTotalpagar(cabecerSelected.getTotalconimpuestos().add(cabecerSelected.getValorretenido().negate()).setScale(2, RoundingMode.HALF_UP));
		pagoList = null;
		totalPago = BigDecimal.ZERO;
		
		if(totalPago!=null && totalPago.doubleValue()>0) {
			totalizarPago();
		}
		// determinar descripcion iva
		determinarDescripcionIVA(detalleFacList);
		
	}
	
	public void totalizarRetencion() {
		try {
			if(cabecerSelected == null) {
				return;
			}
			
			if (cabecerSelected.getTotalsinimpuestos().doubleValue()<=0) {
				return;
			}
			
			totalizar();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
//	private Detalle existeProductoLista() {
//		for (Detalle d : detalleFacList) {
//			if(d.getProducto()!=null && d.getProducto().getIdproducto().equals(productoSelected.getIdproducto())) {
//				return d;
//			}
//		}
//		
//		return null;
//	}
	
	public void agregarDetalle() {
		try {
			if(detalleFacList==null) {
				detalleFacList = new ArrayList<>();
			}
			if(detalleSelected.getAccion().equals("NUEVO")) {
				detalleFacList.add(detalleSelected);				
			}
			totalizar();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmListProducto", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void agregarItem() {
		try {
			
			consultarIce();
			consultarIva();
			
			detalleSelected = new Detalle();
			detalleSelected.setCantidad(BigDecimal.valueOf(1));			
			detalleSelected.setDescuento(BigDecimal.ZERO);
			detalleSelected.setPreciounitario(BigDecimal.ZERO);
			detalleSelected.setDescuento(BigDecimal.ZERO);
			detalleSelected.setValorice(BigDecimal.ZERO);
			detalleSelected.setValoriva(BigDecimal.ZERO);
			detalleSelected.setProducto(null);
			detalleSelected.setPrecioVenta(0);
			detalleSelected.setIva(ivaServicio.getIvaDao().getDefecto(establecimientoMain.getEmpresa().getIdempresa()));
			// valida
			if(detalleSelected.getIva()==null) {
				AppJsfUtil.addErrorMessage("frmListProducto","ERROR","NO EXISTE IVA POR DEFECTO, CONFIGURACION / IVA : SELECCIONAR POR DEFECTO");
				return;
			}
			detalleSelected.setIce(iceList.stream().filter(x->x.getCodigo().equals("-1")).findFirst().orElse(null));
			if(detalleSelected.getIce()==null) {
				AppJsfUtil.addErrorMessage("frmListProducto","ERROR","NO EXISTE ICE CON VALOR 0, CONFIGURACION / ICE : CREAR ICE VALOR 0 Y CODIGO SRI [-1].");
				return;
			}
			
			calcularItem(detalleSelected, false);
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmListProducto", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
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
		
		totalSaldo = cabecerSelected.getTotalpagar().add(totalPago.negate()).setScale(2, RoundingMode.HALF_UP);
		if(totalSaldo.doubleValue()<0) {
			totalSaldo = BigDecimal.ZERO;
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
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTE DETALLE DE PRODUCTOS O SERVICIOS.");
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
			if(totalPago.doubleValue()!=cabecerSelected.getTotalconimpuestos().doubleValue()) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "VALOR DE PAGO DIFERENTE AL VALOR DEL RECIBO.");
				return;
			}
			
			cabecerSelected.setDetalleList(detalleFacList);
			cabecerSelected.setPagoList(pagoList);
			noEditarSecuencial(cabecerSelected);
			cabecerSelected.setGenTipoDocumentoEnum(GenTipoDocumentoEnum.RECIBO);
			populatefactura(GenTipoDocumentoEnum.RECIBO);
			cabecerSelected.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
			cabecerSelected.setUpdated(new Date());
			cabecerSelected.setInfoadicionalList(new ArrayList<>());
			cabecerSelected = cabeceraServicio.guardarComprobanteFacade(cabecerSelected);
			noEditarSecuencial(cabecerSelected);
			
			RecEmitidoCtrl recEmitidoCtrl = (RecEmitidoCtrl) AppJsfUtil.getManagedBean("recEmitidoCtrl");
			if(recEmitidoCtrl!=null) {
				recEmitidoCtrl.buscar();
			}
			
			showRide();
			
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
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "FACTURA SIN DETALLE DE PRODUCTOS O SERVICIOS.");
				return;
			}
			
			// validar estado
			if(cabecerSelected.getIdcabecera()!=null) {
				
				String analisisEstado = cabeceraServicio.analizarEstadoComprobante(cabecerSelected.getIdcabecera(), "GUARDAR");
				
				if(analisisEstado!=null) {
					AppJsfUtil.addErrorMessage("formMain", "ERROR", analisisEstado);
					return;
				}
			}
			
			// validar el valor
//			if(!cabecerSelected.isBorrador() && totalPago.doubleValue()!=cabecerSelected.getTotalpagar().doubleValue()) {
			if(totalPago.doubleValue()!=cabecerSelected.getTotalpagar().doubleValue()) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "VALOR DE PAGO DIFERENTE AL VALOR A PAGAR.");
				return;
			}
			
			cabecerSelected.setDetalleList(detalleFacList);
			cabecerSelected.setPagoList(pagoList);
			cabecerSelected.setGenTipoDocumentoEnum(GenTipoDocumentoEnum.FACTURA);
			populatefactura(GenTipoDocumentoEnum.FACTURA);
			cabecerSelected.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
			cabecerSelected.setUpdated(new Date());
			cabecerSelected = cabeceraServicio.guardarComprobanteFacade(cabecerSelected);
			infoadicionalList = cabecerSelected.getInfoadicionalList();
			noEditarSecuencial(cabecerSelected);
			cabecerSelected.setIdUsurioTransaccion(AppJsfUtil.getUsuario().getIdusuario());
			sriDispacher.queue_comprobanteSriDispacher(cabecerSelected);
			if(cabecerSelected.isBorrador()) {
				messageCtrl.cargarMenssage("AVISO", "BORRADOR DE FACTURA GENERADA CORRECTAMENTE.", "WARNING");
			}else {
				messageCtrl.cargarMenssage("OK", "FACTURA GENERADA CORRECTAMENTE.", "OK");
			}
			
			FacEmitidaCtrl facEmitidaCtrl = (FacEmitidaCtrl) AppJsfUtil.getManagedBean("facEmitidaCtrl");
			if(facEmitidaCtrl!=null) {
				facEmitidaCtrl.buscar();
			}
			
		} catch (ExisteNumDocumentoException e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", msg.getString("mensaje.secuencialconprobante", cabecerSelected.getSecuencial()));
			cabecerSelected.setEstado(ComprobanteEstadoEnum.BORRADOR.toString());
			cabecerSelected.setSecuencial(null);
			cabecerSelected.setNumdocumento(null);
			cabecerSelected.setClaveacceso(null);
			cabecerSelected.setNumeroautorizacion(null);
		}catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
			cabecerSelected.setEstado(ComprobanteEstadoEnum.BORRADOR.toString());
			cabecerSelected.setSecuencial(null);
			cabecerSelected.setNumdocumento(null);
			cabecerSelected.setClaveacceso(null);
			cabecerSelected.setNumeroautorizacion(null);
		}
	}
	
	public void imprimir() {
		// verifica si solo debe de imprimir
		if(cabecerSelected.getIdcabecera()!=null) {
			if(cabeceraServicio.getCabeceraDao().isImprimir(cabecerSelected.getIdcabecera())){
				showRide();
				return;
			}
		}
		// pone el nuevo estado
		boolean estadoTemp = cabecerSelected.isBorrador();
		cabecerSelected.setBorrador(false);
		generarFactura();
		AppJsfUtil.ajaxUpdate("formMain");
		if(AppJsfUtil.existErrors()) {
			cabecerSelected.setBorrador(estadoTemp);
			return;
		}
		showRide();
	}
	
	private void showRide() {
		// despliega el comprobante
		rideCtrl.setIdCabecera(cabecerSelected.getIdcabecera());
		if(cabecerSelected.getTipocomprobante().getIdentificador().equals("00")) {
			rideCtrl.setInicialComprobante("REC-");
		}else {
			rideCtrl.setInicialComprobante("FAC-");
		}
		rideCtrl.setNumComprobante(ComprobanteHelper.formatNumDocumento(cabecerSelected.getNumdocumento()));
		rideCtrl.showRide();
		
	}
	
	
	private void determinarPeriodoFiscal() {
		SimpleDateFormat sf = new SimpleDateFormat("MM/yyyy");
		cabecerSelected.setPeriodofiscal(sf.format(cabecerSelected.getFechaemision()));
	}
	
	private void populatefactura(GenTipoDocumentoEnum genTipoDocumentoEnum) throws DaoException, ParametroRequeridoException {
		
		cabecerSelected.setTipoemision("1");
		cabecerSelected.setTipocomprobante(tipocomprobanteServicio.getByTipoDocumento(genTipoDocumentoEnum));
		
		cabecerSelected.setEstablecimiento(establecimientoServicio.consultarByPk(establecimientoMain.getIdestablecimiento()));
		cabecerSelected.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
		determinarPeriodoFiscal();
		cabecerSelected.setContribuyenteespecial("5368");
		cabecerSelected.setMoneda("DOLAR");
		cabecerSelected.setPropina(BigDecimal.ZERO);
		if(cabecerSelected.getIdcabecera()==null || (cabecerSelected.getIdcabecera()!=null && (cabecerSelected.getEstado().equals(ComprobanteEstadoEnum.BORRADOR.toString()) || cabecerSelected.getEstado().equals(ComprobanteEstadoEnum.RECHAZADO_SRI.toString()) ))) {
			
			if(genTipoDocumentoEnum.equals(GenTipoDocumentoEnum.FACTURA)) {
				
				if(cabecerSelected.isBorrador()) {
					cabecerSelected.setEstado(ComprobanteEstadoEnum.BORRADOR.toString());
				}else {
					cabecerSelected.setEstado(ComprobanteEstadoEnum.PENDIENTE.toString());
				}
				
			}else {
				
				cabecerSelected.setEstado(ComprobanteEstadoEnum.REGISTRADO.toString());
			}
			
		}
		cabecerSelected.setResumenpago(ComprobanteHelper.determinarResumenPago(pagoList));
		cabecerSelected.setValorapagar(cabecerSelected.getTotalpagar());
		
		// tabla de total impuesto
		List<Totalimpuesto> totalimpuestoList = new ArrayList<>();
		totalimpuestoList.addAll(ComprobanteHelper.determinarIva(cabecerSelected.getDetalleList()));
		totalimpuestoList.addAll(ComprobanteHelper.determinarIce(cabecerSelected.getDetalleList()));
		cabecerSelected.setTotalimpuestoList(totalimpuestoList);
		
		// detalle impuesto
		ComprobanteHelper.determinarDetalleImpuesto(cabecerSelected.getDetalleList());
		
		// infromacion adicional 
		cabecerSelected.setInfoadicionalList(infoadicionalList);
		
	}
	
	public void editarSecuencialAction() {
		try {
			
			editarSecuencial(cabecerSelected, "formMain:intSecDocumento");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void noEditarSecuencialAction() {
		try {
			
			noEditarSecuencial(cabecerSelected);
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void calcularRettencionAction(String tipo) {
		try {
			
			if(cabecerSelected == null) {
				return;
			}
			
			if (cabecerSelected.getTotalsinimpuestos().doubleValue()<=0) {
				return;
			}
			
			calcularRettencion(tipo);
			totalizar();
			porcentajeIva = null;
			porcentajeRenta = null;
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void calcularRettencion(String tipo) {
		try {
			
			if(cabecerSelected == null) {
				return;
			}
			
			if (cabecerSelected.getTotalsinimpuestos().doubleValue()<=0) {
				return;
			}
			
			switch (tipo) {
			case "RENTA":
				if(porcentajeRenta!=null) {
					cabecerSelected.setValorretenidorenta(
							porcentajeRenta.divide(BigDecimal.valueOf(100d)).multiply(cabecerSelected.getTotalsinimpuestos()).setScale(2, RoundingMode.HALF_UP));
				}
				break;
			case "IVA":
				if(porcentajeIva!=null) {
					cabecerSelected.setValorretenidoiva(
							porcentajeIva.divide(BigDecimal.valueOf(100d)).multiply(cabecerSelected.getTotaliva()).setScale(2, RoundingMode.HALF_UP));
				}
				break;	
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	
	public String editar(String idFactura) throws DaoException, NumberFormatException, ParametroRequeridoException {
		nuevaFactura();
		cabecerSelected = cabeceraServicio.consultarByPk(idFactura);
		
		if(cabecerSelected==null) {
			return "NO EXISTE LA FACTURA SELECCIONADA";
		}
		
		// analizar estado para habilitar crud
		habilitarCrud(cabecerSelected.getEstado());
		
		detalleFacList = detalleServicio.getDetalleDao().getByIdCabecera(idFactura);
		infoadicionalList = infoadicionalServicio.getInfoadicionalDao().getByIdCabecera(idFactura);
		totalizar();
		pagoList = pagoServicio.getPagoDao().getByIdCabecera(idFactura);
		pagoList.stream().forEach(x->{
			x.setTipoPagoFormularioEnum(TipoPagoFormularioEnum.getByCodInterno(x.getTipopago().getCodinterno()));
		}); 
		totalizarPago();
		if (cabecerSelected.getEstado().equals(ComprobanteEstadoEnum.BORRADOR.toString())) {
			cabecerSelected.setBorrador(true);
		}
		
		return null;
	}
	
//	public void establecerFocoDetalle() {
//		try {
//			detalleSelected = detalleFacList.stream().filter(x->x.getIddetalle().equals(FacesUtils.getParameter("idDetalle"))).findFirst().orElse(null);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	public String crearUnaCopia(String idCabeceraSeleccion) {
		try {
			
			nuevaFactura();
			cabecerSelected.setCliente(cabeceraServicio.getClienteComprobante(idCabeceraSeleccion));
			detalleFacList = detalleServicio.getDetalleDao().getByIdCabecera(idCabeceraSeleccion);
			int cont=1;
			for (Detalle d : detalleFacList) {
				d.setIddetalle("M" + (cont++));
			}
			totalizar();
			
			return "./factura.jsf?faces-redirect=true";
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}

		return null;
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

	/**
	 * @return the porcentajeRenta
	 */
	public BigDecimal getPorcentajeRenta() {
		return porcentajeRenta;
	}

	/**
	 * @param porcentajeRenta the porcentajeRenta to set
	 */
	public void setPorcentajeRenta(BigDecimal porcentajeRenta) {
		this.porcentajeRenta = porcentajeRenta;
	}

	/**
	 * @return the porcentajeIva
	 */
	public BigDecimal getPorcentajeIva() {
		return porcentajeIva;
	}

	/**
	 * @param porcentajeIva the porcentajeIva to set
	 */
	public void setPorcentajeIva(BigDecimal porcentajeIva) {
		this.porcentajeIva = porcentajeIva;
	}

	/**
	 * @return the callModule
	 */
	public String getCallModule() {
		return callModule;
	}

	/**
	 * @param callModule the callModule to set
	 */
	public void setCallModule(String callModule) {
		this.callModule = callModule;
	}

	/**
	 * @return the rideCtrl
	 */
	public RideCtrl getRideCtrl() {
		return rideCtrl;
	}

	/**
	 * @param rideCtrl the rideCtrl to set
	 */
	public void setRideCtrl(RideCtrl rideCtrl) {
		this.rideCtrl = rideCtrl;
	}

}
