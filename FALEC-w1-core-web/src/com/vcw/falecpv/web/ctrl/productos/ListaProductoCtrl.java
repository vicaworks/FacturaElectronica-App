/**
 * 
 */
package com.vcw.falecpv.web.ctrl.productos;

import java.math.BigDecimal;
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
import com.vcw.falecpv.web.ctrl.comprobantes.guiarem.GuiaRemFormCtrl;
import com.vcw.falecpv.web.ctrl.comprobantes.nc.NotaCreditoCtrl;
import com.vcw.falecpv.web.ctrl.proforma.CotizacionFormCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;
import com.xpert.faces.utils.FacesUtils;

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
	private NotaCreditoCtrl notaCreditoCtrl;
	private String callModule;
	private String formModule;
	private String viewUpdate;
	private String criterioBusqueda;
	private String onComplete="";
	private CotizacionFormCtrl cotizacionFormCtrl;
	
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
		productoList = productoServicio.getProductoDao().getByCriteriaEstado(establecimientoMain.getIdestablecimiento(), criterioBusqueda);
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
			productoSelected = null;
			criterioBusqueda = null;
			consultarProductos();
			if(callModule.equals("ADQUISICION")) {
				for (Producto producto : productoList) {
					producto.setPorcentajedescuento(BigDecimal.ZERO);
				}
			}
			if(callModule.equals("GUIA_REMISION")) {
				GuiaRemFormCtrl guiaRemFormCtrl = (GuiaRemFormCtrl) AppJsfUtil.getManagedBean("guiaRemFormCtrl");
				if(guiaRemFormCtrl.getDestinatarioSelected()==null) {
					AppJsfUtil.addErrorMessage(formModule, "ERROR", "NO EXISTE DESTINATARIO SELECCIONADO.");
					return;
				}
			}
			AppJsfUtil.showModalRender("dlgListaProducto", "frmListProducto");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage(formModule, "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	@Override
	public void guardar() {
		try {
			
			if(productoSelected==null) {
				AppJsfUtil.addErrorMessage("frmListProducto", "ERROR", "NO EXISTE PRODUCTO SELECCIONADO");
				return;
			}
			
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
			case "COTIZACION_FORM":
				cotizacionFormCtrl.setProductoSelected(productoSelected);
				cotizacionFormCtrl.agregarProducto();
				AppJsfUtil.hideModal("dlgListaProducto");
				Ajax.oncomplete("PrimeFaces.focus('formMain:formulario:pvDetalleDT:" + (cotizacionFormCtrl.getDetalleFacList().size()-1) + ":insDetFacCanbtidad1_input')");
				break;
			case "NOTA_CREDITO":
				
				notaCreditoCtrl.setProductoSelected(productoSelected);
				notaCreditoCtrl.agregarProducto();
				AppJsfUtil.hideModal("dlgListaProducto");
				Ajax.oncomplete("PrimeFaces.focus('formMain:pvDetalleDT:" + (notaCreditoCtrl.getDetalleNcList().size()-1) + ":insDetFacCanbtidad1_input')");
				
				break;
			case "GUIA_REMISION" :
				GuiaRemFormCtrl guiaRemFormCtrl = (GuiaRemFormCtrl) AppJsfUtil.getManagedBean("guiaRemFormCtrl");
				String agregar = guiaRemFormCtrl.agregarProducto(productoSelected);
				if(agregar!=null) {
					AppJsfUtil.addErrorMessage("frmListProducto", "ERROR", agregar);
				}else {
					AppJsfUtil.addInfoMessage("frmListProducto", "OK","AGREGADO : " + productoSelected.getNombregenerico() + " CANTIDAD: " + productoSelected.getCantidad());
				}
				
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
	
	public void establecerFocoProducto() {
		try {
			
			productoSelected = productoList.stream().filter(x->x.getIdproducto().equals(FacesUtils.getParameter("idProducto"))).findFirst().orElse(null);
			
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

	/**
	 * @return the notaCreditoCtrl
	 */
	public NotaCreditoCtrl getNotaCreditoCtrl() {
		return notaCreditoCtrl;
	}

	/**
	 * @param notaCreditoCtrl the notaCreditoCtrl to set
	 */
	public void setNotaCreditoCtrl(NotaCreditoCtrl notaCreditoCtrl) {
		this.notaCreditoCtrl = notaCreditoCtrl;
	}

	/**
	 * @return the cotizacionFormCtrl
	 */
	public CotizacionFormCtrl getCotizacionFormCtrl() {
		return cotizacionFormCtrl;
	}

	/**
	 * @param cotizacionFormCtrl the cotizacionFormCtrl to set
	 */
	public void setCotizacionFormCtrl(CotizacionFormCtrl cotizacionFormCtrl) {
		this.cotizacionFormCtrl = cotizacionFormCtrl;
	}

}
