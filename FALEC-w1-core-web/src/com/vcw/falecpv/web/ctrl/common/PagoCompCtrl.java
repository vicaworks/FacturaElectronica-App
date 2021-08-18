/**
 * 
 */
package com.vcw.falecpv.web.ctrl.common;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.omnifaces.util.Ajax;
import org.primefaces.PrimeFaces;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.FechaUtil;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.modelo.persistencia.Adquisicion;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Pago;
import com.vcw.falecpv.core.modelo.persistencia.Tipopago;
import com.vcw.falecpv.core.servicio.TipopagoServicio;
import com.vcw.falecpv.web.ctrl.adquisicion.AdquisicionFrmCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@ViewScoped
public class PagoCompCtrl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6522494094259594867L;
	
	@EJB
	private TipopagoServicio tipopagoServicio;

	
	private String callModule;
	private String callForm;
	private String updateView;
	private Cabecera cabeceraSelected;
	private Adquisicion adquisicionSelected;
	private List<Pago> pagoList;
	private List<Tipopago> tipoPagoList;
	private Pago pagoSelected;
	private BigDecimal totalPago = BigDecimal.ZERO;
	private BigDecimal totalSaldo = BigDecimal.ZERO;
	private Tipopago tipopagoSelected;
	
	
	// controlers
	private AdquisicionFrmCtrl adquisicionFrmCtrl;
	
	/**
	 * 
	 */
	public PagoCompCtrl() {
	}

	@PostConstruct
	private void init() {
		
	}
	
	private void consultarTipoPago() throws DaoException{
		tipoPagoList = null;
		tipoPagoList = tipopagoServicio.getALL();
	}
	
	public void loadPantalla() {
		try {
			consultarTipoPago();			
			totalPago = BigDecimal.ZERO;
			totalSaldo = BigDecimal.ZERO;
			switch (callModule) {
			case "ADQUISICION":				
				cabeceraSelected = new Cabecera();
				cabeceraSelected.setCliente(adquisicionSelected.getCliente());
				cabeceraSelected.setTotalsinimpuestos(adquisicionSelected.getSubtotal());
				cabeceraSelected.setTotaldescuento(adquisicionSelected.getTotaldescuento());
				cabeceraSelected.setTotalice(adquisicionSelected.getTotalice());
				cabeceraSelected.setTotaliva(adquisicionSelected.getTotaliva());
				cabeceraSelected.setTotalconimpuestos(adquisicionSelected.getTotalfactura());
				cabeceraSelected.setValorapagar(adquisicionSelected.getTotalpagar());
				cabeceraSelected.setFechaemision(adquisicionSelected.getFecha());
				break;

			default:
				break;
			}
			
			if(pagoList==null) {
				tipopagoSelected = tipoPagoList.get(0);
				aplicarPago();
			}else {
				tipopagoSelected = pagoList.get(pagoList.size()-1).getTipopago();
				tipoPagoList.forEach(x->{
					x.setActivo(false);
					if(x.getIdtipopago().equals(tipopagoSelected.getIdtipopago())) {
						x.setActivo(true);
					}
				});
				totalizarPago();
				Ajax.oncomplete("seleccionarInputLista('.selector-input-monto','.mainPagoDT')");					
			}
			
			AppJsfUtil.showModalRender("dlgListaPago", "frmListPago");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage(callForm, "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void calcularFechaAction(Pago p,String tipo) {
		try {
			switch (tipo) {
			case "PLAZO":
				p.setFechapago(FechaUtil.agregarDias(cabeceraSelected.getFechaemision(), pagoSelected.getPlazo().intValue()));				
				break;
			case "FECHA":	
				p.setPlazo(BigDecimal.valueOf(FechaUtil.getDiasRangoFechas(cabeceraSelected.getFechaemision(), p.getFechapago())));
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmListPago", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
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
			AppJsfUtil.addErrorMessage("frmListPago", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void totalizarPagoaction(Pago p) {
		try {
			pagoSelected = p;
			// tipo efectivo
			if(pagoSelected.getTipopago().getSubdetalle().equals("1")) {
				calcularCambioAction(p);
			}
			totalizarPago();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmListPago", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
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
			AppJsfUtil.addErrorMessage("frmListPago", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		
	}
	
	public void aplicarPago() throws DaoException {
		
		if (cabeceraSelected.getValorapagar().doubleValue()<=0d) {
			return;
		}
		
		if (pagoList==null) {
			pagoList = new ArrayList<>();
		}
		
		Tipopago tp = getTipopagoSelected();
		if(!tipoPagoList.isEmpty()) {
			tipoPagoList.forEach(x->x.setActivo(false));
		}
		tp.setActivo(true);
		pagoSelected = new Pago();
		
		if(callModule.equals("ADQUISICION")) {
			pagoSelected.setAdquisicion(adquisicionSelected);			
		}else {
			pagoSelected.setCabecera(cabeceraSelected);
		}
		pagoSelected.setTipopago(tp);
		pagoSelected.setTotal(adquisicionSelected.getTotalpagar().add(totalPago.negate()).setScale(2, RoundingMode.HALF_UP));
		pagoSelected.setPlazo(BigDecimal.ZERO);
		pagoSelected.setFechapago(new Date());
		pagoSelected.setUnidadtiempo("DIAS");
		
		if(pagoSelected.getTotal().doubleValue()==0 && pagoList.size()==1) {
			pagoSelected.setTotal(pagoList.get(0).getTotal());
			pagoList = new ArrayList<>();
		}else if(pagoSelected.getTotal().doubleValue()==0 ){
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "ERROR", " EL COMPROBANTE ESTA PAGADO AL 100%.");
	        PrimeFaces.current().dialog().showMessageDynamic(message);
			return;
		}		
		
		pagoList.add(pagoSelected);
		totalizarPago();
		
	}
	
	public void realizarPago() {
		try {
			
			if(totalSaldo.longValue()>0) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "ERROR", "   EXISTE UN SALDO PENDIENTE DE : $ " + totalSaldo.doubleValue());
		        PrimeFaces.current().dialog().showMessageDynamic(message);
				return;
			}
			
			// validar campos obligatorios
			for (Pago pago : pagoList) {
				if (pago.getTipopago().getSubdetalle().equals("2") || pago.getTipopago().getSubdetalle().equals("3")) {
					if(pago.getNumerodocumento()==null || pago.getNombrebanco()==null) {
						FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "ERROR", "   LOS CAMPOS NUMERO DE DOCUMENTO, INSTITUCION FINANCIERA SON OBLIGATORIOS.");
				        PrimeFaces.current().dialog().showMessageDynamic(message,true);
						return;
					}
				}
			}
			
			switch (callModule) {
			case "ADQUISICION":
				adquisicionFrmCtrl.setPagoList(pagoList);
				adquisicionFrmCtrl.setTotalPago(totalPago);
				adquisicionFrmCtrl.setTotalSaldo(totalSaldo);
				break;

			default:
				break;
			}
			
			AppJsfUtil.hideModal("dlgListaPago");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmListPago", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
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
	 * @return the callForm
	 */
	public String getCallForm() {
		return callForm;
	}

	/**
	 * @param callForm the callForm to set
	 */
	public void setCallForm(String callForm) {
		this.callForm = callForm;
	}

	/**
	 * @return the updateView
	 */
	public String getUpdateView() {
		return updateView;
	}

	/**
	 * @param updateView the updateView to set
	 */
	public void setUpdateView(String updateView) {
		this.updateView = updateView;
	}

	/**
	 * @return the cabeceraSelected
	 */
	public Cabecera getCabeceraSelected() {
		return cabeceraSelected;
	}

	/**
	 * @param cabeceraSelected the cabeceraSelected to set
	 */
	public void setCabeceraSelected(Cabecera cabeceraSelected) {
		this.cabeceraSelected = cabeceraSelected;
	}

	/**
	 * @return the adquisicionFrmCtrl
	 */
	public AdquisicionFrmCtrl getAdquisicionFrmCtrl() {
		return adquisicionFrmCtrl;
	}

	/**
	 * @param adquisicionFrmCtrl the adquisicionFrmCtrl to set
	 */
	public void setAdquisicionFrmCtrl(AdquisicionFrmCtrl adquisicionFrmCtrl) {
		this.adquisicionFrmCtrl = adquisicionFrmCtrl;
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
	 * @return the tipoPagoList
	 */
	public List<Tipopago> getTipoPagoList() {
		return tipoPagoList;
	}

	/**
	 * @param tipoPagoList the tipoPagoList to set
	 */
	public void setTipoPagoList(List<Tipopago> tipoPagoList) {
		this.tipoPagoList = tipoPagoList;
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
	 * @return the tipopagoSelected
	 */
	public Tipopago getTipopagoSelected() {
		return tipopagoSelected;
	}

	/**
	 * @param tipopagoSelected the tipopagoSelected to set
	 */
	public void setTipopagoSelected(Tipopago tipopagoSelected) {
		this.tipopagoSelected = tipopagoSelected;
	}
	
}
