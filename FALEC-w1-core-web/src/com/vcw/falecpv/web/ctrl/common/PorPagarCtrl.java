/**
 * 
 */
package com.vcw.falecpv.web.ctrl.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.modelo.persistencia.Adquisicion;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Pago;
import com.vcw.falecpv.core.servicio.AdquisicionServicio;
import com.vcw.falecpv.core.servicio.CabeceraServicio;
import com.vcw.falecpv.core.servicio.PagoServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.ctrl.pagos.CuentaCobrarCtrl;
import com.vcw.falecpv.web.ctrl.pagos.CuentaPagarCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@ViewScoped
public class PorPagarCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1925056514940556482L;
	
	
	@EJB
	private CabeceraServicio cabeceraServicio;
	
	@EJB
	private PagoServicio pagoServicio;
	
	@EJB
	private AdquisicionServicio adquisicionServicio;
	
	private Cabecera cabeceraSelected;
	private String idCabeceraSelected;
	private String callModule;
	private String callForm;
	private String viewUpdate;
	private PagoCtrl pagoCtrl;
	private CuentaCobrarCtrl cuentaCobrarCtrl;
	private CuentaPagarCtrl cuentaPagarCtrl;
	private Adquisicion adquisicionSelected;
	private String idAdquisicionSelected;

	/**
	 * 
	 */
	public PorPagarCtrl() {
	}
	
	@PostConstruct
	private void init() {
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	private void consultar(String idCabecera) throws DaoException {
		cabeceraSelected = cabeceraServicio.consultarByPk(idCabecera);
	}
	
	public void cargar() {
		try {
			
			switch (callModule) {
			case "MAIN_POR_COBRAR":
				
				if(idCabeceraSelected!=null) {
					consultar(idCabeceraSelected);
				}
					
				break;
			
			case "MAIN_POR_PAGAR":
				
				adquisicionSelected = adquisicionServicio.consultarByPk(idAdquisicionSelected);
				
				break;	
			default:
				break;
			}
			
			
			AppJsfUtil.showModalRender("dlgPorPagar", "frmPorPagar");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage(callForm, "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void calcular() {
		try {
			
			if(cabeceraSelected.getValorretenidoiva().doubleValue()<0 || cabeceraSelected.getValorretenidoiva().doubleValue()>cabeceraSelected.getTotaliva().doubleValue()) {
				cabeceraSelected.setValorretenidoiva(BigDecimal.ZERO);
				AppJsfUtil.addErrorMessage("frmPorPagar:intValRetIva", "ERROR", "NO PUEDE SER MAYOR AL VALOR DEL IVA, O MENOR A 0.");
			}
			
			if(cabeceraSelected.getValorretenidorenta().doubleValue()<0 || cabeceraSelected.getValorretenidorenta().doubleValue()>cabeceraSelected.getTotalsinimpuestos().doubleValue()) {
				cabeceraSelected.setValorretenidorenta(BigDecimal.ZERO);
				AppJsfUtil.addErrorMessage("frmPorPagar:intValRetRenta", "ERROR", "NO PUEDE SER MAYOR MENOR AL VALOR TOTAl SIN IMPUESTOS, O MENOR A 0.");
			}
			
			cabeceraSelected.setValorapagar(
					cabeceraSelected.getTotalconimpuestos().add(cabeceraSelected.getValorretenidoiva().negate())
							.add(cabeceraSelected.getValorretenidorenta().negate()).setScale(2, RoundingMode.HALF_UP));
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmPorPagar", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void calcularAdquisicion() {
		try {
			
			if(adquisicionSelected.getValorretenidoiva().doubleValue()<0 || adquisicionSelected.getValorretenidoiva().doubleValue()>adquisicionSelected.getTotaliva().doubleValue()) {
				adquisicionSelected.setValorretenidoiva(BigDecimal.ZERO);
				AppJsfUtil.addErrorMessage("frmPorPagar:intValRetIva", "ERROR", "NO PUEDE SER MAYOR AL VALOR DEL IVA, O MENOR A 0.");
			}
			
			if(adquisicionSelected.getValorretenidorenta().doubleValue()<0 || adquisicionSelected.getValorretenidorenta().doubleValue()>adquisicionSelected.getSubtotal().doubleValue()) {
				adquisicionSelected.setValorretenidorenta(BigDecimal.ZERO);
				AppJsfUtil.addErrorMessage("frmPorPagar:intValRetRenta", "ERROR", "NO PUEDE SER MAYOR MENOR AL VALOR TOTAl SIN IMPUESTOS, O MENOR A 0.");
			}
			
			adquisicionSelected.setTotalpagar(
					adquisicionSelected.getTotalfactura().add(adquisicionSelected.getValorretenidoiva().negate())
							.add(adquisicionSelected.getValorretenidorenta().negate()).setScale(2, RoundingMode.HALF_UP));
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmPorPagar", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	@Override
	public void buscar() {
		try {
			cabeceraSelected = null;
			consultar(idCabeceraSelected);
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmPorPagar", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	@Override
	public void guardar() {
		try {
			
			List<Pago> pagoList = null;
			Pago p = null;
			switch (callModule) {
			case "MAIN_POR_COBRAR" :
				cabeceraServicio.actualizar(cabeceraSelected);
				pagoList = pagoServicio.getPagoDao().getByIdCabecera(cabeceraSelected.getIdcabecera());
				// como es credito solo dejar un valor de pago
				pagoServicio.getPagoDao().eliminarByCabecera(cabeceraSelected.getIdcabecera());
				// todos los valores 
				p = pagoList.get(0);
				p.setNotas("");
				p.setTotal(cabeceraSelected.getValorapagar());
				break;
			case "MAIN_POR_PAGAR" :
				adquisicionServicio.actualizar(adquisicionSelected);
				pagoList = pagoServicio.getPagoDao().getByIdAdquisicion(adquisicionSelected.getIdadquisicion());
				// como es credito solo dejar un valor de pago
				pagoServicio.getPagoDao().eliminarByAdquisicion(adquisicionSelected.getIdadquisicion());
				// todos los valores 
				p = pagoList.get(0);
				p.setNotas("");
				p.setTotal(adquisicionSelected.getTotalpagar());
				break;
			default:
				break;
			}
			
			
			pagoServicio.crear(p);
			if(cuentaCobrarCtrl!=null) {
				cuentaCobrarCtrl.consultar();
				AppJsfUtil.updateComponente("formMain");
			}
			
			if(cuentaPagarCtrl!=null) {
				cuentaPagarCtrl.consultar();
				AppJsfUtil.updateComponente("formMain");
			}
			
			if(pagoCtrl!=null) {
				if(cabeceraSelected!=null) {
					pagoCtrl.setValorPagar(cabeceraSelected.getValorapagar());
				}
				if(adquisicionSelected!=null) {
					pagoCtrl.setValorPagar(adquisicionSelected.getTotalpagar());
				}
				pagoCtrl.consultarPagosCredito();
			}
			
			AppJsfUtil.hideModal("dlgPorPagar");
			
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmPorPagar", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	/**
	 * @return the cabeceraServicio
	 */
	public CabeceraServicio getCabeceraServicio() {
		return cabeceraServicio;
	}

	/**
	 * @param cabeceraServicio the cabeceraServicio to set
	 */
	public void setCabeceraServicio(CabeceraServicio cabeceraServicio) {
		this.cabeceraServicio = cabeceraServicio;
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
	 * @return the idCabeceraSelected
	 */
	public String getIdCabeceraSelected() {
		return idCabeceraSelected;
	}

	/**
	 * @param idCabeceraSelected the idCabeceraSelected to set
	 */
	public void setIdCabeceraSelected(String idCabeceraSelected) {
		this.idCabeceraSelected = idCabeceraSelected;
	}

	/**
	 * @return the pagoCtrl
	 */
	public PagoCtrl getPagoCtrl() {
		return pagoCtrl;
	}

	/**
	 * @param pagoCtrl the pagoCtrl to set
	 */
	public void setPagoCtrl(PagoCtrl pagoCtrl) {
		this.pagoCtrl = pagoCtrl;
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
	 * @return the idAdquisicionSelected
	 */
	public String getIdAdquisicionSelected() {
		return idAdquisicionSelected;
	}

	/**
	 * @param idAdquisicionSelected the idAdquisicionSelected to set
	 */
	public void setIdAdquisicionSelected(String idAdquisicionSelected) {
		this.idAdquisicionSelected = idAdquisicionSelected;
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

}
