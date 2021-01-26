/**
 * 
 */
package com.vcw.falecpv.web.ctrl.reporte;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@ViewScoped
public class RepMainCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4449551491741281123L;
	
	@EJB
	private EstablecimientoServicio establecimientoServicio;
	

	/**
	 * 
	 */
	public RepMainCtrl() {
		
	}
	
	@PostConstruct
	private void init() {
		try {
			
			super.establecimientoFacade(establecimientoServicio, false);
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param repVentaCtrl
	 * @param repFacCtrl
	 * @param repFacAnuCtrl
	 * @param repRecibCtrl
	 * @param repRecibAnuCtrl
	 */
	public void consultarMain(RepVentaCtrl repVentaCtrl,
			RepFacCtrl repFacCtrl,
			RepFacAnuCtrl repFacAnuCtrl,
			RepRecibCtrl repRecibCtrl,
			RepRecibAnuCtrl repRecibAnuCtrl) {
		try {
			
			repVentaCtrl.setEstablecimientoMain(establecimientoMain);
			repFacCtrl.setEstablecimientoMain(establecimientoMain);
			repFacAnuCtrl.setEstablecimientoMain(establecimientoMain);
			repRecibCtrl.setEstablecimientoMain(establecimientoMain);
			repRecibAnuCtrl.setEstablecimientoMain(establecimientoMain);
			
			repVentaCtrl.buscar();
			repFacCtrl.buscar();
			repFacAnuCtrl.buscar();
			repRecibCtrl.buscar();
			repRecibAnuCtrl.buscar();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

}
