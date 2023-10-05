/**
 * 
 */
package com.vcw.falecpv.web.ctrl.common;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.commons.beanutils.BeanUtils;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.FechaUtil;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.modelo.persistencia.Pago;
import com.vcw.falecpv.core.modelo.query.VentasQuery;
import com.vcw.falecpv.core.servicio.AdquisicionServicio;
import com.vcw.falecpv.core.servicio.CabeceraServicio;
import com.vcw.falecpv.core.servicio.PagoServicio;
import com.vcw.falecpv.core.servicio.TipopagoServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.ctrl.common.MessageCommonCtrl.Message;
import com.vcw.falecpv.web.ctrl.pagos.CuentaCobrarCtrl;
import com.vcw.falecpv.web.ctrl.pagos.CuentaPagarCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@ViewScoped
public class PagoCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1954399330489712126L;

	@EJB
	private PagoServicio pagoServicio;
	
	@EJB
	private TipopagoServicio tipopagoServicio;
	
	@EJB
	private CabeceraServicio cabeceraServicio;
	
	@EJB
	private AdquisicionServicio adquisicionServicio;
	
	private List<Pago> pagoList;
	private List<Pago> pagoOtrosList;
	private List<Pago> pagoEliminarList;
	private Pago pagoSelected;
	private VentasQuery ventasQuery;
	private String idComprobante;
	private BigDecimal valorPagar = BigDecimal.ZERO;
	private BigDecimal totalPago = BigDecimal.ZERO;
	private String callModule;
	private BigDecimal totalOtrosPago = BigDecimal.ZERO;
	private BigDecimal totalAdeudado = BigDecimal.ZERO;
	private BigDecimal totalAbono = BigDecimal.ZERO;
	private CuentaCobrarCtrl cuentaCobrarCtrl;
	private CuentaPagarCtrl cuentaPagarCtrl;
	private String idTipoComprobante;
	
	/**
	 * 
	 */
	public PagoCtrl() {
	}
	
	public void cargarPagos() {
		try {
			
			pagoList = null;
			pagoList = pagoServicio.getPagoDao().getByIdCabecera(ventasQuery.getIdcabecera());
			ordenar();
			valorPagar = ventasQuery.getValorapagar();
			totalPago = ventasQuery.getTotalpago();
			
			AppJsfUtil.showModalRender("dlgResumenPagos", "frmResPagos");
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	private void ordenar() {
		if(pagoList!=null && !pagoList.isEmpty()) {
			for (Pago p : pagoList) {
				if(!p.getTipopago().getIdtipopago().equals("6")) {
					Calendar cl = Calendar.getInstance();
					cl.set(2000, Calendar.JANUARY, 1);
					p.setFechapago(cl.getTime());
				}
			}
			
			pagoList.sort(Comparator.comparing(Pago::getFechapago));
		}
	}
	
	public void cargarPagosById() {
		try {
			
			pagoList = null;
			pagoList = pagoServicio.getPagoDao().getByIdCabecera(idComprobante);
			ordenar();
			totalPago = BigDecimal.valueOf(pagoList.stream().mapToDouble(x->x.getTotal().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP);
			AppJsfUtil.showModalRender("dlgResumenPagos", "frmResPagos");
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	public void cargarPagosAdquisicionById() {
		try {
			
			pagoList = null;
			pagoList = pagoServicio.getPagoDao().getByIdAdquisicion(idComprobante);
			ordenar();
			totalPago = BigDecimal.valueOf(pagoList.stream().mapToDouble(x->x.getTotal().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP);
			AppJsfUtil.showModalRender("dlgResumenPagos", "frmResPagos");
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	public void cargarPagosForm() {
		try {
			consultarPagosCredito();
			AppJsfUtil.showModalRender("dlgPagosForm", "formPagos");
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	public void refrescarPagosForm() {
		try {
			consultarPagosCredito();
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	public void consultarPagosCredito() throws DaoException {
		pagoList = null;
		pagoEliminarList = null;
		pagoSelected = null;
		switch (callModule) {
		case "CUENTAS_COBRAR":
			pagoList = pagoServicio.getPagoDao().getByIdCabecera(idComprobante);
			break;
		case "CUENTAS_PAGAR":
			if(idTipoComprobante.equals("C")) {
				pagoList = pagoServicio.getPagoDao().getByIdAdquisicion(idComprobante);
			}else {
				pagoList = pagoServicio.getPagoDao().getByIdCabecera(idComprobante);
			}
			break;	
		default:
			break;
		}
		
		// consultar otros pagos
		pagoOtrosList = null;
		pagoOtrosList = pagoList.stream().filter(x->!x.getTipopago().getIdtipopago().equals("6")).collect(Collectors.toList());
		// consultar solo creditos
		pagoList = pagoList.stream().filter(x->x.getTipopago().getIdtipopago().equals("6")).collect(Collectors.toList());
		// ordenar
		pagoList.sort(Comparator.comparing(Pago::getFechapago));
		totalizar();
	}
	
	public void eliminarCredito() {
		try {
			eliminarPago();
			totalizar();
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	private void eliminarPago() throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
		if(pagoEliminarList==null) {
			pagoEliminarList = new ArrayList<>();
		}
		pagoEliminarList.add((Pago)BeanUtils.cloneBean(pagoSelected));
		pagoList.remove(pagoSelected);
		pagoSelected = null;
	}
	
	private void totalizar() {
		
		totalPago = BigDecimal.ZERO;
		totalOtrosPago = BigDecimal.ZERO;
		totalAdeudado = BigDecimal.ZERO;
		totalAbono = BigDecimal.ZERO;
		
		if(!pagoOtrosList.isEmpty()) {
			totalOtrosPago = BigDecimal.valueOf(pagoOtrosList.stream().mapToDouble(p->p.getTotal().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP);
		}
		int i=1;
		for (Pago p : pagoList) {
			if(p.getIdpago()==null || p.getIdpago().contains("M")) {
				p.setIdpago("M"+i);
			}
			i++;
		}
		
		pagoList.stream().forEach(p->{
			if(p.getValorpago().doubleValue()>p.getTotal().doubleValue()) {
				p.setValorpago(p.getTotal());
			}
		});
		
		
		totalAbono = BigDecimal.valueOf(pagoList.stream().mapToDouble(p->p.getValorpago().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP);
		totalAdeudado = BigDecimal.valueOf(pagoList.stream().mapToDouble(p->p.getTotal().doubleValue()).sum() - totalAbono.doubleValue()).setScale(2, RoundingMode.HALF_UP);
		totalPago = totalPago.add(totalOtrosPago).add(totalAdeudado).add(totalAbono).setScale(2, RoundingMode.HALF_UP);
		
	}
	
	public void aplicarAbonoAction() {
		try {
			totalizar();
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	public void aceptarPago() {
		try {
			if(pagoSelected.getIdpago()==null) {
				pagoSelected.setIdpago("M");
				pagoList.add(pagoSelected);
			}
			pagoList.sort(Comparator.comparing(Pago::getFechapago));
			totalizar();
			AppJsfUtil.hideModal("dlgFormPago");
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	public void nuevoPago() {
		try {
			pagoSelected = new Pago();
			pagoSelected.setTipopago(tipopagoServicio.getTipopagoDao().cargar("6"));
			pagoSelected.setFechapago(new Date());
			if(valorPagar.doubleValue() - totalPago.doubleValue()>0) {
				pagoSelected.setTotal(BigDecimal.valueOf(valorPagar.doubleValue() - totalPago.doubleValue()).setScale(2, RoundingMode.HALF_UP));
			}
			
			switch (callModule) {
			case "CUENTAS_COBRAR":
				pagoSelected.setCabecera(cabeceraServicio.consultarByPk(idComprobante));
				break;
			case "CUENTAS_PAGAR":
				pagoSelected.setAdquisicion(adquisicionServicio.consultarByPk(idComprobante));
				break;
			default:
				break;
			}
			AppJsfUtil.showModalRender("dlgFormPago", "formPagoRegistro");
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	public boolean isPagoVencimiento(Pago p) {
		try {
			if(FechaUtil.comparaFechas(p.getFechapago(), new Date())<0 && p.getValorpago().doubleValue()!=p.getTotal().doubleValue()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @return the pagoList
	 */
	public List<Pago> getPagoList() {
		return pagoList;
	}

	@Override
	public void guardar() {
		try {
			
			if(valorPagar.doubleValue() != totalPago.doubleValue()) {
				getMessageCommonCtrl().crearMensaje("Error", 
						"La sumatoria de otros pagos, adeudado, abonado no cuadra con el valor total a pagar del comprobante", 
						Message.ERROR);
				return;
			}
			
			pagoList = pagoServicio.guardarPago(pagoList,pagoEliminarList);
			// pantall aprincipal
			switch (callModule) {
			case "CUENTAS_COBRAR":
				cuentaCobrarCtrl.consultar();
				cuentaCobrarCtrl.totalizar();
				break;
			case "CUENTAS_PAGAR":
				cuentaPagarCtrl.consultar();
				cuentaPagarCtrl.totalizar();
				break;	
			default:
				break;
			}
			AppJsfUtil.addInfoMessage("formPagos", "OK", "REGISTROS GUARDADOS CORRECTAMENTE.");
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}

	/**
	 * @param pagoList the pagoList to set
	 */
	public void setPagoList(List<Pago> pagoList) {
		this.pagoList = pagoList;
	}

	/**
	 * @return the pagoServicio
	 */
	public PagoServicio getPagoServicio() {
		return pagoServicio;
	}

	/**
	 * @param pagoServicio the pagoServicio to set
	 */
	public void setPagoServicio(PagoServicio pagoServicio) {
		this.pagoServicio = pagoServicio;
	}

	/**
	 * @return the ventasQuery
	 */
	public VentasQuery getVentasQuery() {
		return ventasQuery;
	}

	/**
	 * @param ventasQuery the ventasQuery to set
	 */
	public void setVentasQuery(VentasQuery ventasQuery) {
		this.ventasQuery = ventasQuery;
	}

	/**
	 * @return the idComprobante
	 */
	public String getIdComprobante() {
		return idComprobante;
	}

	/**
	 * @param idComprobante the idComprobante to set
	 */
	public void setIdComprobante(String idComprobante) {
		this.idComprobante = idComprobante;
	}

	/**
	 * @return the valorPagar
	 */
	public BigDecimal getValorPagar() {
		return valorPagar;
	}

	/**
	 * @param valorPagar the valorPagar to set
	 */
	public void setValorPagar(BigDecimal valorPagar) {
		this.valorPagar = valorPagar;
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
	 * @return the pagoOtrosList
	 */
	public List<Pago> getPagoOtrosList() {
		return pagoOtrosList;
	}

	/**
	 * @param pagoOtrosList the pagoOtrosList to set
	 */
	public void setPagoOtrosList(List<Pago> pagoOtrosList) {
		this.pagoOtrosList = pagoOtrosList;
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
	 * @return the totalOtrosPago
	 */
	public BigDecimal getTotalOtrosPago() {
		return totalOtrosPago;
	}

	/**
	 * @param totalOtrosPago the totalOtrosPago to set
	 */
	public void setTotalOtrosPago(BigDecimal totalOtrosPago) {
		this.totalOtrosPago = totalOtrosPago;
	}

	/**
	 * @return the totalAdeudado
	 */
	public BigDecimal getTotalAdeudado() {
		return totalAdeudado;
	}

	/**
	 * @param totalAdeudado the totalAdeudado to set
	 */
	public void setTotalAdeudado(BigDecimal totalAdeudado) {
		this.totalAdeudado = totalAdeudado;
	}

	/**
	 * @return the totalAbono
	 */
	public BigDecimal getTotalAbono() {
		return totalAbono;
	}

	/**
	 * @param totalAbono the totalAbono to set
	 */
	public void setTotalAbono(BigDecimal totalAbono) {
		this.totalAbono = totalAbono;
	}

	/**
	 * @return the pagoEliminarList
	 */
	public List<Pago> getPagoEliminarList() {
		return pagoEliminarList;
	}

	/**
	 * @param pagoEliminarList the pagoEliminarList to set
	 */
	public void setPagoEliminarList(List<Pago> pagoEliminarList) {
		this.pagoEliminarList = pagoEliminarList;
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
	 * @return the cuentaCobrarCtrl
	 */
	public CuentaCobrarCtrl getCuentaCobrarCtrl() {
		return cuentaCobrarCtrl;
	}

	/**
	 * @param cuentaCobrarCtrl the cuentaCobrarCtrl to set
	 */
	public void setCuentaCobrarCtrl(CuentaCobrarCtrl cuentaCobrarCtrl) {
		this.cuentaCobrarCtrl = cuentaCobrarCtrl;
	}

	/**
	 * @return the cuentaPagarCtrl
	 */
	public CuentaPagarCtrl getCuentaPagarCtrl() {
		return cuentaPagarCtrl;
	}

	/**
	 * @param cuentaPagarCtrl the cuentaPagarCtrl to set
	 */
	public void setCuentaPagarCtrl(CuentaPagarCtrl cuentaPagarCtrl) {
		this.cuentaPagarCtrl = cuentaPagarCtrl;
	}

	/**
	 * @return the idTipoComprobante
	 */
	public String getIdTipoComprobante() {
		return idTipoComprobante;
	}

	/**
	 * @param idTipoComprobante the idTipoComprobante to set
	 */
	public void setIdTipoComprobante(String idTipoComprobante) {
		this.idTipoComprobante = idTipoComprobante;
	}

}
