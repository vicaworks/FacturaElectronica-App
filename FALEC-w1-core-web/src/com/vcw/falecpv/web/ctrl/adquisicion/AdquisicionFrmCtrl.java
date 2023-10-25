/**
 * 
 */
package com.vcw.falecpv.web.ctrl.adquisicion;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import com.vcw.falecpv.core.constante.ComprobanteEstadoEnum;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.constante.contadores.TipoComprobanteEnum;
import com.vcw.falecpv.core.modelo.persistencia.Adquisicion;
import com.vcw.falecpv.core.modelo.persistencia.Adquisiciondetalle;
import com.vcw.falecpv.core.modelo.persistencia.Ice;
import com.vcw.falecpv.core.modelo.persistencia.Iva;
import com.vcw.falecpv.core.modelo.persistencia.Pago;
import com.vcw.falecpv.core.modelo.persistencia.Producto;
import com.vcw.falecpv.core.modelo.persistencia.Tipocomprobante;
import com.vcw.falecpv.core.servicio.AdquisicionServicio;
import com.vcw.falecpv.core.servicio.AdquisiciondetalleServicio;
import com.vcw.falecpv.core.servicio.ClienteServicio;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.falecpv.core.servicio.IceServicio;
import com.vcw.falecpv.core.servicio.IvaServicio;
import com.vcw.falecpv.core.servicio.PagoServicio;
import com.vcw.falecpv.core.servicio.TipocomprobanteServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.ctrl.common.MessageCommonCtrl.Message;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@SessionScoped
public class AdquisicionFrmCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1134597750495584673L;
	
	@EJB
	private TipocomprobanteServicio tipocomprobanteServicio;
	@EJB
	private IvaServicio ivaServicio;
	@EJB
	private IceServicio iceServicio;
	@EJB
	private AdquisicionServicio adquisicionServicio;
	@EJB
	private AdquisiciondetalleServicio adquisiciondetalleServicio;
	@EJB
	private PagoServicio pagoServicio;
	@EJB
	private ClienteServicio clienteServicio;
	@EJB
	private EstablecimientoServicio establecimientoServicio;
	
	private Adquisicion adquisicionSelected = new Adquisicion();
	private List<Tipocomprobante> tipocomprobanteList;
	private List<Adquisiciondetalle> adquisiciondetalleList;
	private Adquisiciondetalle adquisiciondetalleSelected;
	private List<Iva> ivaList;
	private List<Ice> iceList;
	private BigDecimal valorTotalPago;
	private String criterioProveedor;	
	private BigDecimal porcentajeRenta;
	private BigDecimal porcentajeIva;
	private BigDecimal totalPago = BigDecimal.ZERO;
	private BigDecimal totalSaldo = BigDecimal.ZERO;
	private List<Pago> pagoList;
	
	/**
	 * 
	 */
	public AdquisicionFrmCtrl() {
	}
	
	@PostConstruct
	private void init() {
		try {
			adquisicionSelected = new Adquisicion();
			adquisicionSelected.setSubtotal(BigDecimal.ZERO);
			adquisicionSelected.setTotaliva(BigDecimal.ZERO);
			adquisicionSelected.setTotaldescuento(BigDecimal.ZERO);
			adquisicionSelected.setTotalretencion(BigDecimal.ZERO);
			adquisicionSelected.setTotalfactura(BigDecimal.ZERO);
			adquisicionSelected.setTotalpagar(BigDecimal.ZERO);
			consultarIva();
			consultarIce();
			populateTipoPago();
			consultarTipoComprobante();
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}

	public void consultarTipoComprobante()throws DaoException{
		setTipocomprobanteList(tipocomprobanteServicio.getTipocomprobanteDao()
				.getByEmpresaFormulario(TipoComprobanteEnum.ADQUICIION));
	}
	
	@Override
	public void refrescar() {
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}

	@Override
	public void nuevo() {
		try {
			nuevaAdquisicion();
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	@Override
	public void guardar() {
		try {
			
			// verifica si la adquisicion estado GEN
			if(adquisicionSelected.getIdadquisicion()!=null) {
				
				String analisisEstado = adquisicionServicio.analizarEstado(adquisicionSelected.getIdadquisicion(),
						adquisicionSelected.getEstablecimiento().getIdestablecimiento(), "GUARDAR");
				
				if(analisisEstado!=null) {
					getMessageCommonCtrl().crearMensaje("Error", 
							analisisEstado, 
							Message.ERROR);
					return;
				}
				
			}
			
			// sino existe proveedor
			if(adquisicionSelected.getCliente()==null) {
				getMessageCommonCtrl().crearMensaje("Error", 
						"No existe Proveedor", 
						Message.ERROR);
				return;
			}
			
			// si ya existe la factura del mismo proveedor
			
			if (adquisicionServicio.getAdquisicionDao().existeFacturaProveedor(establecimientoMain.getIdestablecimiento(), adquisicionSelected.getCliente().getIdcliente(), adquisicionSelected.getIdadquisicion(), adquisicionSelected.getNumfactura())) {
				getMessageCommonCtrl().crearMensaje("Error", 
						"YA EXISTE LA FACTURA :" + adquisicionSelected.getNumfactura() + " DEL PROVEEDOR : " + adquisicionSelected.getCliente().getRazonsocial(), 
						Message.ERROR);
				return;
			}
			
			// sino existe detale
			if(adquisiciondetalleList==null || adquisiciondetalleList.size()==0) {
				getMessageCommonCtrl().crearMensaje("Error", 
						"NO SE PUEDE GUARDAR, NO EXISTE DETALLE DE COMPRA", 
						Message.ERROR);
				return;
			}
			
			// validar el valor
			if(totalPago.doubleValue()<adquisicionSelected.getTotalpagar().doubleValue()) {
				getMessageCommonCtrl().crearMensaje("Error", 
						"VALOR DE PAGO MENOR AL VALOR A PAGAR.", 
						Message.ERROR);
				return;
			}
			
			adquisicionSelected.setUpdated(new Date());
			for (Adquisiciondetalle d : adquisiciondetalleList) {
				d.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
				d.setUpdated(new Date());
			}
			
			adquisicionSelected = adquisicionServicio.guadarFacade(adquisicionSelected, adquisiciondetalleList, pagoList);
			
			// actualiza lista de compras
			AdquisicionMainCtrl adquisicionMainCtrl = (AdquisicionMainCtrl) AppJsfUtil.getManagedBean("adquisicionMainCtrl");
			adquisicionMainCtrl.consultarAdquisiciones();
			
			getMessageCommonCtrl().crearMensaje("Ok", 
					"COMPRA REGISTRADA CORRECTAMENTE.", 
					Message.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}	
	
	public void editarAdquisicion(String idAdquisicion)throws DaoException {
		nuevaAdquisicion();
		adquisicionSelected = adquisicionServicio.consultarByPk(idAdquisicion);
		adquisiciondetalleList = adquisicionServicio.getByAdquisicion(idAdquisicion);
		pagoList = pagoServicio.getPagoDao().getByIdAdquisicion(idAdquisicion);
		totalizarCompra();
	}
	
	public void nuevaAdquisicion() throws DaoException {
		criterioProveedor = null;
		totalPago = BigDecimal.ZERO;
		totalSaldo = BigDecimal.ZERO;
		pagoList = null;
		porcentajeIva = null;
		porcentajeRenta = null;
		adquisiciondetalleList = new ArrayList<>();
		adquisicionSelected = new Adquisicion();
		adquisicionSelected.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
		adquisicionSelected.setEstablecimiento(establecimientoMain);
		adquisicionSelected.setFecha(new Date());
		adquisicionSelected.setSubtotal(BigDecimal.ZERO);
		adquisicionSelected.setTotaliva(BigDecimal.ZERO);
		adquisicionSelected.setTotaldescuento(BigDecimal.ZERO);
		adquisicionSelected.setTotalretencion(BigDecimal.ZERO);
		adquisicionSelected.setTotalfactura(BigDecimal.ZERO);
		adquisicionSelected.setTotalpagar(BigDecimal.ZERO);
		adquisicionSelected.setEstado(ComprobanteEstadoEnum.REGISTRADO.toString());
		adquisicionSelected.setFecha(new Date());
		adquisicionSelected.setEsgasto(0);
		setTipocomprobanteList(tipocomprobanteServicio.getTipocomprobanteDao()
				.getByEmpresaFormulario(TipoComprobanteEnum.ADQUICIION));
		totalizarCompra();
		consultarIva();
		consultarIce();
		consultarTipoComprobante();
	}
	
	public void consultarIva()throws DaoException{
		ivaList = null;
		ivaList = ivaServicio.getIvaDao().getByEstado(EstadoRegistroEnum.ACTIVO,AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa());
	}
	
	public void consultarIce()throws DaoException{
		iceList = iceServicio.getIceDao().getByEstado(EstadoRegistroEnum.ACTIVO	, AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa());
	}
	
	public void agregarProducto(Producto p) {
		
		Adquisiciondetalle ad = new Adquisiciondetalle();
		ad.setCantidad(BigDecimal.valueOf(p.getCantidad()));
		ad.setDescripcion(p.getNombregenerico());
		ad.setDescuento(BigDecimal.ZERO);
		ad.setPorcentajeDescuento(p.getPorcentajedescuento());
		ad.setPrecioUntarioCalculado(BigDecimal.ZERO);
		ad.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
		ad.setIva(p.getIva());
		ad.setIce(p.getIce());
		ad.setPreciounitario(p.getPreciouno());
		ad.setPorcentajeDescuento(ad.getPorcentajeDescuento().divide(BigDecimal.valueOf(100)));
		ad.setCodProducto(p.getCodigoprincipal());
		ad.setProducto(p);
		
	}
	
	private void calcularAdquicisioDetalleProducto(Adquisiciondetalle a,boolean calcDescuento) {
		a.setPreciototalsinimpuesto(a.getCantidad().multiply(a.getPreciounitario()));
		if(!a.isFlagCalcularDetForm() && 
				a.getPorcentajeDescuento()!=null && 
				a.getPorcentajeDescuento().doubleValue()>0.0d && 
				calcDescuento) {
			
			a.setDescuento(a.getPreciototalsinimpuesto().multiply(a.getPorcentajeDescuento().divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.HALF_UP));
			
		}else {
			
			a.setPorcentajeDescuento(BigDecimal.ZERO);
			
		}
		
		a.setPreciototalsinimpuesto(a.getPreciototalsinimpuesto().add(a.getDescuento().negate()).setScale(2, RoundingMode.HALF_UP));
		a.setPrecioUntarioCalculado(a.getPreciounitario());
		// ice
		a.setValorice(a.getPreciototalsinimpuesto().multiply(a.getIce().getValor().divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.HALF_UP));
		// iva
		a.setValoriva(a.getPreciototalsinimpuesto().add(a.getValorice()).multiply(a.getIva().getValor().divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.HALF_UP));
		a.setPreciototal(a.getPreciototalsinimpuesto().add(a.getValorice()).add(a.getValoriva()).setScale(2, RoundingMode.HALF_UP));
		adquisiciondetalleSelected = a;
	}
	
	public void calcularAdquicisioDetalleProductoAction(Adquisiciondetalle a,boolean calcDescuento) {
		try {
			a.setFlagCalcularDetForm(true);
			calcularAdquicisioDetalleProducto(a,calcDescuento);
			limpiarTotales();
			totalizarCompra();
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	public void agregarDetalle() {
		try {
			if(adquisiciondetalleList==null) {
				adquisiciondetalleList = new ArrayList<>();
			}
			if(adquisiciondetalleSelected.getAccion().equals("NUEVO")) {
				adquisiciondetalleList.add(adquisiciondetalleSelected);				
			}
			adquisicionSelected.setValorretenidoiva(BigDecimal.ZERO);
			adquisicionSelected.setValorretenidorenta(BigDecimal.ZERO);
			totalizarCompra();
			boolean flagCodOtroConcepto = false;
			if(adquisiciondetalleSelected.getProducto()!=null) {
				AppJsfUtil.executeJavaScript("PrimeFaces.focus('frmListProducto:intListProBusqueda')");
			}else {
				flagCodOtroConcepto = true;
				AppJsfUtil.executeJavaScript("PrimeFaces.focus('frmListProducto:tvPLmain:fsvAdquisicion:intAdqDescripcion')");
			}
			nuevoDetalle();
			if(flagCodOtroConcepto) {
				adquisiciondetalleSelected.setCodProducto("COD" + (adquisiciondetalleList==null?1:adquisiciondetalleList.size()+1));
			}
			adquisiciondetalleSelected.setAccion("NUEVO");
			limpiarTotales();
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	private void limpiarTotales() {
		adquisicionSelected.setValorretenidoiva(BigDecimal.ZERO);
		adquisicionSelected.setValorretenidorenta(BigDecimal.ZERO);
		totalPago = BigDecimal.ZERO;
		totalSaldo = BigDecimal.ZERO;
		pagoList = null;
		porcentajeIva = BigDecimal.ZERO;
		porcentajeRenta = BigDecimal.ZERO;
	}
	
	
	private void totalizarCompra() {
		
		adquisicionSelected.setSubtotal(BigDecimal.ZERO);
		adquisicionSelected.setTotaliva(BigDecimal.ZERO);
		adquisicionSelected.setTotalice(BigDecimal.ZERO);
		adquisicionSelected.setTotaldescuento(BigDecimal.ZERO);
		adquisicionSelected.setTotalfactura(BigDecimal.ZERO);
		adquisicionSelected.setTotalpagar(BigDecimal.ZERO);
		int fil = 1;
		for (Adquisiciondetalle a : adquisiciondetalleList) {
			if(a.getValoriva()==null) {
				a.setValoriva(BigDecimal.ZERO);
			}
			// totales
			adquisicionSelected.setSubtotal(adquisicionSelected.getSubtotal().add(a.getPreciototalsinimpuesto()));
			adquisicionSelected.setTotaliva(adquisicionSelected.getTotaliva().add(a.getValoriva()));
			adquisicionSelected.setTotalice(adquisicionSelected.getTotalice().add(a.getValorice()));
			adquisicionSelected.setTotaldescuento(adquisicionSelected.getTotaldescuento().add(a.getDescuento()));
			if(a.getIdadquisiciondetalle()==null || a.getIdadquisiciondetalle().contains("MM")) {
				a.setIdadquisiciondetalle("MM" + fil);
			}
			fil++;
			
		}
		adquisicionSelected.setTotalfactura(adquisicionSelected.getSubtotal().add(adquisicionSelected.getTotaliva()).add(adquisicionSelected.getTotalice()) .setScale(2, RoundingMode.HALF_UP));
		// Valor de la retención
		calcularRetencion("RENTA");
		calcularRetencion("IVA");
		adquisicionSelected.setTotalretencion(adquisicionSelected.getValorretenidorenta().add(adquisicionSelected.getValorretenidoiva()).setScale(2, RoundingMode.HALF_UP));
		adquisicionSelected.setTotalpagar(adquisicionSelected.getTotalfactura().add(adquisicionSelected.getTotalretencion().negate()).setScale(2, RoundingMode.HALF_UP));
		totalizarPago();
	}
	
	public void seleccionarProducto(Producto producto) {
		
		try {			
			adquisiciondetalleSelected.setCantidad(BigDecimal.valueOf(1d));
			adquisiciondetalleSelected.setCodProducto(producto.getCodigoprincipal());
			adquisiciondetalleSelected.setDescripcion(producto.getNombre());
			adquisiciondetalleSelected.setDescuento(BigDecimal.ZERO);
			adquisiciondetalleSelected.setPorcentajeDescuento(BigDecimal.ZERO);
			adquisiciondetalleSelected.setPrecioUntarioCalculado(BigDecimal.ZERO);
			adquisiciondetalleSelected.setPreciounitario(BigDecimal.ZERO);
			adquisiciondetalleSelected.setProducto(producto);
			//AppJsfUtil.executeJavaScript("PrimeFaces.focus('formMain:adquisicionDetDT:0:intDetNombreProducto');");
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
		
	}
	
	public void nuevoDetalle() {
		try {
			adquisiciondetalleSelected = new Adquisiciondetalle();
			consultarIce();
			consultarIva();
			adquisiciondetalleSelected.setCantidad(BigDecimal.valueOf(1));
			//adquisiciondetalleSelected.setDescripcion("-");
			adquisiciondetalleSelected.setPreciototalsinimpuesto(BigDecimal.ZERO);
			adquisiciondetalleSelected.setPreciototal(BigDecimal.ZERO);
			adquisiciondetalleSelected.setValoriva(BigDecimal.ZERO);
			adquisiciondetalleSelected.setDescuento(BigDecimal.ZERO);
			adquisiciondetalleSelected.setPorcentajeDescuento(BigDecimal.ZERO);
			adquisiciondetalleSelected.setPrecioUntarioCalculado(BigDecimal.ZERO);
			adquisiciondetalleSelected.setPreciounitario(BigDecimal.ZERO);
			adquisiciondetalleSelected.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
			adquisiciondetalleSelected.setIva(ivaServicio.getIvaDao().getDefecto(AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa()));			
			if(adquisiciondetalleSelected.getIva()==null) {
				getMessageCommonCtrl().crearMensaje("Error", 
						"NO EXISTE IVA POR DEFECTO, CONFIGURACION / IVA : SELECCIONAR POR DEFECTO", 
						Message.ERROR);
				return;
			}			
			adquisiciondetalleSelected.setIce(iceList.stream().filter(x->x.getValor().doubleValue()==0d).findFirst().orElse(null));
			if(adquisiciondetalleSelected.getIce()==null) {
				getMessageCommonCtrl().crearMensaje("Error", 
						"NO EXISTE ICE CON VALOR 0, CONFIGURACION / ICE : CREAR ICE VALOR 0.", 
						Message.ERROR);
				return;
			}			
			adquisiciondetalleSelected.setProducto(null);
			adquisiciondetalleSelected.setValoriva(BigDecimal.ZERO);
			adquisiciondetalleSelected.setValorice(BigDecimal.ZERO);
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	public void eliminarDetalle() {
		try {
			
			if(adquisicionSelected!=null && adquisicionSelected.getIdadquisicion()!=null) {
				Adquisicion a = adquisicionServicio.consultarByPk(adquisicionSelected.getIdadquisicion());
				if(a.getEstado().equals("ANU")) {
					getMessageCommonCtrl().crearMensaje("Error", 
							"NO SE PUEDE ELIMINAR LA COMPRA, ESTA EN ESTADO ANULADO.", 
							Message.ERROR);
					return;
				}
				
				if(a.getEstado().equals("ENV") || a.getEstado().equals("RETENCION")) {
					getMessageCommonCtrl().crearMensaje("Error", 
							"NO SE PUEDE ELIMINAR LA COMPRA, TIENE RETENCION.", 
							Message.ERROR);
					return;
				}
			}
			
			Adquisiciondetalle ad = adquisiciondetalleServicio.consultarByPk(adquisiciondetalleSelected.getIdadquisiciondetalle());
			if(ad!=null) {
				adquisicionServicio.anularDetalle(adquisiciondetalleSelected.getIdadquisiciondetalle(), true, AppJsfUtil.getUsuario().getIdusuario());
			}
			
			adquisiciondetalleList.remove(adquisiciondetalleSelected);
			totalizarCompra();
			// guarda la cabecera
			if(adquisicionSelected.getIdadquisicion()!=null) {
				adquisicionSelected = adquisicionServicio.guadarFacade(adquisicionSelected, adquisiciondetalleList, pagoList);
			}
			// pantalla principal
			AdquisicionMainCtrl adquisicionMainCtrl = (AdquisicionMainCtrl) AppJsfUtil.getManagedBean("adquisicionMainCtrl");
			adquisicionMainCtrl.consultarAdquisiciones();
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	public void buscarProveedor() {
		try {
			
			if(adquisicionSelected==null) return;
			
			if(criterioProveedor==null || criterioProveedor.trim().length()==0) {
				AppJsfUtil.addErrorMessage("formMain:inpCriterioProveedor", "ERROR", "REQUERIDO");
				return;
			}
			
			consultarProveedor(criterioProveedor);
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	public void consultarProveedor(String identificador) throws DaoException {
		adquisicionSelected.setCliente(null);
		adquisicionSelected.setCliente(clienteServicio.getClienteDao().getByIdentificador(identificador,AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa()));
	}
	
	public void calcularRettencionAction(String tipo) {
		try {
			
			if(adquisicionSelected == null) {
				return;
			}
			
			if (adquisicionSelected.getSubtotal().doubleValue()<=0) {
				return;
			}
			
			calcularRetencion(tipo);
			totalizarCompra();
			porcentajeIva = null;
			porcentajeRenta = null;
			totalPago = BigDecimal.ZERO;
			totalSaldo = BigDecimal.ZERO;
			pagoList = null;
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	private void calcularRetencion(String tipo) {
		switch (tipo) {
		case "RENTA":
			if(porcentajeRenta!=null) {
				adquisicionSelected.setValorretenidorenta(
						porcentajeRenta.divide(BigDecimal.valueOf(100d)).multiply(adquisicionSelected.getSubtotal()).setScale(2, RoundingMode.HALF_UP));
			}
			break;
		case "IVA":
			if(porcentajeIva!=null) {
				adquisicionSelected.setValorretenidoiva(
						porcentajeIva.divide(BigDecimal.valueOf(100d)).multiply(adquisicionSelected.getTotaliva()).setScale(2, RoundingMode.HALF_UP));
			}
			break;	
		default:
			break;
		}
	}
	
	public void totalizarRetencion() {
		try {
			if(adquisicionSelected == null) {
				return;
			}
			
			if (adquisicionSelected.getSubtotal().doubleValue()<=0) {
				return;
			}
			
			totalizarCompra();
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
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
		
		totalSaldo = adquisicionSelected.getTotalpagar().add(totalPago.negate()).setScale(2, RoundingMode.HALF_UP);
		if(totalSaldo.doubleValue()<0) {
			totalSaldo = BigDecimal.ZERO;
		}
		
	}
	
	/**
	 * @return the adquisicionSelected
	 */
	public Adquisicion getAdquisicionSelected() {
		return adquisicionSelected;
	}

	/**
	 * @param adquisicionSelected the adquisicionSelected to set
	 */
	public void setAdquisicionSelected(Adquisicion adquisicionSelected) {
		this.adquisicionSelected = adquisicionSelected;
	}

	/**
	 * @return the tipocomprobanteList
	 */
	public List<Tipocomprobante> getTipocomprobanteList() {
		return tipocomprobanteList;
	}

	/**
	 * @param tipocomprobanteList the tipocomprobanteList to set
	 */
	public void setTipocomprobanteList(List<Tipocomprobante> tipocomprobanteList) {
		this.tipocomprobanteList = tipocomprobanteList;
	}

	/**
	 * @return the adquisiciondetalleList
	 */
	public List<Adquisiciondetalle> getAdquisiciondetalleList() {
		return adquisiciondetalleList;
	}

	/**
	 * @param adquisiciondetalleList the adquisiciondetalleList to set
	 */
	public void setAdquisiciondetalleList(List<Adquisiciondetalle> adquisiciondetalleList) {
		this.adquisiciondetalleList = adquisiciondetalleList;
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
	 * @return the adquisiciondetalleSelected
	 */
	public Adquisiciondetalle getAdquisiciondetalleSelected() {
		return adquisiciondetalleSelected;
	}

	/**
	 * @param adquisiciondetalleSelected the adquisiciondetalleSelected to set
	 */
	public void setAdquisiciondetalleSelected(Adquisiciondetalle adquisiciondetalleSelected) {
		this.adquisiciondetalleSelected = adquisiciondetalleSelected;
	}

	/**
	 * @return the valorTotalPago
	 */
	public BigDecimal getValorTotalPago() {
		return valorTotalPago;
	}

	/**
	 * @param valorTotalPago the valorTotalPago to set
	 */
	public void setValorTotalPago(BigDecimal valorTotalPago) {
		this.valorTotalPago = valorTotalPago;
	}

	/**
	 * @return the criterioProveedor
	 */
	public String getCriterioProveedor() {
		return criterioProveedor;
	}

	/**
	 * @param criterioProveedor the criterioProveedor to set
	 */
	public void setCriterioProveedor(String criterioProveedor) {
		this.criterioProveedor = criterioProveedor;
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


}
