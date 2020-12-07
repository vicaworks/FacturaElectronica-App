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
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Pago;
import com.vcw.falecpv.core.servicio.CabeceraServicio;
import com.vcw.falecpv.core.servicio.PagoServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.ctrl.pagos.CuentaCobrarCtrl;
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
	
	private Cabecera cabeceraSelected;
	private String idCabeceraSelected;
	private String callModule;
	private String callForm;
	private String viewUpdate;
	private PagoCtrl pagoCtrl;
	private CuentaCobrarCtrl cuentaCobrarCtrl;

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
			if(idCabeceraSelected!=null) {
				consultar(idCabeceraSelected);
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
			
			cabeceraServicio.actualizar(cabeceraSelected);
			
			switch (callModule) {
			case "POR_PAGAR":
				List<Pago> pagoList = pagoServicio.getPagoDao().getByIdCabecera(cabeceraSelected.getIdcabecera());
				// como es credito solo dejar un valor de pago
				pagoServicio.getPagoDao().eliminarByCabecera(cabeceraSelected.getIdcabecera());
				// todos los valores 
				Pago p = pagoList.get(0);
				p.setNotas("");
				p.setTotal(cabeceraSelected.getValorapagar());
				pagoServicio.crear(p);
				pagoCtrl.setValorPagar(cabeceraSelected.getValorapagar());
				pagoCtrl.consultarPagosCredito();
				AppJsfUtil.hideModal("dlgPorPagar");
				break;
			case "MAIN_POR_PAGAR" :
				pagoList = pagoServicio.getPagoDao().getByIdCabecera(cabeceraSelected.getIdcabecera());
				// como es credito solo dejar un valor de pago
				pagoServicio.getPagoDao().eliminarByCabecera(cabeceraSelected.getIdcabecera());
				// todos los valores 
				p = pagoList.get(0);
				p.setNotas("");
				p.setTotal(cabeceraSelected.getValorapagar());
				pagoServicio.crear(p);
				cuentaCobrarCtrl.consultar();
				AppJsfUtil.hideModal("dlgPorPagar");
			default:
				break;
			}
			
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

}
