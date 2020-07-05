/**
 * 
 */
package com.vcw.falecpv.web.ctrl.comprobantes.liqcompra;

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
import com.vcw.falecpv.core.modelo.persistencia.Iva;
import com.vcw.falecpv.core.modelo.persistencia.Pago;
import com.vcw.falecpv.core.modelo.persistencia.Proveedor;
import com.vcw.falecpv.core.modelo.persistencia.Tipocomprobante;
import com.vcw.falecpv.core.modelo.persistencia.Tipopago;
import com.vcw.falecpv.core.modelo.persistencia.Totalimpuesto;
import com.vcw.falecpv.core.servicio.CabeceraServicio;
import com.vcw.falecpv.core.servicio.ContadorPkServicio;
import com.vcw.falecpv.core.servicio.DetalleServicio;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.falecpv.core.servicio.IvaServicio;
import com.vcw.falecpv.core.servicio.LiqCompraServicio;
import com.vcw.falecpv.core.servicio.PagoServicio;
import com.vcw.falecpv.core.servicio.ProveedorServicio;
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
public class LiqCompraFormCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3595569165013603425L;
	
	@EJB
	private IvaServicio ivaServicio;
	
	@EJB
	private LiqCompraServicio liqCompraServicio;

	@EJB
	private TipocomprobanteServicio tipocomprobanteServicio;
	
	@EJB
	private CabeceraServicio cabeceraServicio;
	
	@EJB
	private DetalleServicio detalleServicio;
	
	@EJB
	private ContadorPkServicio contadorPkServicio;
	
	@EJB
	private EstablecimientoServicio establecimientoServicio;
	
	@EJB
	private ProveedorServicio proveedorServicio;
	
	@EJB
	private TipopagoServicio tipopagoServicio;
	
	@EJB
	private PagoServicio pagoServicio;
	
	
	
	private List<Proveedor> proveedorList;
	private List<Tipocomprobante> tipocomprobanteList;
	private Cabecera liqCompraSelected;
	private List<Detalle> liqCompraDetalleList;
	private Detalle detalleSelected;
	private List<Iva> ivaList;
	private BigDecimal totalPago = BigDecimal.ZERO;
	private BigDecimal totalSaldo = BigDecimal.ZERO;
	private List<Pago> pagoList;
	private Pago pagoSelected;
	private String criterioProveedor;
	
	/**
	 * 
	 */
	public LiqCompraFormCtrl() {
	}
	
	
	@PostConstruct
	private void init() {
		try {
			
			nuevaLiqCompra();
			
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void consultarIva()throws DaoException {
		ivaList = ivaServicio.getIvaDao().getByEstado(EstadoRegistroEnum.ACTIVO, AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa());
	}
	
	public void nuevaLiqCompra() throws DaoException{
		
		liqCompraSelected = new Cabecera();
		liqCompraSelected.setEstablecimiento(AppJsfUtil.getEstablecimiento());
		liqCompraSelected.setTotal(BigDecimal.ZERO);
		liqCompraSelected.setTotaldescuento(BigDecimal.ZERO);
		liqCompraSelected.setTotaliva(BigDecimal.ZERO);
		liqCompraSelected.setTotalsinimpuestos(BigDecimal.ZERO);
		liqCompraSelected.setDetalleList(new ArrayList<>());
		liqCompraSelected.setFechaemision(new Date());
		liqCompraDetalleList = null;
		criterioProveedor = null;
		detalleSelected = null;
		pagoList = null;
		pagoSelected = null;
		totalPago = BigDecimal.ZERO;
		totalSaldo = BigDecimal.ZERO;
		
		consultarProveedor();
		consultarIva();
	}

	public void consultarProveedor()throws DaoException {
		
		setProveedorList(proveedorServicio.getProveedorDao().getByConsulta(
				AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa(), EstadoRegistroEnum.ACTIVO, null));
		
	}
	
	@Override
	public void guardar() {
		try {
			
			if(liqCompraSelected.getProveedor()==null) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTE DATOS DEL PROVEEDOR");
				return;
			}
			
			// validar estado
			if(liqCompraSelected.getIdcabecera()!=null) {
				
				String analisisEstado = liqCompraServicio.analizarEstado(liqCompraSelected.getIdcabecera(),liqCompraSelected.getEstablecimiento().getIdestablecimiento() ,"GUARDAR");
				
				if(analisisEstado!=null) {
					AppJsfUtil.addErrorMessage("formMain", "ERROR", analisisEstado);
					return;
				}
				
			}
			
			// validar el valor
			if(totalPago.doubleValue()<liqCompraSelected.getTotalconimpuestos().doubleValue()) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "VALOR DE PAGO MENOR AL VALOR DE LA LIQUIDACION DE COMPRA.");
				return;
			}
			
			populatefactura(GenTipoDocumentoEnum.LIQUIDACION_COMPRA);
			liqCompraSelected.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
			liqCompraSelected.setUpdated(new Date());
			liqCompraSelected.setPagoList(pagoList);			
			liqCompraSelected = cabeceraServicio.guardarComprobanteFacade(liqCompraSelected);
			
			// main principal de la lista de liquidacion de compra 
			LiqCompraCtrl liqCompraCtrl = (LiqCompraCtrl) AppJsfUtil.getManagedBean("liqCompraCtrl");
			liqCompraCtrl.consultar();
			
			AppJsfUtil.addInfoMessage("formMain", "OK", "GUARDADO GENERAR FACTURA");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	private void populatefactura(GenTipoDocumentoEnum genTipoDocumentoEnum) throws DaoException, ParametroRequeridoException {
		
		liqCompraSelected.setTipoemision("1");
		liqCompraSelected.setTipocomprobante(tipocomprobanteServicio.getByTipoDocumento(genTipoDocumentoEnum));
		
		liqCompraSelected.setEstablecimiento(establecimientoServicio.consultarByPk(AppJsfUtil.getEstablecimiento().getIdestablecimiento()));
		liqCompraSelected.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
		determinarPeriodoFiscal();
		liqCompraSelected.setContribuyenteespecial("5368");
		liqCompraSelected.setMoneda("DOLAR");
		if(liqCompraSelected.getSecuencial()==null) {
			liqCompraSelected.setSecuencial(contadorPkServicio.generarNumeroDocumento(genTipoDocumentoEnum,
					AppJsfUtil.getEstablecimiento().getIdestablecimiento()));
			// clave de acceso
			liqCompraSelected.setClaveacceso(ComprobanteHelper.generarAutorizacionFacade(liqCompraSelected, contadorPkServicio.generarContadorTabla(TCAleatorio.ALEATORIOLIQCOMPRA, liqCompraSelected.getEstablecimiento().getIdestablecimiento(),new Object[] {false})));
			liqCompraSelected.setNumdocumento(TextoUtil.leftPadTexto(liqCompraSelected.getEstablecimiento().getCodigoestablecimiento(),3, "0").concat("001").concat(liqCompraSelected.getSecuencial()));
			liqCompraSelected.setNumfactura(liqCompraSelected.getNumdocumento());
		}
		
		liqCompraSelected.setPropina(BigDecimal.ZERO);
		liqCompraSelected.setEstado(ComprobanteEstadoEnum.REGISTRADO.toString());
		liqCompraSelected.setDetalleList(liqCompraDetalleList);
		
		// tabla de total impuesto
		List<Totalimpuesto> totalimpuestoList = new ArrayList<>();
		totalimpuestoList.addAll(ComprobanteHelper.determinarIva(liqCompraSelected.getDetalleList()));
//		totalimpuestoList.addAll(ComprobanteHelper.determinarIce(liqCompraSelected.getDetalleList()));
		liqCompraSelected.setTotalimpuestoList(totalimpuestoList);
		
		// detalle impuesto
		ComprobanteHelper.determinarDetalleImpuesto(liqCompraSelected.getDetalleList());
		
		// infromacion adicional 
		liqCompraSelected.setInfoadicionalList(ComprobanteHelper.determinarInfoAdicional(liqCompraSelected));
		
	}
	
	private void determinarPeriodoFiscal() {
		SimpleDateFormat sf = new SimpleDateFormat("MM/yyyy");
		liqCompraSelected.setPeriodofiscal(sf.format(liqCompraSelected.getFechaemision()));
	}


	@Override
	public void nuevo() {
		try {
			nuevaLiqCompra();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void agregarItem() {
		try {
			
			if(liqCompraDetalleList==null) {
				liqCompraDetalleList = new ArrayList<>();
			}
			
			consultarIva();
			
			detalleSelected = new Detalle();
			detalleSelected.setCantidad(BigDecimal.valueOf(1));
			detalleSelected.setDescuento(BigDecimal.ZERO);
			detalleSelected.setDescripcion("");
			detalleSelected.setPreciounitario(BigDecimal.ZERO);
			detalleSelected.setProducto(null);
			detalleSelected.setValorice(BigDecimal.ZERO);
			detalleSelected.setValoriva(BigDecimal.ZERO);
			detalleSelected.setIva(ivaServicio.getIvaDao().getDefecto(AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa()));
			// valida
			if(detalleSelected.getIva()==null) {
				AppJsfUtil.addErrorMessage("formMain","ERROR","NO EXISTE IVA POR DEFECTO, CONFIGURACION / IVA : SELECCIONAR POR DEFECTO");
				return;
			}
			
			liqCompraDetalleList.add(detalleSelected);
			
			calcularItem(detalleSelected);
			totalizar();
			
			Ajax.oncomplete("PrimeFaces.focus('formMain:pvDetalleDT:" + (liqCompraDetalleList.size()-1) + ":intDetNombreProducto');");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void calcularItemAction(Detalle det) {
		try {
			detalleSelected = det;
			calcularItem(detalleSelected);
			totalizar();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	private void calcularItem(Detalle dFac) {
		
		dFac.setPreciototalsinimpuesto(dFac.getCantidad().multiply(dFac.getPreciounitario()).add(dFac.getDescuento().negate()).setScale(2, RoundingMode.HALF_UP));
		dFac.setValoriva(dFac.getIva().getValor().divide(BigDecimal.valueOf(100))
				.multiply(dFac.getPreciototalsinimpuesto()).setScale(2, RoundingMode.HALF_UP));
		dFac.setPreciototal(dFac.getPreciototalsinimpuesto().add(dFac.getValoriva()).setScale(2, RoundingMode.HALF_UP));
		
	}
	
	private void totalizar() throws DaoException {
		if(liqCompraSelected==null) nuevaLiqCompra();;
		
		
		liqCompraSelected.setTotaliva(BigDecimal.ZERO);
		liqCompraSelected.setTotaldescuento(BigDecimal.ZERO);
		liqCompraSelected.setTotalsinimpuestos(BigDecimal.ZERO);
		liqCompraSelected.setTotalconimpuestos(BigDecimal.ZERO);
		
		int i= 1;
		for (Detalle d : liqCompraDetalleList) {
			
			liqCompraSelected.setTotalsinimpuestos(liqCompraSelected.getTotalsinimpuestos().add(d.getPreciototalsinimpuesto()));
			liqCompraSelected.setTotaliva(liqCompraSelected.getTotaliva().add(d.getValoriva()));
			liqCompraSelected.setTotaldescuento(liqCompraSelected.getTotaldescuento().add(d.getDescuento()));
			
			if(d.getIddetalle()==null || d.getIddetalle().contains("MM")) {
				d.setIddetalle("MM" + i);
			}
			
			i++;
		}
		
		liqCompraSelected.setTotaliva(liqCompraSelected.getTotaliva().setScale(2, RoundingMode.HALF_UP));
		liqCompraSelected.setTotaldescuento(liqCompraSelected.getTotaldescuento().setScale(2, RoundingMode.HALF_UP));
		liqCompraSelected.setTotalsinimpuestos(liqCompraSelected.getTotalsinimpuestos().setScale(2, RoundingMode.HALF_UP));
		liqCompraSelected.setTotalconimpuestos(liqCompraSelected.getTotalsinimpuestos().add(liqCompraSelected.getTotaliva()).add(liqCompraSelected.getTotalice()).setScale(2, RoundingMode.HALF_UP));
		
		if(totalPago!=null && totalPago.doubleValue()>0) {
			totalizarPago();
		}
		
	}
	
	public void eliminarDetalle() {
		try {
			
			// eliminar datos de la base de datos
			
			// eliminar
			
			for (Detalle p : liqCompraDetalleList) {
				if(detalleSelected.getIddetalle().equals(p.getIddetalle())) {
					break;
				}
			}
			
			if(liqCompraSelected.getDetalleEliminarList()==null) {
				liqCompraSelected.setDetalleEliminarList(new ArrayList<>());
			}
			detalleSelected.setIdUsuarioEliminacion(AppJsfUtil.getUsuario().getIdusuario());
			liqCompraSelected.getDetalleEliminarList().add(detalleSelected);
			liqCompraDetalleList.remove(detalleSelected);
			if(liqCompraDetalleList.isEmpty()) {
				detalleSelected=null;
			}else {
				detalleSelected = liqCompraDetalleList.get(liqCompraDetalleList.size()-1);
			}
			
			totalizar();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void agregarPago(TipoPagoFormularioEnum tipoPagoFormularioEnum) {
		try {
			
			aplicarPago(tipoPagoFormularioEnum);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void aplicarPago(TipoPagoFormularioEnum tipoPagoFormularioEnum) throws DaoException {
		if(liqCompraSelected == null) {
			return;
		}
		
		if (liqCompraSelected.getTotalsinimpuestos().doubleValue()<=0) {
			return;
		}
		
		if (pagoList==null) {
			pagoList = new ArrayList<>();
		}
		
		Tipopago tp = tipopagoServicio.getByCodINterno(tipoPagoFormularioEnum);
		
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
			pagoSelected.setCabecera(liqCompraSelected);
			pagoSelected.setTipopago(tp);
			pagoSelected.setTotal(liqCompraSelected.getTotalconimpuestos().add(totalPago.negate()).setScale(2, RoundingMode.HALF_UP));
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
		
		totalSaldo = liqCompraSelected.getTotalconimpuestos().add(totalPago.negate()).setScale(2, RoundingMode.HALF_UP);
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
	
	public void buscarProveedor() {
		try {
			
			if(liqCompraSelected==null) return;
			
			if(criterioProveedor==null || criterioProveedor.trim().length()==0) {
				AppJsfUtil.addErrorMessage("formMain:inpCriterioProveedor", "ERROR", "REQUERIDO");
				return;
			}
			
			consultarProveedor(criterioProveedor);
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void consultarProveedor(String identificador) throws DaoException {
		liqCompraSelected.setProveedor(null);
		liqCompraSelected.setProveedor(proveedorServicio.getProveedorDao().getByIdentificador(identificador,AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa()));
	}
	
	public String editar(String idLiqCompra) throws DaoException {
		
		nuevaLiqCompra();
		liqCompraSelected = cabeceraServicio.consultarByPk(idLiqCompra);
		
		if(liqCompraSelected==null) {
			return "NO EXISTE LA LIQUIDACION DE COMPRA";
		}
		
		liqCompraDetalleList = detalleServicio.getDetalleDao().getByIdCabecera(idLiqCompra);
		pagoList = pagoServicio.getPagoDao().getByIdCabecera(idLiqCompra);
		pagoList.stream().forEach(x->{
			x.setTipoPagoFormularioEnum(TipoPagoFormularioEnum.getByCodInterno(x.getTipopago().getCodinterno()));
		});
		totalizar();
		totalizarPago();
		
		return null;
	}

	/**
	 * @return the proveedorList
	 */
	public List<Proveedor> getProveedorList() {
		return proveedorList;
	}


	/**
	 * @param proveedorList the proveedorList to set
	 */
	public void setProveedorList(List<Proveedor> proveedorList) {
		this.proveedorList = proveedorList;
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
	 * @return the liqCompraSelected
	 */
	public Cabecera getLiqCompraSelected() {
		return liqCompraSelected;
	}


	/**
	 * @param liqCompraSelected the liqCompraSelected to set
	 */
	public void setLiqCompraSelected(Cabecera liqCompraSelected) {
		this.liqCompraSelected = liqCompraSelected;
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
	 * @return the liqCompraDetalleList
	 */
	public List<Detalle> getLiqCompraDetalleList() {
		return liqCompraDetalleList;
	}


	/**
	 * @param liqCompraDetalleList the liqCompraDetalleList to set
	 */
	public void setLiqCompraDetalleList(List<Detalle> liqCompraDetalleList) {
		this.liqCompraDetalleList = liqCompraDetalleList;
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

}