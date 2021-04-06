/**
 * 
 */
package com.vcw.falecpv.web.ctrl.proforma;

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
import com.servitec.common.util.FechaUtil;
import com.servitec.common.util.TextoUtil;
import com.servitec.common.util.exceptions.ParametroRequeridoException;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;
import com.vcw.falecpv.core.constante.TipoPagoFormularioEnum;
import com.vcw.falecpv.core.constante.parametrosgenericos.PGEmpresaEnum;
import com.vcw.falecpv.core.exception.ExisteNumDocumentoException;
import com.vcw.falecpv.core.helper.ComprobanteHelper;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Detalle;
import com.vcw.falecpv.core.modelo.persistencia.Establecimiento;
import com.vcw.falecpv.core.modelo.persistencia.Ice;
import com.vcw.falecpv.core.modelo.persistencia.Iva;
import com.vcw.falecpv.core.modelo.persistencia.Pago;
import com.vcw.falecpv.core.modelo.persistencia.Producto;
import com.vcw.falecpv.core.modelo.persistencia.Totalimpuesto;
import com.vcw.falecpv.core.servicio.CabeceraServicio;
import com.vcw.falecpv.core.servicio.ClienteServicio;
import com.vcw.falecpv.core.servicio.ContadorPkServicio;
import com.vcw.falecpv.core.servicio.CotizacionServicio;
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
import com.vcw.falecpv.web.util.AppJsfUtil;
import com.xpert.faces.utils.FacesUtils;

/**
 * @author cristianvillarreal
 *
 */
