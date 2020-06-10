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

import org.omnifaces.util.Ajax;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.TextoUtil;
import com.servitec.common.util.exceptions.ParametroRequeridoException;
import com.vcw.falecpv.core.constante.ComprobanteEstadoEnum;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;
import com.vcw.falecpv.core.constante.contadores.TCAleatorio;
import com.vcw.falecpv.core.constante.contadores.TipoComprobanteEnum;
import com.vcw.falecpv.core.helper.ComprobanteHelper;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Cliente;
import com.vcw.falecpv.core.modelo.persistencia.Detalle;
import com.vcw.falecpv.core.modelo.persistencia.Ice;
import com.vcw.falecpv.core.modelo.persistencia.Iva;
import com.vcw.falecpv.core.modelo.persistencia.Producto;
import com.vcw.falecpv.core.modelo.persistencia.Tipocomprobante;
import com.vcw.falecpv.core.modelo.persistencia.Totalimpuesto;
import com.vcw.falecpv.core.servicio.CabeceraServicio;
import com.vcw.falecpv.core.servicio.ClienteServicio;
import com.vcw.falecpv.core.servicio.ContadorPkServicio;
import com.vcw.falecpv.core.servicio.DetalleServicio;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.falecpv.core.servicio.IceServicio;
import com.vcw.falecpv.core.servicio.IvaServicio;
import com.vcw.falecpv.core.servicio.ProductoServicio;
import com.vcw.falecpv.core.servicio.TipocomprobanteServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.ctrl.facturacion.FacEmitidaCtrl;
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
	
	
	private String callModule;
	private String viewUpdate;
	private List<Cliente> clienteList;
	private List<Tipocomprobante> tipocomprobanteList;
	private Cabecera notaCreditoSeleccion;
	private Cabecera facturaSeleccion;
	private CompNcCtrl compNcCtrl;
	private FacEmitidaCtrl facEmitidaCtrl;
	private List<Detalle> detalleNcList;
	private Detalle detalleSelected;
	private Producto productoSelected;
	private List<Iva> ivaList;
	private List<Ice> iceList;
	private String criterioBusqueda;
	
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
			consultarCliente();
			consultarTipoComprobante();
			consultarIce();
			consultarIva();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void consultarIce()throws DaoException{
		iceList = iceServicio.getIceDao().getByEstado(EstadoRegistroEnum.ACTIVO	, AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa());
	}
	
	public void consultarIva()throws DaoException {
		ivaList = ivaServicio.getIvaDao().getByEstado(EstadoRegistroEnum.ACTIVO, AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa());
	}
	
	public void consultarCliente()throws DaoException{
		clienteList = null;
		clienteList = clienteServicio.getClienteDao().getByEstado(EstadoRegistroEnum.ACTIVO, AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa());
	}
	public void consultarTipoComprobante()throws DaoException{
		
		setTipocomprobanteList(tipocomprobanteServicio.getTipocomprobanteDao()
				.getByEmpresaFormulario(AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa(),
						TipoComprobanteEnum.NOTACREDITO));
	}
	
	@Override
	public void guardar() {
		try {
			
//			notaCreditoSeleccion.setIdcabecera(null);
			
			// validar estado
			if(notaCreditoSeleccion.getIdcabecera()!=null) {
				
				String analisisEstado = cabeceraServicio.analizarNotaCredito(notaCreditoSeleccion.getIdcabecera(), "GUARDAR");
				
				if(analisisEstado!=null) {
					AppJsfUtil.addErrorMessage("formMain", "ERROR", analisisEstado);
					return;
				}
				
				AppJsfUtil.addInfoMessage("formMain", "OK", "TODO: IMPRIMIR");
				return;
				
			}
			
			notaCreditoSeleccion.setDetalleList(detalleNcList);
			populatefactura(GenTipoDocumentoEnum.NOTA_CREDITO);
			notaCreditoSeleccion.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
			notaCreditoSeleccion.setUpdated(new Date());
			notaCreditoSeleccion = cabeceraServicio.guardarComprobanteFacade(notaCreditoSeleccion);
			if(callModule.equals("FACTURAS_EMITIDAS")) {
				FacEmitidaCtrl facEmitidaCtrl = (FacEmitidaCtrl)AppJsfUtil.getManagedBean("facEmitidaCtrl");
				facEmitidaCtrl.consultar();
			}
			
			AppJsfUtil.addInfoMessage("formMain", "OK", "TODO: GUARDADO IMPRIMIR");
			
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	private void populatefactura(GenTipoDocumentoEnum genTipoDocumentoEnum) throws DaoException, ParametroRequeridoException {
		
		notaCreditoSeleccion.setTipoemision("1");
		notaCreditoSeleccion.setTipocomprobante(tipocomprobanteServicio.getByTipoDocumento(genTipoDocumentoEnum,
				AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa()));
		
		notaCreditoSeleccion.setEstablecimiento(establecimientoServicio.consultarByPk(AppJsfUtil.getEstablecimiento().getIdestablecimiento()));
		notaCreditoSeleccion.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
		notaCreditoSeleccion.setContribuyenteespecial("5368");
		determinarPeriodoFiscal();
		notaCreditoSeleccion.setMoneda("DOLAR");
		if(notaCreditoSeleccion.getSecuencial()==null) {
			notaCreditoSeleccion.setSecuencial(contadorPkServicio.generarNumeroDocumento(genTipoDocumentoEnum,
					AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa()));
			// clave de acceso
			notaCreditoSeleccion.setClaveacceso(ComprobanteHelper.generarAutorizacionFacade(notaCreditoSeleccion, contadorPkServicio.generarContadorTabla(TCAleatorio.ALEATORIONOTACREDITO, notaCreditoSeleccion.getEstablecimiento().getIdestablecimiento(),new Object[] {false})));
			notaCreditoSeleccion.setNumdocumento(TextoUtil.leftPadTexto(notaCreditoSeleccion.getEstablecimiento().getCodigoestablecimiento(),3, "0").concat("001").concat(notaCreditoSeleccion.getSecuencial()));
		}
		
		notaCreditoSeleccion.setPropina(BigDecimal.ZERO);
		notaCreditoSeleccion.setEstado(ComprobanteEstadoEnum.REGISTRADO.toString());
		
		formatoNumDoc(notaCreditoSeleccion.getNumfactura());
		if(notaCreditoSeleccion.getIdcabecerapadre()==null) {
			notaCreditoSeleccion.setValordocasociado(notaCreditoSeleccion.getTotalconimpuestos());
		}
		notaCreditoSeleccion.setImportetotal(notaCreditoSeleccion.getTotalconimpuestos());
		
		
		// tabla de total impuesto
		List<Totalimpuesto> totalimpuestoList = new ArrayList<>();
		totalimpuestoList.addAll(ComprobanteHelper.determinarIva(notaCreditoSeleccion.getDetalleList()));
		totalimpuestoList.addAll(ComprobanteHelper.determinarIce(notaCreditoSeleccion.getDetalleList()));
		notaCreditoSeleccion.setTotalimpuestoList(totalimpuestoList);
		
		// detalle impuesto
		ComprobanteHelper.determinarDetalleImpuesto(notaCreditoSeleccion.getDetalleList());
		
		// infromacion adicional 
		notaCreditoSeleccion.setInfoadicionalList(ComprobanteHelper.determinarInfoAdicional(notaCreditoSeleccion));
		
	}

	@Override
	public void nuevo() {
		try {
			nuevaNotaCredito();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void nuevaNotaCredito()throws DaoException {
		consultarTipoComprobante();
		consultarCliente();
		consultarIce();
		consultarIva();
		notaCreditoSeleccion = null;
		detalleNcList = null;
		detalleSelected = null;
		
		notaCreditoSeleccion = new Cabecera();
		notaCreditoSeleccion.setEstablecimiento(AppJsfUtil.getEstablecimiento());
		notaCreditoSeleccion.setEstado(ComprobanteEstadoEnum.REGISTRADO.toString());
		notaCreditoSeleccion.setFechaemision(new Date());
		notaCreditoSeleccion.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
		notaCreditoSeleccion.setTotalbaseimponible(BigDecimal.ZERO);
		notaCreditoSeleccion.setTotalconimpuestos(BigDecimal.ZERO);
		notaCreditoSeleccion.setTotaldescuento(BigDecimal.ZERO);
		notaCreditoSeleccion.setTotalice(BigDecimal.ZERO);
		notaCreditoSeleccion.setTotaliva(BigDecimal.ZERO);
		notaCreditoSeleccion.setTotalsinimpuestos(BigDecimal.ZERO);
		
		if(facturaSeleccion!=null) {
			notaCreditoSeleccion.setIdcabecera(facturaSeleccion.getIdcabecera());
			formatoNumDoc(facturaSeleccion.getNumdocumento());
		}
		
		determinarPeriodoFiscal();
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
	
	public void agregarProducto() {
		try {
			
			if(detalleNcList==null) {
				detalleNcList = new ArrayList<>();
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
				detalleNcList.add(detalleSelected);
			}
			
			totalizar();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	private Detalle existeProductoLista() {
		for (Detalle d : detalleNcList) {
			if(d.getProducto()!=null && d.getProducto().getIdproducto().equals(productoSelected.getIdproducto())) {
				return d;
			}
		}
		
		return null;
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
			detalleSelected.setDescripcion("");
			detalleSelected.setPreciounitario(BigDecimal.ZERO);
			detalleSelected.setProducto(null);
			detalleSelected.setIva(ivaServicio.getIvaDao().getDefecto(AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa()));
			detalleSelected.setIce(iceList.stream().filter(x->x.getValor().doubleValue()==0d).findFirst().get());
			detalleNcList.add(detalleSelected);
			
			calcularItem(detalleSelected, false);
			totalizar();
			Ajax.oncomplete("PrimeFaces.focus('formMain:pvDetalleDT:" + (detalleNcList.size()-1) + ":intDetNombreProducto');");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	
	
	
	public void nuevoByFacturaEmitida(String idFactura)throws DaoException{
		
		
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
		notaCreditoSeleccion.setEstado(ComprobanteEstadoEnum.REGISTRADO.toString());
		notaCreditoSeleccion.setSecuencial(null);
		notaCreditoSeleccion.setTipocomprobante(null);
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
			
			Cabecera c = cabeceraServicio.getCabeceraDao().getByTipoComprobante(AppJsfUtil.getEstablecimiento().getIdestablecimiento(), notaCreditoSeleccion.getTipocomprobanteretencion() ,notaCreditoSeleccion.getNumfactura());
			if(c!=null) {
				nuevoByFacturaEmitida(c.getIdcabecera());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
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
	 * @return the clienteList
	 */
	public List<Cliente> getClienteList() {
		return clienteList;
	}

	/**
	 * @param clienteList the clienteList to set
	 */
	public void setClienteList(List<Cliente> clienteList) {
		this.clienteList = clienteList;
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

}