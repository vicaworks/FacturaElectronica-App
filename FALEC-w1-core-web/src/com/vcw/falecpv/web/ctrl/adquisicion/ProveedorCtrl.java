/**
 * 
 */
package com.vcw.falecpv.web.ctrl.adquisicion;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.model.StreamedContent;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.modelo.persistencia.Proveedor;
import com.vcw.falecpv.core.modelo.persistencia.TipoIdentificacion;
import com.vcw.falecpv.core.servicio.ProveedorServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@ViewScoped
public class ProveedorCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5057524440057042402L;
	
	@EJB
	private ProveedorServicio proveedorServicio;
	
	
	private List<Proveedor> proveedorList;
	private Proveedor proveedorSelected;
	private String criteriBusqueda;
	private List<TipoIdentificacion> tipoIdentificacionList;
	private String callModule;
	private String formModule;
	private String viewUpdate;
	private String estado;

	/**
	 * 
	 */
	public ProveedorCtrl() {
	}
	
	@PostConstruct
	private void init() {
		try {
			estado = EstadoRegistroEnum.ACTIVO.getInicial();
			consultarProveedores();
			consultarTipoIdentificacion();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void consultarTipoIdentificacion()throws DaoException{
		tipoIdentificacionList = null;
		tipoIdentificacionList = proveedorServicio.getByProveedor();
	}
	
	public void consultarProveedores()throws DaoException {
		AppJsfUtil.limpiarFiltrosDataTable("formMain:adquisicionDT");
		proveedorList = null;
		proveedorList = proveedorServicio.getProveedorDao().getByConsulta(AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa(), EstadoRegistroEnum.getByInicial(estado), criteriBusqueda);
	}

	@Override
	public void refrescar() {
		try {
			
			proveedorSelected = null;
			consultarProveedores();
			consultarTipoIdentificacion();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	@Override
	public void eliminar() {
		try {
			
			if(proveedorSelected==null) {
				AppJsfUtil.addErrorMessage("formMain", "NO EXISTE REGISTRO SELECIONADO");
				return;
			}
			
			proveedorServicio.eliminar(proveedorSelected);
			proveedorSelected = null;
			consultarProveedores();
			AppJsfUtil.addInfoMessage("formMain", "REGISTRO ELIMINADO CORRECTAMENTE");
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	@Override
	public void guardar() {
		try {
			
			// validaciones
			if(proveedorServicio.existeCampo(proveedorSelected.getIdproveedor(), proveedorSelected.getEmpresa().getIdempresa(), "RS", proveedorSelected.getRazonsocial())) {
				AppJsfUtil.addErrorMessage("frmProveedor","ERROR" ,"YA EXISTE UN PROVEDOR CON LA MISMA RAZON SOCIAL");
				AppJsfUtil.addErrorMessage("frmProveedor:intProveedorRazonSocial","YA EXISTE");
				return;
			}
			
			if(proveedorServicio.existeCampo(proveedorSelected.getIdproveedor(), proveedorSelected.getEmpresa().getIdempresa(), "NB", proveedorSelected.getNombrecomercial())) {
				AppJsfUtil.addErrorMessage("frmProveedor","ERROR" ,"YA EXISTE UN PROVEDOR CON EL MISMO NOMBRE COMERCIAL");
				AppJsfUtil.addErrorMessage("frmProveedor:intProveedorNombreComercial","YA EXISTE");
				return;
			}
			
			if(proveedorServicio.existeCampo(proveedorSelected.getIdproveedor(), proveedorSelected.getEmpresa().getIdempresa(), "ID", proveedorSelected.getIdentificacion())) {
				AppJsfUtil.addErrorMessage("frmProveedor","ERROR" ,"YA EXISTE UN PROVEDOR CON LA MISMA IDENTIFICACION");
				AppJsfUtil.addErrorMessage("frmProveedor:intProveedorNombreComercial","YA EXISTE");
				return;
			}
			
			proveedorSelected.setUpdated(new Date());
			proveedorSelected = proveedorServicio.guardar(proveedorSelected, AppJsfUtil.getEstablecimiento().getIdestablecimiento());
			
			switch (callModule) {
			case "PROVEEDOR":
				consultarProveedores();
				break;

			default:
				break;
			}
			
			AppJsfUtil.addInfoMessage("frmProveedor","OK", "REGISTRO ALMACENADO CORRECTAMENTE.");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmProveedor", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	@Override
	public void editar() {
		try {
			
			if(proveedorSelected==null) {
				AppJsfUtil.addErrorMessage("formMain", "NO EXISTE REGISTRO SELECIONADO");
				return;
			}
			
			AppJsfUtil.showModalRender("dlgProveedor", "frmProveedor");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	@Override
	public void nuevo() {
		try {
			
			proveedorSelected = new Proveedor();
			proveedorSelected.setEmpresa(AppJsfUtil.getEstablecimiento().getEmpresa());
			proveedorSelected.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
			proveedorSelected.setEstado(EstadoRegistroEnum.ACTIVO.getInicial());
			
			AppJsfUtil.showModalRender("dlgProveedor", "frmProveedor");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public StreamedContent getFileProveedor() {
		try {
			
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		return null;
	}

	/**
	 * @return the proveedorList
	 */
	public List<Proveedor> getProveedorList() {
		return proveedorList;
	}

	/**
	 * @param proveedorList the proveedorList to set
	 */
	public void setProveedorList(List<Proveedor> proveedorList) {
		this.proveedorList = proveedorList;
	}

	/**
	 * @return the proveedorSelected
	 */
	public Proveedor getProveedorSelected() {
		return proveedorSelected;
	}

	/**
	 * @param proveedorSelected the proveedorSelected to set
	 */
	public void setProveedorSelected(Proveedor proveedorSelected) {
		this.proveedorSelected = proveedorSelected;
	}

	/**
	 * @return the tipoIdentificacionList
	 */
	public List<TipoIdentificacion> getTipoIdentificacionList() {
		return tipoIdentificacionList;
	}

	/**
	 * @param tipoIdentificacionList the tipoIdentificacionList to set
	 */
	public void setTipoIdentificacionList(List<TipoIdentificacion> tipoIdentificacionList) {
		this.tipoIdentificacionList = tipoIdentificacionList;
	}

	/**
	 * @return the criteriBusqueda
	 */
	public String getCriteriBusqueda() {
		return criteriBusqueda;
	}

	/**
	 * @param criteriBusqueda the criteriBusqueda to set
	 */
	public void setCriteriBusqueda(String criteriBusqueda) {
		this.criteriBusqueda = criteriBusqueda;
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
	 * @return the formModule
	 */
	public String getFormModule() {
		return formModule;
	}

	/**
	 * @param formModule the formModule to set
	 */
	public void setFormModule(String formModule) {
		this.formModule = formModule;
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
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	

}