@Named
@SessionScoped
public class CotizacionFormCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6951769378117753191L;
	
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
	private CotizacionServicio cotizacionServicio; 
	
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
	private BigDecimal porcentajeRenta;
	private BigDecimal porcentajeIva;
	private boolean cotizacionProductoDetalle = false;
	
	/**
	 * 
	 */
	public CotizacionFormCtrl() {
	}
	
	@PostConstruct
	public void init() {
		try {
			establecimientoFacade(establecimientoServicio, false);
			productoSelected = null;
			nuevaCotizacion();
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
			nuevaCotizacion();
			detalleFacList = null;
			detalleSelected = null;
			criterioBusqueda = null;
			consultarProductos();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void nuevoFromMain(Establecimiento establecimiento) {
		try {
			this.establecimientoMain = establecimiento;
			productoSelected = null;
			nuevaCotizacion();
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
		productoList = productoServicio.getProductoDao().consultarAllImageEager(establecimientoMain.getIdestablecimiento(),criterioBusqueda);
	}
	
	public void nuevaCotizacion() throws DaoException, NumberFormatException, ParametroRequeridoException {
		criterioBusqueda = null;
		criterioCliente = null;
		cabecerSelected = new Cabecera();
		cabecerSelected.setFechaemision(new Date());
		cabecerSelected.setFechaVencimiento(FechaUtil.agregarDias(cabecerSelected.getFechaemision(), 15));
		cabecerSelected.setAutorizacionBol(false);
		infoadicionalList = null;
		inicializarSecuencia(cabecerSelected);
		cabecerSelected.setSecuencialCaja("PRF");
		criterioCliente = null;
		pagoList = null;
		pagoSelected = null;
		totalPago = BigDecimal.ZERO;
		totalSaldo = BigDecimal.ZERO;
		porcentajeIva = null;
		porcentajeRenta = null;
		consultarIce();
		consultarIva();
		populateTipoPago();
		// autorizacion en parametros
		cabecerSelected.setAutorizacion(((boolean)parametroGenericoEmpresaServicio.consultarParametroEmpresa(PGEmpresaEnum.COTIZACION_AUTORIZACION, TipoRetornoParametroGenerico.BOOLEAN, establecimientoMain.getEmpresa().getIdempresa()))?1:0);
		// si despliega detalle
		cotizacionProductoDetalle = parametroGenericoEmpresaServicio.consultarParametroEmpresa(PGEmpresaEnum.COTIZACION_PRODUCTO_DESCRIPCION, TipoRetornoParametroGenerico.BOOLEAN, establecimientoMain.getEmpresa().getIdempresa());
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
		cabecerSelected.setValorretenidoiva(BigDecimal.ZERO);
		cabecerSelected.setValorretenidorenta(BigDecimal.ZERO);
		
	}
	
	private void totalizar() throws DaoException, NumberFormatException, ParametroRequeridoException {
		if(cabecerSelected==null) nuevaCotizacion();
		
		
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
		calcularRetencion("RENTA");
		calcularRetencion("IVA");
		cabecerSelected.setValorretenido(cabecerSelected.getValorretenidorenta().add(cabecerSelected.getValorretenidoiva()).setScale(2, RoundingMode.HALF_UP));
		cabecerSelected.setTotalpagar(cabecerSelected.getTotalconimpuestos().add(cabecerSelected.getValorretenido().negate()).setScale(2, RoundingMode.HALF_UP));
		if(totalPago!=null && totalPago.doubleValue()>0) {
			totalizarPago();
		}
		
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
			// valida
			if(detalleSelected.getIva()==null) {
				AppJsfUtil.addErrorMessage("formMain","ERROR","NO EXISTE IVA POR DEFECTO, CONFIGURACION / IVA : SELECCIONAR POR DEFECTO");
				return;
			}
			detalleSelected.setIce(iceList.stream().filter(x->x.getValor().doubleValue()==0d).findFirst().orElse(null));
			if(detalleSelected.getIce()==null) {
				AppJsfUtil.addErrorMessage("formMain","ERROR","NO EXISTE ICE CON VALOR 0, CONFIGURACION / ICE : CREAR ICE VALOR 0.");
				return;
			}
			
			detalleFacList.add(detalleSelected);
			
			calcularItem(detalleSelected, false);
			totalizar();
			Ajax.oncomplete("PrimeFaces.focus('formMain:formulario:pvDetalleDT:" + (detalleFacList.size()-1) + ":intDetNombreProducto');");
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void eliminarDetalle() {
		try {
			
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
	
	public void buscarCodigoProducto() {
		try {
			
			if(criterioBusqueda!=null && criterioBusqueda.trim().length()==0) {
				return;
			}
			
			productoSelected = productoServicio.getProductoDao().getByCodigoPrincipal(criterioBusqueda, establecimientoMain.getIdestablecimiento());
			
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
	
	
	public void generarCotizacion() {
		try {
			
			// validar cliente
			if(cabecerSelected.getCliente()==null) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTE DATOS DEL CLIENTE");
				return;
			}
			
			if(cabecerSelected.getTotalconimpuestos().doubleValue()<=0) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "COTIZACION SIN DETALLE.");
				return;
			}
			
			// validar estado
			if(cabecerSelected.getIdcabecera()!=null) {
				
				String analisisEstado = cotizacionServicio.analizarEstado(cabecerSelected.getIdcabecera(), "GUARDAR");
				
				if(analisisEstado!=null) {
					AppJsfUtil.addErrorMessage("formMain", "ERROR", analisisEstado);
					return;
				}
				
			}
			
			cabecerSelected.setDetalleList(detalleFacList);
			cabecerSelected.setPagoList(pagoList);
			cabecerSelected.setGenTipoDocumentoEnum(GenTipoDocumentoEnum.COTIZACION);
			populateCotizacion(GenTipoDocumentoEnum.COTIZACION);
			cabecerSelected.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
			cabecerSelected.setUpdated(new Date());
			cabecerSelected = cabeceraServicio.guardarComprobanteFacade(cabecerSelected);
			noEditarSecuencial(cabecerSelected);
			
			// actlualizar la consulta pantalla principal
			CotizacionCtrl cotizacionCtrl = (CotizacionCtrl)AppJsfUtil.getManagedBean("cotizacionCtrl");
			cotizacionCtrl.consultar();
			
			messageCtrl.cargarMenssage("OK", "COTIZACION GENERADA CORRECTAMENTE.", "OK");
			
		} catch (ExisteNumDocumentoException e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	

	private void determinarPeriodoFiscal() {
		SimpleDateFormat sf = new SimpleDateFormat("MM/yyyy");
		cabecerSelected.setPeriodofiscal(sf.format(cabecerSelected.getFechaemision()));
	}

	private void populateCotizacion(GenTipoDocumentoEnum genTipoDocumentoEnum) throws DaoException, ParametroRequeridoException {
		
		cabecerSelected.setTipoemision("1");
		cabecerSelected.setTipocomprobante(tipocomprobanteServicio.getByTipoDocumento(genTipoDocumentoEnum));
		
		cabecerSelected.setEstablecimiento(establecimientoServicio.consultarByPk(establecimientoMain.getIdestablecimiento()));
		cabecerSelected.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
		determinarPeriodoFiscal();
		cabecerSelected.setContribuyenteespecial("5368");
		cabecerSelected.setMoneda("DOLAR");
		cabecerSelected.setPropina(BigDecimal.ZERO);
		if(cabecerSelected.isAutorizacionBol()) {
			cabecerSelected.setEstado("AUTORIZACION");
		}else {
			cabecerSelected.setEstado("SEGUIMIENTO");
			
		}
		cabecerSelected.setResumenpago("EFECTIVO");
		cabecerSelected.setValorapagar(cabecerSelected.getTotalpagar());
		
		// tabla de total impuesto
		List<Totalimpuesto> totalimpuestoList = new ArrayList<>();
		totalimpuestoList.addAll(ComprobanteHelper.determinarIva(cabecerSelected.getDetalleList()));
		totalimpuestoList.addAll(ComprobanteHelper.determinarIce(cabecerSelected.getDetalleList()));
		cabecerSelected.setTotalimpuestoList(totalimpuestoList);
		
		// detalle impuesto
		ComprobanteHelper.determinarDetalleImpuesto(cabecerSelected.getDetalleList());
		
		// verifica los contenidos
		if(cabecerSelected.getContenido1()!=null) {
			cabecerSelected.setContenido1(cabecerSelected.getContenido1().replaceAll("\\r|\\n", ""));
		}
		if(cabecerSelected.getContenido2()!=null) {
			cabecerSelected.setContenido2(cabecerSelected.getContenido2().replaceAll("\\r|\\n", ""));
		}
		if(cabecerSelected.getContenido3()!=null) {
			cabecerSelected.setContenido3(cabecerSelected.getContenido3().replaceAll("\\r|\\n", ""));
		}
		
	}
	
	public void calcularRetencionAction(String tipo) {
		try {
			
			if(cabecerSelected == null) {
				return;
			}
			
			if (cabecerSelected.getTotalsinimpuestos().doubleValue()<=0) {
				return;
			}
			
			calcularRetencion(tipo);
			totalizar();
			porcentajeIva = null;
			porcentajeRenta = null;
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void calcularRetencion(String tipo) {
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
	
	
	public String editar(String idCotizacion) throws DaoException, NumberFormatException, ParametroRequeridoException {
		
		nuevaCotizacion();
		cabecerSelected = cabeceraServicio.consultarByPk(idCotizacion);
		
		if(cabecerSelected==null) {
			return "NO EXISTE LA COTIZACIONES SELECCIONADA";
		}
		
		detalleFacList = detalleServicio.getDetalleDao().getByIdCabecera(idCotizacion);
		pagoList = pagoServicio.getPagoDao().getByIdCabecera(idCotizacion);
		pagoList.stream().forEach(x->{
			x.setTipoPagoFormularioEnum(TipoPagoFormularioEnum.getByCodInterno(x.getTipopago().getCodinterno()));
		}); 
		infoadicionalList = infoadicionalServicio.getInfoadicionalDao().getByIdCabecera(idCotizacion);
		totalizar();
		totalizarPago();
		
		return null;
	}
	
	public void establecerFocoDetalle() {
		try {
			detalleSelected = detalleFacList.stream().filter(x->x.getIddetalle().equals(FacesUtils.getParameter("idDetalle"))).findFirst().orElse(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void crearUnaCopia(String idCabeceraSeleccion) {
		try {
			
			nuevaCotizacion();
			Cabecera c= cabeceraServicio.consultarByPk(idCabeceraSeleccion);
			cabecerSelected.setCliente(c.getCliente());
			cabecerSelected.setResumen(c.getResumen());
			cabecerSelected.setContenido1(c.getContenido1());
			cabecerSelected.setContenido2(c.getContenido2());
			cabecerSelected.setContenido3(c.getContenido3());
			detalleFacList = detalleServicio.getDetalleDao().getByIdCabecera(idCabeceraSeleccion);
			int cont=1;
			for (Detalle d : detalleFacList) {
				d.setIddetalle("M" + (cont++));
			}
			totalizar();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}

	}
	
	public void cambioEstablecimiento(Establecimiento est) {
		try {
			super.setEstablecimientoMain(est);
			productoSelected = null;
			nuevaCotizacion();
			detalleFacList = null;
			detalleSelected = null;
			criterioBusqueda = null;
			consultarProductos();
		} catch (Exception e) {
		}
	}

	public boolean habilitarAutorizar() {
		try {
			
			return parametroGenericoEmpresaServicio.consultarParametroEmpresa(
					PGEmpresaEnum.COTIZACION_AUTORIZACION, TipoRetornoParametroGenerico.BOOLEAN,
					establecimientoMain.getEmpresa().getIdempresa());
			
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		return false;
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
	 * @return the cotizacionProductoDetalle
	 */
	public boolean isCotizacionProductoDetalle() {
		return cotizacionProductoDetalle;
	}

	/**
	 * @param cotizacionProductoDetalle the cotizacionProductoDetalle to set
	 */
	public void setCotizacionProductoDetalle(boolean cotizacionProductoDetalle) {
		this.cotizacionProductoDetalle = cotizacionProductoDetalle;
	}

}
