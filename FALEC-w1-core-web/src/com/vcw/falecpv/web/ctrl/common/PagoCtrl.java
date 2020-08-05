/**
 * 
 */
package com.vcw.falecpv.web.ctrl.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.modelo.persistencia.Pago;
import com.vcw.falecpv.core.modelo.query.VentasQuery;
import com.vcw.falecpv.core.servicio.PagoServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
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
	
	private List<Pago> pagoList;
	private List<Pago> pagoOtrosList;
	private VentasQuery ventasQuery;
	private String idComprobante;
	private BigDecimal valorPagar = BigDecimal.ZERO;
	private BigDecimal totalPago = BigDecimal.ZERO;
	private String callModule;
	private BigDecimal totalOtrosPago = BigDecimal.ZERO;
	private BigDecimal totalAdeudado = BigDecimal.ZERO;
	private BigDecimal totalAbono = BigDecimal.ZERO;
	
	/**
	 * 
	 */
	public PagoCtrl() {
	}
	
	public void cargarPagos() {
		try {
			
			pagoList = null;
			pagoList = pagoServicio.getPagoDao().getByIdCabecera(ventasQuery.getIdcabecera());
			valorPagar = ventasQuery.getValorapagar();
			totalPago = ventasQuery.getTotalpago();
			
			AppJsfUtil.showModalRender("dlgResumenPagos", "frmResPagos");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void cargarPagosById() {
		try {
			
			pagoList = null;
			pagoList = pagoServicio.getPagoDao().getByIdCabecera(idComprobante);
			totalPago = BigDecimal.valueOf(pagoList.stream().mapToDouble(x->x.getTotal().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP);
			AppJsfUtil.showModalRender("dlgResumenPagos", "frmResPagos");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void cargarPagosAdquisicionById() {
		try {
			
			pagoList = null;
			pagoList = pagoServicio.getPagoDao().getByIdAdquisicion(idComprobante);
			totalPago = BigDecimal.valueOf(pagoList.stream().mapToDouble(x->x.getTotal().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP);
			AppJsfUtil.showModalRender("dlgResumenPagos", "frmResPagos");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void cargarPagosForm() {
		try {
			pagoList = null;
			switch (callModule) {
			case "CUENTAS_COBRAR":
				pagoList = pagoServicio.getPagoDao().getByIdCabecera(idComprobante);
				break;
			case "CUENTAS_PAGAR":
				pagoList = pagoServicio.getPagoDao().getByIdAdquisicion(idComprobante);
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
			AppJsfUtil.showModalRender("dlgPagosForm", "formPagos");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	private void totalizar() {
		
		totalPago = BigDecimal.ZERO;
		totalOtrosPago = BigDecimal.ZERO;
		totalAdeudado = BigDecimal.ZERO;
		totalAbono = BigDecimal.ZERO;
		
		if(!pagoOtrosList.isEmpty()) {
			totalOtrosPago = BigDecimal.valueOf(pagoOtrosList.stream().mapToDouble(p->p.getTotal().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP);
		}
		
		totalAbono = BigDecimal.valueOf(pagoList.stream().mapToDouble(p->p.getValorpago().doubleValue()).sum()).setScale(2, RoundingMode.HALF_UP);
		totalAdeudado = BigDecimal.valueOf(pagoList.stream().mapToDouble(p->p.getTotal().doubleValue()).sum() - totalAbono.doubleValue()).setScale(2, RoundingMode.HALF_UP);
		totalPago = totalPago.add(totalOtrosPago).add(totalAdeudado).add(totalAbono).setScale(2, RoundingMode.HALF_UP);
		
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

}
