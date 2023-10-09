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
	private BigDecimal porcentajeRetRenta;
	private BigDecimal porcentajeRetIva;

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
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	private void consultar(String idCabecera) throws DaoException {
		cabeceraSelected = cabeceraServicio.consultarByPk(idCabecera);
	}
	
	public void cargar() {
		try {
			
			porcentajeRetIva = BigDecimal.ZERO;
			porcentajeRetRenta = BigDecimal.ZERO;
			
			switch (callModule) {
			case "MAIN_POR_COBRAR":
				
				if(idCabeceraSelected!=null) {
					consultar(idCabeceraSelected);
					calcular();
				}
					
				break;
			
			case "MAIN_POR_PAGAR":
				
				adquisicionSelected = adquisicionServicio.consultarByPk(idAdquisicionSelected);
				calcularAdquisicion();
				
				break;	
			default:
				break;
			}
			
			
			AppJsfUtil.showModalRender("dlgPorPagar", "frmPorPagar");
			
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	public void calcular() {
		try {
			
			if(cabeceraSelected.getValorretenidoiva().doubleValue()<0 || cabeceraSelected.getValorretenidoiva().doubleValue()>cabeceraSelected.getTotaliva().doubleValue()) {
				cabeceraSelected.setValorretenidoiva(BigDecimal.ZERO);
				AppJsfUtil.addErrorMessage("frmPorPagar", "Error", "No puede ser mayor al valor del iva, o menor a 0.");
				flagError = true;
				return;
			}
			
			if(cabeceraSelected.getValorretenidorenta().doubleValue()<0 || cabeceraSelected.getValorretenidorenta().doubleValue()>cabeceraSelected.getTotalsinimpuestos().doubleValue()) {
				cabeceraSelected.setValorretenidorenta(BigDecimal.ZERO);
				AppJsfUtil.addErrorMessage("frmPorPagar", "Error", "No puede ser mayor menor al valor total sin impuestos, o menor a 0.");
				flagError = true;
				return;
			}
			cabeceraSelected.setValorretenido(cabeceraSelected.getValorretenidoiva().add(cabeceraSelected.getValorretenidorenta()));
			cabeceraSelected.setValorapagar(
					cabeceraSelected.getTotalconimpuestos().add(cabeceraSelected.getValorretenidoiva().negate())
							.add(cabeceraSelected.getValorretenidorenta().negate()).setScale(2, RoundingMode.HALF_UP));
			
			cabeceraSelected.setOtrosPagos(pagoServicio.getPagoDao().getByCabeceraOtrosPagos(cabeceraSelected.getIdcabecera()));
			
			cabeceraSelected.setTotalCredito(
					cabeceraSelected.getValorapagar().add(cabeceraSelected.getOtrosPagos().negate())
						.setScale(2, RoundingMode.HALF_UP)
					);
			
			flagError = false;
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	public void calcularAdquisicion() {
		try {
			
			if(adquisicionSelected.getValorretenidoiva().doubleValue()<0 || adquisicionSelected.getValorretenidoiva().doubleValue()>adquisicionSelected.getTotaliva().doubleValue()) {
				adquisicionSelected.setValorretenidoiva(BigDecimal.ZERO);
				AppJsfUtil.addErrorMessage("frmPorPagar", "Error", "No puede ser mayor al valor del iva, o menor a 0.");
				flagError = true;
				return;
			}
			
			if(adquisicionSelected.getValorretenidorenta().doubleValue()<0 || adquisicionSelected.getValorretenidorenta().doubleValue()>adquisicionSelected.getSubtotal().doubleValue()) {
				adquisicionSelected.setValorretenidorenta(BigDecimal.ZERO);
				AppJsfUtil.addErrorMessage("frmPorPagar", "Error", "No puede ser mayor menor al valor total sin impuestos, o menor a 0.");
				flagError = true;
				return;
			}
			
			adquisicionSelected.setTotalretencion(adquisicionSelected.getValorretenidoiva().add(adquisicionSelected.getValorretenidorenta()));
			adquisicionSelected.setTotalpagar(
					adquisicionSelected.getTotalfactura().add(adquisicionSelected.getValorretenidoiva().negate())
							.add(adquisicionSelected.getValorretenidorenta().negate()).setScale(2, RoundingMode.HALF_UP));
			adquisicionSelected.setOtrosPagos(
					pagoServicio.getPagoDao().getByIdAdquisiscionOtrospagos(adquisicionSelected.getIdadquisicion())
					);
			adquisicionSelected.setTotalCredito(
					adquisicionSelected.getTotalpagar().add(adquisicionSelected.getOtrosPagos().negate())
						.setScale(2, RoundingMode.HALF_UP)
					);
			flagError = false;
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	@Override
	public void buscar() {
		try {
			cabeceraSelected = null;
			consultar(idCabeceraSelected);
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
			
			List<Pago> pagoList = null;
			Pago p = null;
			switch (callModule) {
			case "MAIN_POR_COBRAR" :
				calcular();
				if(flagError) {
					return;
				}
				cabeceraServicio.actualizar(cabeceraSelected);
				pagoList = pagoServicio.getPagoDao().getByIdCabecera(cabeceraSelected.getIdcabecera(), "6");
				// como es credito solo dejar un valor de pago
				pagoServicio.getPagoDao().eliminarByCabecera(cabeceraSelected.getIdcabecera(), "6");
				// todos los valores 
				p = pagoList.get(0);
				p.setNotas("");
				p.setTotal(cabeceraSelected.getTotalCredito());
				break;
			case "MAIN_POR_PAGAR" :
				calcularAdquisicion();
				if(flagError) {
					return;
				}
				adquisicionServicio.actualizar(adquisicionSelected);
				pagoList = pagoServicio.getPagoDao().getByIdAdquisicion(adquisicionSelected.getIdadquisicion(),"6");
				// como es credito solo dejar un valor de pago
				pagoServicio.getPagoDao().eliminarByAdquisicion(adquisicionSelected.getIdadquisicion(), "6");
				// todos los valores 
				p = pagoList.get(0);
				p.setNotas("");
				p.setTotal(adquisicionSelected.getTotalCredito());
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
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
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

	/**
	 * @return the porcentajeRetRenta
	 */
	public BigDecimal getPorcentajeRetRenta() {
		return porcentajeRetRenta;
	}

	/**
	 * @param porcentajeRetRenta the porcentajeRetRenta to set
	 */
	public void setPorcentajeRetRenta(BigDecimal porcentajeRetRenta) {
		this.porcentajeRetRenta = porcentajeRetRenta;
	}

	/**
	 * @return the porcentajeRetIva
	 */
	public BigDecimal getPorcentajeRetIva() {
		return porcentajeRetIva;
	}

	/**
	 * @param porcentajeRetIva the porcentajeRetIva to set
	 */
	public void setPorcentajeRetIva(BigDecimal porcentajeRetIva) {
		this.porcentajeRetIva = porcentajeRetIva;
	}

}
