/**
 * 
 */
package com.vcw.falecpv.web.ctrl.proforma;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.modelo.persistencia.Tareacabecera;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@SessionScoped
public class TareaCotMainCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8652749472314923747L;
	
	@EJB
	private EstablecimientoServicio establecimientoServicio;
	
	private List<Tareacabecera> tareacabeceraList;
	private Tareacabecera tareacabeceraSelected;

	/**
	 * 
	 */
	public TareaCotMainCtrl() {
	}
	
	@PostConstruct
	private void init() {
		try {
			establecimientoFacade(establecimientoServicio, false);
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	/**
	 * @return the tareacabeceraList
	 */
	public List<Tareacabecera> getTareacabeceraList() {
		return tareacabeceraList;
	}

	/**
	 * @param tareacabeceraList the tareacabeceraList to set
	 */
	public void setTareacabeceraList(List<Tareacabecera> tareacabeceraList) {
		this.tareacabeceraList = tareacabeceraList;
	}

	/**
	 * @return the tareacabeceraSelected
	 */
	public Tareacabecera getTareacabeceraSelected() {
		return tareacabeceraSelected;
	}

	/**
	 * @param tareacabeceraSelected the tareacabeceraSelected to set
	 */
	public void setTareacabeceraSelected(Tareacabecera tareacabeceraSelected) {
		this.tareacabeceraSelected = tareacabeceraSelected;
	}

}
