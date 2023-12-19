/**
 * 
 */
package com.vcw.falecpv.web.ctrl.comprobantes.nc;

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
import com.vcw.falecpv.core.constante.contadores.EstadoComprobanteEnum;
import com.vcw.falecpv.core.constante.contadores.TipoComprobanteEnum;
import com.vcw.falecpv.core.constante.parametrosgenericos.PGEmpresaSucursal;
import com.vcw.falecpv.core.exception.ExisteNumDocumentoException;
import com.vcw.falecpv.core.helper.ComprobanteHelper;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Detalle;
import com.vcw.falecpv.core.modelo.persistencia.Ice;
import com.vcw.falecpv.core.modelo.persistencia.Iva;
import com.vcw.falecpv.core.modelo.persistencia.Producto;
import com.vcw.falecpv.core.modelo.persistencia.Tipocomprobante;
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
import com.vcw.falecpv.core.servicio.ParametroGenericoEmpresaServicio;
import com.vcw.falecpv.core.servicio.ParametroGenericoEmpresaServicio.TipoRetornoParametroGenerico;
import com.vcw.falecpv.core.servicio.ProductoServicio;
import com.vcw.falecpv.core.servicio.TipocomprobanteServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.common.RideCtrl;
import com.vcw.falecpv.web.ctrl.common.MessageCommonCtrl.Message;
import com.vcw.falecpv.web.ctrl.facturacion.FacEmitidaCtrl;
import com.vcw.falecpv.web.servicio.SriDispacher;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@SessionScoped
public class NotaCreditoCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4064386825533304320L;
	
	@EJB
	private TipocomprobanteServicio tipocomprobanteServicio;
	
	@EJB
	private CabeceraServicio cabeceraServicio;
	
	@EJB
	private ClienteServicio clienteServicio;
	
	@EJB
	private EstablecimientoServicio establecimientoServicio;
	
	@EJB
	private ContadorPkServicio contadorPkServicio;
	
	@EJB
	private IvaServicio ivaServicio;
	
	@EJB
	private IceServicio iceServicio;
	
	@EJB
	private ProductoServicio productoServicio;
	
	@EJB
	private DetalleServicio detalleServicio;
	
	@EJB
	private InfoadicionalServicio infoadicionalServicio;
	
	@EJB
	private ParametroGenericoEmpresaServicio parametroGenericoEmpresaServicio;
	
	@EJB
	private ConfiguracionServicio configuracionServicio;
	
	@EJB
	private SriDispacher sriDispacher;
	
	private String callModule;
	private String viewUpdate;
	private List<Tipocomprobante> tipocomprobanteList;
	private Cabecera notaCreditoSeleccion;
	private Cabecera facturaSeleccion;
	private CompNcCtrl compNcCtrl;
	private FacEmitidaCtrl facEmitidaCtrl;
	private List<Detalle> detalleNcList;
	private Detalle detalleSelected;
	private List<Iva> ivaList;
	private List<Ice> iceList;
	private String criterioCliente;
	private RideCtrl rideCtrl;
	
	/**
	 * 
	 */
	public NotaCreditoCtrl() {
	}
	
	@PostConstruct
	private void init() {
		try {
			callModule = "NOTACREDITO";
			notaCreditoSeleccion = new Cabecera();
			consultarTipoComprobante();
			consultarIce();
			consultarIva();
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	public void consultarIce()throws DaoException{
		iceList = iceServicio.getIceDao().getByEstado(EstadoRegistroEnum.ACTIVO	, establecimientoMain.getEmpresa().getIdempresa());
	}
	
	public void consultarIva()throws DaoException {
		ivaList = ivaServicio.getIvaDao().getByEstado(EstadoRegistroEnum.ACTIVO, establecimientoMain.getEmpresa().getIdempresa());
	}
	
	public void consultarCliente(String identificador)throws DaoException{
		notaCreditoSeleccion.setCliente(null);
		notaCreditoSeleccion.setCliente(clienteServicio.getClienteDao().getByIdentificador(identificador,
				establecimientoMain.getEmpresa().getIdempresa()));
	}
	public void consultarTipoComprobante()throws DaoException{
		
		setTipocomprobanteList(tipocomprobanteServicio.getTipocomprobanteDao()
				.getByEmpresaFormulario(TipoComprobanteEnum.NOTACREDITO));
	}
	
	@Override
	public void guardar() {
		try {
			
			// validar cliente
			if(notaCreditoSeleccion.getCliente()==null) {
				getMessageCommonCtrl().crearMensaje("Error", 
						"No existe datos del Cliente", 
						Message.ERROR);
				return;
			}
			
			if(notaCreditoSeleccion.getTotalconimpuestos().doubleValue()<=0) {
				getMessageCommonCtrl().crearMensaje("Error", 
						"No existe detalle de productos o servicios", 
						Message.ERROR);
				return;
			}
			
			// validar estado
			if(notaCreditoSeleccion.getIdcabecera()!=null) {
				
				String analisisEstado = cabeceraServicio.analizarEstadoComprobante(notaCreditoSeleccion.getIdcabecera(), "GUARDAR");
				
				if(analisisEstado!=null) {
					getMessageCommonCtrl().crearMensaje("Error", 
							analisisEstado, 
							Message.ERROR);
					return;
				}
				
			}
			
			if(detalleNcList==null || detalleNcList.isEmpty()) {
				getMessageCommonCtrl().crearMensaje("Error", 
						"No existe detalle", 
						Message.ERROR);
				return;
			}
			
			notaCreditoSeleccion.setDetalleList(detalleNcList);
			notaCreditoSeleccion.setGenTipoDocumentoEnum(GenTipoDocumentoEnum.NOTA_CREDITO);
			populateNotaCredito(GenTipoDocumentoEnum.NOTA_CREDITO);
			notaCreditoSeleccion.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
			notaCreditoSeleccion.setUpdated(new Date());
			notaCreditoSeleccion = cabeceraServicio.guardarComprobanteFacade(notaCreditoSeleccion);
			infoadicionalList = notaCreditoSeleccion.getInfoadicionalList();
			noEditarSecuencial(notaCreditoSeleccion);
			notaCreditoSeleccion.setIdUsurioTransaccion(AppJsfUtil.getUsuario().getIdusuario());
			sriDispacher.queue_comprobanteSriDispacher(notaCreditoSeleccion);
			
			FacEmitidaCtrl facEmitidaCtrl = (FacEmitidaCtrl)AppJsfUtil.getManagedBean("facEmitidaCtrl");
			if(facEmitidaCtrl!=null) {
				facEmitidaCtrl.consultar();
			}
			CompNcCtrl compNcCtrl = (CompNcCtrl) AppJsfUtil.getManagedBean("compNcCtrl");
			if(compNcCtrl!=null) {
				compNcCtrl.consultar();
			}
			
			switch (callModule) {
			case "FACTURAS_EMITIDAS":
				break;
			case "NOTACREDITO":
				break;	
			default:
				break;
			}
			
			if(notaCreditoSeleccion.isBorrador()) {
				getMessageCommonCtrl().crearMensaje("Aviso", 
						"Borrador de Nota de crédito generada correctamente.", 
						Message.WARNING);
			}else {
				getMessageCommonCtrl().crearMensaje("Ok", 
						"Comprobanto guardado correctamente.", 
						Message.OK);
			}
			
			
		} catch (ExisteNumDocumentoException e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					e.getMessage(), 
					Message.ERROR);
			notaCreditoSeleccion.setEstado(ComprobanteEstadoEnum.BORRADOR.toString());
			notaCreditoSeleccion.setSecuencial(null);
			notaCreditoSeleccion.setNumdocumento(null);
			notaCreditoSeleccion.setClaveacceso(null);
			notaCreditoSeleccion.setNumeroautorizacion(null);
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
			notaCreditoSeleccion.setEstado(ComprobanteEstadoEnum.BORRADOR.toString());
			notaCreditoSeleccion.setSecuencial(null);
			notaCreditoSeleccion.setNumdocumento(null);
			notaCreditoSeleccion.setClaveacceso(null);
			notaCreditoSeleccion.setNumeroautorizacion(null);
		}
	}
	
	private void populateNotaCredito(GenTipoDocumentoEnum genTipoDocumentoEnum) throws DaoException, ParametroRequeridoException {
		
		notaCreditoSeleccion.setTipoemision("1");
		notaCreditoSeleccion.setTipocomprobante(tipocomprobanteServicio.getByTipoDocumento(genTipoDocumentoEnum));
		
		notaCreditoSeleccion.setEstablecimiento(establecimientoServicio.consultarByPk(establecimientoMain.getIdestablecimiento()));
		notaCreditoSeleccion.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
		if(establecimientoMain.getEmpresa().isRegimenrimpeToBoolean()) {
			notaCreditoSeleccion.setRegimenrimpe(1);
		}
//		notaCreditoSeleccion.setContribuyenteespecial("5368");
		determinarPeriodoFiscal();
		notaCreditoSeleccion.setMoneda("DOLAR");
		notaCreditoSeleccion.setPropina(BigDecimal.ZERO);
		
		if (notaCreditoSeleccion.getIdcabecera() == null || (notaCreditoSeleccion.getIdcabecera() != null && 
																(notaCreditoSeleccion.getEstado().equals(ComprobanteEstadoEnum.BORRADOR.toString()) || 
																 notaCreditoSeleccion.getEstado().equals(ComprobanteEstadoEnum.RECHAZADO_SRI.toString())
																)
															) 
				){
			
			if(notaCreditoSeleccion.isBorrador()) {
				notaCreditoSeleccion.setEstado(ComprobanteEstadoEnum.BORRADOR.toString());
			}else {
				notaCreditoSeleccion.setEstado(ComprobanteEstadoEnum.PENDIENTE.toString());
			}
			
		}
		
		notaCreditoSeleccion.setTipodocasociado(notaCreditoSeleccion.getTipocomprobanteretencion().getIdentificador());
		
		formatoNumDoc(notaCreditoSeleccion.getNumfactura());
		if(notaCreditoSeleccion.getIdcabecerapadre()==null) {
			notaCreditoSeleccion.setValordocasociado(notaCreditoSeleccion.getTotalconimpuestos());
		}
		notaCreditoSeleccion.setImportetotal(notaCreditoSeleccion.getTotalconimpuestos());
		notaCreditoSeleccion.setNumdocasociado(notaCreditoSeleccion.getNumfactura());
		
		// tabla de total impuesto
		List<Totalimpuesto> totalimpuestoList = new ArrayList<>();
		totalimpuestoList.addAll(ComprobanteHelper.determinarIva(notaCreditoSeleccion.getDetalleList()));
		totalimpuestoList.addAll(ComprobanteHelper.determinarIce(notaCreditoSeleccion.getDetalleList()));
		notaCreditoSeleccion.setTotalimpuestoList(totalimpuestoList);
		
		// detalle impuesto
		ComprobanteHelper.determinarDetalleImpuesto(notaCreditoSeleccion.getDetalleList());
		
		// infromacion adicional 
		notaCreditoSeleccion.setInfoadicionalList(infoadicionalList);
		
	}

	@Override
	public void nuevo() {
		try {
			nuevaNotaCredito();
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	public void nuevaNotaCredito()throws DaoException, NumberFormatException, ParametroRequeridoException {
		consultarTipoComprobante();
		consultarIce();
		consultarIva();
		notaCreditoSeleccion = null;
		detalleNcList = null;
		detalleSelected = null;
		criterioCliente = null;
		notaCreditoSeleccion = new Cabecera();
		notaCreditoSeleccion.setEstablecimiento(establecimientoMain);
		notaCreditoSeleccion.setEstado(ComprobanteEstadoEnum.REGISTRADO.toString());
		notaCreditoSeleccion.setFechaemision(new Date());
		notaCreditoSeleccion.setFechaemisiondocasociado(new Date());
		notaCreditoSeleccion.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
		notaCreditoSeleccion.setTotalbaseimponible(BigDecimal.ZERO);
		notaCreditoSeleccion.setTotalconimpuestos(BigDecimal.ZERO);
		notaCreditoSeleccion.setTotaldescuento(BigDecimal.ZERO);
		notaCreditoSeleccion.setTotalice(BigDecimal.ZERO);
		notaCreditoSeleccion.setTotaliva(BigDecimal.ZERO);
		notaCreditoSeleccion.setTotalsinimpuestos(BigDecimal.ZERO);
		infoadicionalList = null;
		descripcionIva = "(0%)";
		inicializarSecuencia(notaCreditoSeleccion);
		
		if(facturaSeleccion!=null) {
			notaCreditoSeleccion.setIdcabecera(facturaSeleccion.getIdcabecera());
			formatoNumDoc(facturaSeleccion.getNumdocumento());
		}
		// estado borrador
		notaCreditoSeleccion.setBorrador(parametroGenericoEmpresaServicio.consultarParametroEstablecimiento(PGEmpresaSucursal.ESTADO_BORRADOR, TipoRetornoParametroGenerico.BOOLEAN, establecimientoMain.getIdestablecimiento()));
		determinarPeriodoFiscal();
		enableAccion = false;
		// infoadicional configuracion
		notaCreditoSeleccion.setTipocomprobante(tipocomprobanteServicio.getByTipoDocumento(GenTipoDocumentoEnum.NOTA_CREDITO));
		notaCreditoSeleccion.setEstablecimiento(establecimientoServicio.consultarByPk(establecimientoMain.getIdestablecimiento()));
		configuracionServicio.populateInformacionAdicional(notaCreditoSeleccion);
		infoadicionalList = notaCreditoSeleccion.getInfoadicionalList();
	}
	
	private void formatoNumDoc(String numDoc) {
		notaCreditoSeleccion.setNumdocasociado(numDoc.substring(0, 3).concat("-")
				.concat(numDoc.substring(3, 6)).concat("-").concat(numDoc.substring(6, numDoc.length())));
	}
	
	private void determinarPeriodoFiscal() {
		SimpleDateFormat sf = new SimpleDateFormat("MM/yyyy");
		notaCreditoSeleccion.setPeriodofiscal(sf.format(notaCreditoSeleccion.getFechaemision()));
	}
	
	public void cambioFechaEmision() {
		try {
			determinarPeriodoFiscal();
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	public void calcularItemAction(boolean calcDescuento,Detalle det) {
		try {
			detalleSelected = det;
			calcularItem(detalleSelected,calcDescuento);
			totalizar();
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
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
		
	}
	
	private void totalizar() throws DaoException, NumberFormatException, ParametroRequeridoException {
		if(notaCreditoSeleccion==null) nuevaNotaCredito();
		
		
		notaCreditoSeleccion.setTotaliva(BigDecimal.ZERO);
		notaCreditoSeleccion.setTotalice(BigDecimal.ZERO);
		notaCreditoSeleccion.setTotaldescuento(BigDecimal.ZERO);
		notaCreditoSeleccion.setTotalsinimpuestos(BigDecimal.ZERO);
		notaCreditoSeleccion.setTotalconimpuestos(BigDecimal.ZERO);
		
		int i= 1;
		for (Detalle d : detalleNcList) {
			
			notaCreditoSeleccion.setTotalsinimpuestos(notaCreditoSeleccion.getTotalsinimpuestos().add(d.getPreciototalsinimpuesto()));
			notaCreditoSeleccion.setTotaliva(notaCreditoSeleccion.getTotaliva().add(d.getValoriva()));
			notaCreditoSeleccion.setTotalice(notaCreditoSeleccion.getTotalice().add(d.getValorice()));
			notaCreditoSeleccion.setTotaldescuento(notaCreditoSeleccion.getTotaldescuento().add(d.getDescuento()));
			
			if(d.getIddetalle()==null || d.getIddetalle().contains("MM")) {
				d.setIddetalle("MM" + i);
			}
			
			i++;
		}
		
		// determinar descripcion iva
		determinarDescripcionIVA(detalleNcList);
		
		notaCreditoSeleccion.setTotaliva(notaCreditoSeleccion.getTotaliva().setScale(2, RoundingMode.HALF_UP));
		notaCreditoSeleccion.setTotalice(notaCreditoSeleccion.getTotalice().setScale(2, RoundingMode.HALF_UP));
		notaCreditoSeleccion.setTotaldescuento(notaCreditoSeleccion.getTotaldescuento().setScale(2, RoundingMode.HALF_UP));
		notaCreditoSeleccion.setTotalsinimpuestos(notaCreditoSeleccion.getTotalsinimpuestos().setScale(2, RoundingMode.HALF_UP));
		notaCreditoSeleccion.setTotalconimpuestos(notaCreditoSeleccion.getTotalsinimpuestos().add(notaCreditoSeleccion.getTotaliva()).add(notaCreditoSeleccion.getTotalice()).setScale(2, RoundingMode.HALF_UP));
		
	}
	
	public void eliminarDetalle() {
		try {
			
			// eliminar datos de la base de datos
			
			// eliminar
			
			for (Detalle p : detalleNcList) {
				if(detalleSelected.getIddetalle().equals(p.getIddetalle())) {
					break;
				}
			}
			
			if(notaCreditoSeleccion.getDetalleEliminarList()==null) {
				notaCreditoSeleccion.setDetalleEliminarList(new ArrayList<>());
			}
			detalleSelected.setIdUsuarioEliminacion(AppJsfUtil.getUsuario().getIdusuario());
			notaCreditoSeleccion.getDetalleEliminarList().add(detalleSelected);
			detalleNcList.remove(detalleSelected);
			if(detalleNcList.isEmpty()) {
				detalleSelected=null;
			}else {
				detalleSelected = detalleNcList.get(detalleNcList.size()-1);
			}
			
			totalizar();
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
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
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
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
	
	public void agregarItem() {
		try {
			
			if(detalleNcList==null) {
				detalleNcList = new ArrayList<>();
			}
			
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
				getMessageCommonCtrl().crearMensaje("Error", 
						"No existe I.V.A. por defecto, ir a configuración / I.V.A. : Seleccionar opor defecto.", 
						Message.ERROR);
				return;
			}
			detalleSelected.setIce(iceList.stream().filter(x->x.getCodigo().equals("-1")).findFirst().orElse(null));
			if(detalleSelected.getIce()==null) {
				getMessageCommonCtrl().crearMensaje("Error", 
						"No existe I.C.E. con valor 0, ir a configuración / ICE : crear I.C.E. valor 0 y código SRI [-1]", 
						Message.ERROR);
				return;
			}
			
			calcularItem(detalleSelected, false);
			
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
			if(detalleNcList==null) {
				detalleNcList = new ArrayList<>();
			}
			if(detalleSelected.getAccion().equals("NUEVO")) {
				detalleNcList.add(detalleSelected);				
			}
			totalizar();
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	
	
	
	public void nuevoByFacturaEmitida(String idFactura)throws DaoException, NumberFormatException, ParametroRequeridoException{
		
		
		nuevaNotaCredito();
		notaCreditoSeleccion = cabeceraServicio.consultarByPk(idFactura);
		notaCreditoSeleccion.setIdcabecera(null);
		notaCreditoSeleccion.setIdcabecerapadre(idFactura);
		notaCreditoSeleccion.setTipocomprobanteretencion(notaCreditoSeleccion.getTipocomprobante());
		formatoNumDoc(notaCreditoSeleccion.getNumdocumento());
		notaCreditoSeleccion.setNumdocumento(null);
		notaCreditoSeleccion.setFechaemisiondocasociado(notaCreditoSeleccion.getFechaemision());
		notaCreditoSeleccion.setFechaemision(new Date());
		determinarPeriodoFiscal();
		notaCreditoSeleccion.setValordocasociado(notaCreditoSeleccion.getTotalconimpuestos());
		notaCreditoSeleccion.setEstado(EstadoComprobanteEnum.REGISTRADO.toString());
		// estado borrador
		notaCreditoSeleccion.setBorrador(parametroGenericoEmpresaServicio.consultarParametroEstablecimiento(PGEmpresaSucursal.ESTADO_BORRADOR, TipoRetornoParametroGenerico.BOOLEAN, establecimientoMain.getIdestablecimiento()));
		notaCreditoSeleccion.setSecuencial(null);
		notaCreditoSeleccion.setTipocomprobante(null);
		inicializarSecuencia(notaCreditoSeleccion);
		detalleNcList = detalleServicio.getDetalleDao().getByIdCabecera(idFactura);
		for (Detalle de : detalleNcList) {
			de.setCabecera(null);
			de.setIddetalle(null);
		}
		if (!detalleNcList.isEmpty()) {
			detalleSelected = detalleNcList.get(0);
		}
		totalizar();
		
	}
	
	public void buscarNumDocumento() {
		try {
			
			Cabecera c = cabeceraServicio.getCabeceraDao().getByTipoComprobante(establecimientoMain.getIdestablecimiento(), notaCreditoSeleccion.getTipocomprobanteretencion() ,notaCreditoSeleccion.getNumfactura());
			if(c!=null) {
				nuevoByFacturaEmitida(c.getIdcabecera());
			}else {
				getMessageCommonCtrl().crearMensaje("Error", 
						"No existe comprobante.", 
						Message.ERROR);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	public void editarSecuencialAction() {
		try {
			
			editarSecuencial(notaCreditoSeleccion, "formMain:intSecDocumento");
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	public void noEditarSecuencialAction() {
		try {
			
			noEditarSecuencial(notaCreditoSeleccion);
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	public void buscarCliente() {
		try {
			
			if(notaCreditoSeleccion==null) return;
			
			if(criterioCliente==null || criterioCliente.trim().length()==0) {
				AppJsfUtil.addErrorMessage("formMain:inpCriterioCliente", "ERROR", "Requerido");
				return;
			}
			
			consultarCliente(criterioCliente);
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	public String editar(String idNotaCredito) throws DaoException, NumberFormatException, ParametroRequeridoException {
		
		nuevaNotaCredito();
		notaCreditoSeleccion = cabeceraServicio.consultarByPk(idNotaCredito);
		
		if(idNotaCredito==null) {
			return "No existe registro seleccionado";
		}
		
	 	detalleNcList = detalleServicio.getDetalleDao().getByIdCabecera(idNotaCredito);
	 	infoadicionalList = infoadicionalServicio.getInfoadicionalDao().getByIdCabecera(idNotaCredito);
		totalizar();
		enableAccion=false;
		habilitarCrud(notaCreditoSeleccion.getEstado());
		if (notaCreditoSeleccion.getEstado().equals(ComprobanteEstadoEnum.BORRADOR.toString())) {
			notaCreditoSeleccion.setBorrador(true);
		}		
		return null;
	}
	
	public void imprimir() {
		// verifica si solo debe de imprimir
		if(notaCreditoSeleccion.getIdcabecera()!=null) {
			if(cabeceraServicio.getCabeceraDao().isImprimir(notaCreditoSeleccion.getIdcabecera())){
				showRide();
				return;
			}
		}
		// pone el nuevo estado
		boolean estadoTemp = notaCreditoSeleccion.isBorrador();
		notaCreditoSeleccion.setBorrador(false);
		guardar();
		AppJsfUtil.ajaxUpdate("formMain");
		if(AppJsfUtil.existErrors()) {
			notaCreditoSeleccion.setBorrador(estadoTemp);
			return;
		}
		showRide();
	}
	
	private void showRide() {
		// despliega el comprobante
		rideCtrl.setIdCabecera(notaCreditoSeleccion.getIdcabecera());
		rideCtrl.setInicialComprobante("FAC-");
		rideCtrl.setNumComprobante(ComprobanteHelper.formatNumDocumento(notaCreditoSeleccion.getNumdocumento()));
		rideCtrl.showRide();
		
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
	 * @return the viewUpdate
	 */
	public String getViewUpdate() {
		return viewUpdate;
	}

	/**
	 * @param viewUpdate the viewUpdate to set
	 */
	public void setViewUpdate(String viewUpdate) {
		this.viewUpdate = viewUpdate;
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
	 * @return the notaCreditoSeleccion
	 */
	public Cabecera getNotaCreditoSeleccion() {
		return notaCreditoSeleccion;
	}

	/**
	 * @param notaCreditoSeleccion the notaCreditoSeleccion to set
	 */
	public void setNotaCreditoSeleccion(Cabecera notaCreditoSeleccion) {
		this.notaCreditoSeleccion = notaCreditoSeleccion;
	}

	/**
	 * @return the compNcCtrl
	 */
	public CompNcCtrl getCompNcCtrl() {
		return compNcCtrl;
	}

	/**
	 * @param compNcCtrl the compNcCtrl to set
	 */
	public void setCompNcCtrl(CompNcCtrl compNcCtrl) {
		this.compNcCtrl = compNcCtrl;
	}

	/**
	 * @return the facEmitidaCtrl
	 */
	public FacEmitidaCtrl getFacEmitidaCtrl() {
		return facEmitidaCtrl;
	}

	/**
	 * @param facEmitidaCtrl the facEmitidaCtrl to set
	 */
	public void setFacEmitidaCtrl(FacEmitidaCtrl facEmitidaCtrl) {
		this.facEmitidaCtrl = facEmitidaCtrl;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the facturaSeleccion
	 */
	public Cabecera getFacturaSeleccion() {
		return facturaSeleccion;
	}

	/**
	 * @param facturaSeleccion the facturaSeleccion to set
	 */
	public void setFacturaSeleccion(Cabecera facturaSeleccion) {
		this.facturaSeleccion = facturaSeleccion;
	}

	/**
	 * @return the detalleNcList
	 */
	public List<Detalle> getDetalleNcList() {
		return detalleNcList;
	}

	/**
	 * @param detalleNcList the detalleNcList to set
	 */
	public void setDetalleNcList(List<Detalle> detalleNcList) {
		this.detalleNcList = detalleNcList;
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
