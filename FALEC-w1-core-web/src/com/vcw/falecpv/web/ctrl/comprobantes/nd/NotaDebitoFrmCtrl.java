/**
 * 
 */
package com.vcw.falecpv.web.ctrl.comprobantes.nd;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.constante.TipoPagoFormularioEnum;
import com.vcw.falecpv.core.constante.contadores.TipoComprobanteEnum;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Detalle;
import com.vcw.falecpv.core.modelo.persistencia.Iva;
import com.vcw.falecpv.core.modelo.persistencia.Pago;
import com.vcw.falecpv.core.modelo.persistencia.Tipocomprobante;
import com.vcw.falecpv.core.modelo.persistencia.Tipopago;
import com.vcw.falecpv.core.servicio.CabeceraServicio;
import com.vcw.falecpv.core.servicio.ClienteServicio;
import com.vcw.falecpv.core.servicio.ContadorPkServicio;
import com.vcw.falecpv.core.servicio.DetalleServicio;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.falecpv.core.servicio.IvaServicio;
import com.vcw.falecpv.core.servicio.NotaDebitoServicio;
import com.vcw.falecpv.core.servicio.PagoServicio;
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
public class NotaDebitoFrmCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5271954796795645087L;
	
	@EJB
	private NotaDebitoServicio notaDebitoServicio;
	
	@EJB
	private IvaServicio ivaServicio;
	
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
	private PagoServicio pagoServicio;
	
	@EJB
	private ClienteServicio clienteServicio;
	
	@EJB
	private TipopagoServicio tipopagoServicio;
	
	private List<Iva> ivaList;
	private BigDecimal totalPago = BigDecimal.ZERO;
	private BigDecimal totalSaldo = BigDecimal.ZERO;
	private List<Pago> pagoList;
	private Pago pagoSelected;
	private String criterioCliente;
	private List<Tipocomprobante> tipocomprobanteList;
	private Cabecera notDebitoSelected;
	private List<Detalle> notDebitoDetalleList;
	private Detalle detalleSelected;

	/**
	 * 
	 */
	public NotaDebitoFrmCtrl() {
	}
	
	@PostConstruct
	private void init() {
		try {
			
			nuevaNotDebito();
			
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void consultarIva()throws DaoException {
		ivaList = ivaServicio.getIvaDao().getByEstado(EstadoRegistroEnum.ACTIVO, AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa());
	}
	
	public void consultarTipoComprobante() throws DaoException {
		tipocomprobanteList = null;
		tipocomprobanteList = tipocomprobanteServicio.getTipocomprobanteDao().getByEmpresaFormulario(TipoComprobanteEnum.NOTA_DEBITO);
	}
	
	public void nuevaNotDebito() throws DaoException{
		
		notDebitoSelected = new Cabecera();
		notDebitoSelected.setEstablecimiento(AppJsfUtil.getEstablecimiento());
		notDebitoSelected.setTotal(BigDecimal.ZERO);
		notDebitoSelected.setTotaldescuento(BigDecimal.ZERO);
		notDebitoSelected.setTotaliva(BigDecimal.ZERO);
		notDebitoSelected.setTotalice(BigDecimal.ZERO);
		notDebitoSelected.setTotalsinimpuestos(BigDecimal.ZERO);
		notDebitoSelected.setTotalconimpuestos(BigDecimal.ZERO);
		notDebitoSelected.setDetalleList(new ArrayList<>());
		notDebitoSelected.setFechaemision(new Date());
		notDebitoDetalleList = null;
		criterioCliente = null;
		detalleSelected = null;
		pagoList = null;
		pagoSelected = null;
		totalPago = BigDecimal.ZERO;
		totalSaldo = BigDecimal.ZERO;
		
		consultarTipoComprobante();
		consultarIva();
	}
	
	public void buscarCliente() {
		try {
			
			if(notDebitoSelected==null) return;
			
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
	
	public void consultarCliente(String identificador) throws DaoException {
		notDebitoSelected.setCliente(null);
		notDebitoSelected.setCliente(clienteServicio.getClienteDao().getByIdentificador(identificador,
				AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa()));
	}
	
	@Override
	public void nuevo() {
		try {
			
			nuevaNotDebito();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void agregarItem() {
		try {
			
			if(notDebitoDetalleList==null) {
				notDebitoDetalleList = new ArrayList<>();
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
			detalleSelected.setIva(ivaServicio.getIvaDao().getByValor(AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa(),BigDecimal.ZERO));
			// valida
			if(detalleSelected.getIva()==null) {
				AppJsfUtil.addErrorMessage("formMain","ERROR","NO EXISTE IVA CON VALOR CERO");
				return;
			}
			
			notDebitoDetalleList.add(detalleSelected);
			
			calcularItem(detalleSelected);
			totalizar();
			
			Ajax.oncomplete("PrimeFaces.focus('formMain:pvDetalleDT:" + (notDebitoDetalleList.size()-1) + ":intDetNombreProducto');");
			
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
		if(notDebitoSelected==null) nuevaNotDebito();		
		
		notDebitoSelected.setTotaliva(BigDecimal.ZERO);
		notDebitoSelected.setTotaldescuento(BigDecimal.ZERO);
		notDebitoSelected.setTotalsinimpuestos(BigDecimal.ZERO);
		notDebitoSelected.setTotalconimpuestos(BigDecimal.ZERO);
		
		int i= 1;
		for (Detalle d : notDebitoDetalleList) {
			
			notDebitoSelected.setTotalsinimpuestos(notDebitoSelected.getTotalsinimpuestos().add(d.getPreciototalsinimpuesto()));
			notDebitoSelected.setTotaliva(notDebitoSelected.getTotaliva().add(d.getValoriva()));
			notDebitoSelected.setTotaldescuento(notDebitoSelected.getTotaldescuento().add(d.getDescuento()));
			
			if(d.getIddetalle()==null || d.getIddetalle().contains("MM")) {
				d.setIddetalle("MM" + i);
			}
			
			i++;
		}
		
		notDebitoSelected.setTotaliva(notDebitoSelected.getTotaliva().setScale(2, RoundingMode.HALF_UP));
		notDebitoSelected.setTotaldescuento(notDebitoSelected.getTotaldescuento().setScale(2, RoundingMode.HALF_UP));
		notDebitoSelected.setTotalsinimpuestos(notDebitoSelected.getTotalsinimpuestos().setScale(2, RoundingMode.HALF_UP));
		notDebitoSelected.setTotalconimpuestos(notDebitoSelected.getTotalsinimpuestos().add(notDebitoSelected.getTotaliva()).add(notDebitoSelected.getTotalice()).setScale(2, RoundingMode.HALF_UP));
		
		if(totalPago!=null && totalPago.doubleValue()>0) {
			totalizarPago();
		}
		
	}
	
	public void eliminarDetalle() {
		try {
			
			// eliminar datos de la base de datos
			
			// eliminar
			
			for (Detalle p : notDebitoDetalleList) {
				if(detalleSelected.getIddetalle().equals(p.getIddetalle())) {
					break;
				}
			}
			
			if(notDebitoSelected.getDetalleEliminarList()==null) {
				notDebitoSelected.setDetalleEliminarList(new ArrayList<>());
			}
			detalleSelected.setIdUsuarioEliminacion(AppJsfUtil.getUsuario().getIdusuario());
			notDebitoSelected.getDetalleEliminarList().add(detalleSelected);
			notDebitoDetalleList.remove(detalleSelected);
			if(notDebitoDetalleList.isEmpty()) {
				detalleSelected=null;
			}else {
				detalleSelected = notDebitoDetalleList.get(notDebitoDetalleList.size()-1);
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
		if(notDebitoSelected == null) {
			return;
		}
		
		if (notDebitoSelected.getTotalsinimpuestos().doubleValue()<=0) {
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
			pagoSelected.setCabecera(notDebitoSelected);
			pagoSelected.setTipopago(tp);
			pagoSelected.setTotal(notDebitoSelected.getTotalconimpuestos().add(totalPago.negate()).setScale(2, RoundingMode.HALF_UP));
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
		
		totalSaldo = notDebitoSelected.getTotalconimpuestos().add(totalPago.negate()).setScale(2, RoundingMode.HALF_UP);
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
	
	public String editar(String idNotDebito) throws DaoException {
		
		nuevaNotDebito();
		notDebitoSelected = cabeceraServicio.consultarByPk(idNotDebito);
		
		if(notDebitoSelected==null) {
			return "NO EXISTE LA NOTA DEBITO";
		}
		
		notDebitoDetalleList = detalleServicio.getDetalleDao().getByIdCabecera(idNotDebito);
		pagoList = pagoServicio.getPagoDao().getByIdCabecera(idNotDebito);
		pagoList.stream().forEach(x->{
			x.setTipoPagoFormularioEnum(TipoPagoFormularioEnum.getByCodInterno(x.getTipopago().getCodinterno()));
		});
		totalizar();
		totalizarPago();
		
		return null;
	}
	
	@Override
	public void guardar() {
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
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
	 * @return the notDebitoSelected
	 */
	public Cabecera getNotDebitoSelected() {
		return notDebitoSelected;
	}

	/**
	 * @param notDebitoSelected the notDebitoSelected to set
	 */
	public void setNotDebitoSelected(Cabecera notDebitoSelected) {
		this.notDebitoSelected = notDebitoSelected;
	}

	/**
	 * @return the notDebitoDetalleList
	 */
	public List<Detalle> getNotDebitoDetalleList() {
		return notDebitoDetalleList;
	}

	/**
	 * @param notDebitoDetalleList the notDebitoDetalleList to set
	 */
	public void setNotDebitoDetalleList(List<Detalle> notDebitoDetalleList) {
		this.notDebitoDetalleList = notDebitoDetalleList;
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

}
