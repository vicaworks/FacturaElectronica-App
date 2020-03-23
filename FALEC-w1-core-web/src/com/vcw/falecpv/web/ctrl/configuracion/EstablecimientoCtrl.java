/**
 * 
 */
package com.vcw.falecpv.web.ctrl.configuracion;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.modelo.persistencia.Establecimiento;
import com.vcw.falecpv.core.modelo.persistencia.Usuario;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@ViewScoped
public class EstablecimientoCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8788719067123516137L;
	
	
	
	
	
	@EJB
	private EstablecimientoServicio establecimientoServicio;
	
	private List<Establecimiento> establecimientoAllList;
	private Establecimiento establecimientoSelected;

	/**
	 * 
	 */
	public EstablecimientoCtrl() {
		
	}

	
	
	
	@Override
	public void limpiar() {
		super.limpiar();
	}

	@Override
	public void buscar() {
		super.buscar();
	}

	@Override
	public void refrescar() {
		try {
			consultarEstablecimiento();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	@Override
	public void eliminar() {
		super.eliminar();
	}

	@Override
	public void guardar() {
		super.guardar();
	}
	
	@Override
	public void nuevo() {
		try {
			System.out.println("Entra en nuevo");
			establecimientoSelected = new Establecimiento();
			
			establecimientoSelected.setEmpresa(AppJsfUtil.getEstablecimiento().getEmpresa());
	
			AppJsfUtil.showModalRender("dlgEstable", "frmEstable");
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMainEstable", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	private void consultarEstablecimiento() {
		try {
			establecimientoAllList= new ArrayList<>();
			establecimientoAllList  = establecimientoServicio.getEstablecimientoDao().getByEstado(EstadoRegistroEnum.ACTIVO);
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("", "Error", TextoUtil.imprimirStackTrace(e,AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void editar() {
		try {
			System.out.println(establecimientoSelected.getNombrecomercial());
			AppJsfUtil.showModalRender("dlgEstable", "frmEstable");
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMainEstable", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	public List<Establecimiento> getEstablecimientoAllList() {
		return establecimientoAllList;
	}

	public void setEstablecimientoAllList(List<Establecimiento> establecimientoAllList) {
		this.establecimientoAllList = establecimientoAllList;
	}

	public Establecimiento getEstablecimientoSelected() {
		return establecimientoSelected;
	}

	public void setEstablecimientoSelected(Establecimiento establecimientoSelected) {
		this.establecimientoSelected = establecimientoSelected;
	}
	
	
	
	
	

}
