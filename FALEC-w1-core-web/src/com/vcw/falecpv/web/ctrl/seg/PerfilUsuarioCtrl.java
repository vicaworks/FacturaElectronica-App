/**
 * 
 */
package com.vcw.falecpv.web.ctrl.seg;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.modelo.persistencia.Segperfil;
import com.vcw.falecpv.core.modelo.persistencia.Segperfilpredefinido;
import com.vcw.falecpv.core.servicio.seg.SegperfilServicio;
import com.vcw.falecpv.core.servicio.seg.SegperfilpredefinidoServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@ViewScoped
public class PerfilUsuarioCtrl extends BaseCtrl{

	@EJB
	private SegperfilpredefinidoServicio segperfilpredefinidoServicio;
	
	@EJB
	private SegperfilServicio segperfilServicio;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5139602474752165395L;
	
	private List<Segperfilpredefinido> segperfilpredefinidoList;
	private List<Segperfil> segperfilList;
	private Segperfilpredefinido segperfilpredefinidoSeleccion;
	private String callForm;

	/**
	 * 
	 */
	public PerfilUsuarioCtrl() {
		
	}
	
	private void consultarPerfilPredefinido() {
		segperfilpredefinidoList = null;
		segperfilpredefinidoList = segperfilpredefinidoServicio.getSegperfilpredefinidoDao().getByEstado(EstadoRegistroEnum.ACTIVO);
	}
	
	private void consultarPerfiles() {
		segperfilList = null;
		segperfilList = segperfilServicio.getSegperfilDao().getByEstado(EstadoRegistroEnum.ACTIVO);
	}

	public void cargarFormulario() {
		try {
		
			consultarPerfilPredefinido();
			consultarPerfiles();
			AppJsfUtil.showModalRender("dlgPerfilUsuario", "frmPerfilUsuario");
			
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage(callForm, "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void refrescar() {
		try {
			
			consultarPerfilPredefinido();
			consultarPerfiles();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmPerfilUsuario", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	/**
	 * @return the segperfilpredefinidoServicio
	 */
	public SegperfilpredefinidoServicio getSegperfilpredefinidoServicio() {
		return segperfilpredefinidoServicio;
	}

	/**
	 * @param segperfilpredefinidoServicio the segperfilpredefinidoServicio to set
	 */
	public void setSegperfilpredefinidoServicio(SegperfilpredefinidoServicio segperfilpredefinidoServicio) {
		this.segperfilpredefinidoServicio = segperfilpredefinidoServicio;
	}

	/**
	 * @return the segperfilpredefinidoList
	 */
	public List<Segperfilpredefinido> getSegperfilpredefinidoList() {
		return segperfilpredefinidoList;
	}

	/**
	 * @param segperfilpredefinidoList the segperfilpredefinidoList to set
	 */
	public void setSegperfilpredefinidoList(List<Segperfilpredefinido> segperfilpredefinidoList) {
		this.segperfilpredefinidoList = segperfilpredefinidoList;
	}

	/**
	 * @return the segperfilpredefinidoSeleccion
	 */
	public Segperfilpredefinido getSegperfilpredefinidoSeleccion() {
		return segperfilpredefinidoSeleccion;
	}

	/**
	 * @param segperfilpredefinidoSeleccion the segperfilpredefinidoSeleccion to set
	 */
	public void setSegperfilpredefinidoSeleccion(Segperfilpredefinido segperfilpredefinidoSeleccion) {
		this.segperfilpredefinidoSeleccion = segperfilpredefinidoSeleccion;
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
	 * @return the segperfilList
	 */
	public List<Segperfil> getSegperfilList() {
		return segperfilList;
	}

	/**
	 * @param segperfilList the segperfilList to set
	 */
	public void setSegperfilList(List<Segperfil> segperfilList) {
		this.segperfilList = segperfilList;
	}
	
	

}
