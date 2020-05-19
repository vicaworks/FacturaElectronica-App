/**
 * 
 */
package com.vcw.falecpv.web.ctrl.productos;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.omnifaces.util.Ajax;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.modelo.persistencia.Producto;
import com.vcw.falecpv.core.servicio.ProductoServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.ctrl.adquisicion.AdquisicionFrmCtrl;
import com.vcw.falecpv.web.ctrl.comprobantes.fac.CompFacCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@ViewScoped
public class ListaProductoCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5555596010142826345L;

	@EJB
	private ProductoServicio productoServicio;
	
	private List<Producto> productoList;
	private Producto productoSelected;
	private AdquisicionFrmCtrl adquisicionFrmCtrl;
	private String callModule;
	private String formModule;
	private String viewUpdate;
	private String criterioBusqueda;
	private String onComplete="";
	
	/**
	 * 
	 */
	public ListaProductoCtrl() {
	}
	
	@PostConstruct
	private void init() {
		try {
			criterioBusqueda = null;
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmListProducto", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void consultarProductos()throws DaoException{
		AppJsfUtil.limpiarFiltrosDataTable("frmListProducto:listaProductoDT");
		productoList = null;
		productoList = productoServicio.getProductoDao().getByCriteriaEstado(AppJsfUtil.getEstablecimiento().getIdestablecimiento(), criterioBusqueda);
		for (Producto producto : productoList) {
			producto.setCantidad(1);
		}
	}
	
	@Override
	public void refrescar() {
		try {
			consultarProductos();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmListProducto", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	public void cargarPantalla() {
		try {
			
			consultarProductos();
			AppJsfUtil.showModalRender("dlgListaProducto", "frmListProducto");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage(formModule, "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	@Override
	public void guardar() {
		try {
			
			switch (callModule) {
			case "ADQUISICION":
				adquisicionFrmCtrl.agregarProducto(productoSelected);
				AppJsfUtil.addInfoMessage("frmListProducto", "PRODUCTO AGREGADO CORRECTAMENTE");
				break;
			
			case "FACTURA_FORM2":
				CompFacCtrl compFacCtrl = (CompFacCtrl)AppJsfUtil.getManagedBean("compFacCtrl");
				compFacCtrl.setProductoSelected(productoSelected);
				compFacCtrl.agregarProducto();
				AppJsfUtil.hideModal("dlgListaProducto");
				// foco a la lista
				Ajax.oncomplete("PrimeFaces.focus('formMain:pvDetalleDT:" + (compFacCtrl.getDetalleFacList().size()-1) + ":insDetFacCanbtidad1_input')");
				//AppJsfUtil.executeJavaScript("PrimeFaces.focus('formMain:pvDetalleDT:" + (compFacCtrl.getDetalleFacList().size()-1) + ":insDetFacCanbtidad1_input')");
				//AppJsfUtil.addInfoMessage("frmListProducto", "PRODUCTO AGREGADO CORRECTAMENTE");
				break;
				
			default:
				AppJsfUtil.hideModal("dlgListaProducto");
				break;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmListProducto", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	/**
	 * @return the productoList
	 */
	public List<Producto> getProductoList() {
		return productoList;
	}

	/**
	 * @param productoList the productoList to set
	 */
	public void setProductoList(List<Producto> productoList) {
		this.productoList = productoList;
	}

	/**
	 * @return the productoSelected
	 */
	public Producto getProductoSelected() {
		return productoSelected;
	}

	/**
	 * @param productoSelected the productoSelected to set
	 */
	public void setProductoSelected(Producto productoSelected) {
		this.productoSelected = productoSelected;
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
	 * @return the criterioBusqueda
	 */
	public String getCriterioBusqueda() {
		return criterioBusqueda;
	}

	/**
	 * @param criterioBusqueda the criterioBusqueda to set
	 */
	public void setCriterioBusqueda(String criterioBusqueda) {
		this.criterioBusqueda = criterioBusqueda;
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
	 * @return the onComplete
	 */
	public String getOnComplete() {
		return onComplete;
	}

	/**
	 * @param onComplete the onComplete to set
	 */
	public void setOnComplete(String onComplete) {
		this.onComplete = onComplete;
	}

}
