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
import org.primefaces.event.TabChangeEvent;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.modelo.persistencia.Categoria;
import com.vcw.falecpv.core.modelo.persistencia.Producto;
import com.vcw.falecpv.core.servicio.CategoriaServicio;
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
	
	@EJB
	private CategoriaServicio categoriaServicio;
	
	private List<Producto> productoList;
	private List<Categoria> categoriaList;
	private Producto productoSelected;
	private Categoria categoriaSelected;
	private AdquisicionFrmCtrl adquisicionFrmCtrl;
	private NotaCreditoCtrl notaCreditoCtrl;
	private String callModule;
	private String formModule;
	private String viewUpdate;
	private String criterioBusqueda;
	private String onComplete="";
	private CotizacionFormCtrl cotizacionFormCtrl;
	private String accion;
	private CompFacCtrl compFacCtrl;
	
	private Integer tipoRegistro = 1;// 1 producto, 2 otro concepto
	private Integer tabIndex = 0;
	
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
		//TODO eliminar
		AppJsfUtil.limpiarFiltrosDataTable("frmListProducto:listaProductoDT");
		productoList = null;
		
		// 1. busca por el codigo de barras
		productoList = productoServicio.getProductoDao().getByCodigoBarraEstado(establecimientoMain.getIdestablecimiento(), criterioBusqueda);
		if(!productoList.isEmpty()) return;
		// 2. busca por criterio
		productoList = productoServicio.getProductoDao().getByCriteriaEstado(establecimientoMain.getIdestablecimiento(), criterioBusqueda);
		for (Producto producto : productoList) {
			producto.setCantidad(1);
		}
	}
	
	private void consultarCategorias() throws DaoException {
		categoriaList = categoriaServicio.getCategoriaDao().getByEstado(EstadoRegistroEnum.ACTIVO, establecimientoMain.getEmpresa().getIdempresa());
	}
	
	public void onTabChange(@SuppressWarnings("rawtypes") TabChangeEvent event) {
		tabIndex = Integer.parseInt(event.getTab().getId().substring(event.getTab().getId().lastIndexOf("_")+1,event.getTab().getId().length())); 
    }
	
	@Override
	public void refrescar() {
		try {
			consultarProductos();
			if(productoList.size()==1) {
				productoSelected = productoList.get(0);	
				switch (callModule) {
				case "ADQUISICION":
					tabIndex = 0;
					adquisicionFrmCtrl.nuevoDetalle();			
					adquisicionFrmCtrl.getAdquisiciondetalleSelected().setAccion("NUEVO");
					adquisicionFrmCtrl.seleccionarProducto(productoSelected);					
					AppJsfUtil.executeJavaScript("PrimeFaces.focus('frmListProducto:tvPLmain:fsvAdquisicion:innCantDt')");
					break;
				case "FACTURA":
					tabIndex = 0;
					compFacCtrl.agregarProducto(productoSelected);
					compFacCtrl.getDetalleSelected().setAccion("NUEVO");
					compFacCtrl.getDetalleSelected().setProducto(productoSelected);
					if(productoSelected.getTipoventa()==2) {
						AppJsfUtil.executeJavaScript("PrimeFaces.focus('frmListProducto:tvPLmain:fsvFactura:innCantFrm')");						
					}else {
						AppJsfUtil.executeJavaScript("PrimeFaces.focus('frmListProducto:tvPLmain:fsvFactura:innCantFrm2')");
					}
					break;
				case "NOTA_CREDITO":
					tabIndex = 0;
					notaCreditoCtrl.agregarProducto(productoSelected);
					notaCreditoCtrl.getDetalleSelected().setAccion("NUEVO");
					notaCreditoCtrl.getDetalleSelected().setProducto(productoSelected);
					if(productoSelected.getTipoventa()==2) {
						AppJsfUtil.executeJavaScript("PrimeFaces.focus('frmListProducto:tvPLmain:fsvNCredito:innCantFrm')");						
					}else {
						AppJsfUtil.executeJavaScript("PrimeFaces.focus('frmListProducto:tvPLmain:fsvNCredito:innCantFrm2')");
					}
					break;
				default:
					break;
				}
				productoList=null;
				categoriaSelected=null;
			}else {
				tabIndex = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmListProducto", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}	
	
	public void loadPantalla() {
		try {
			categoriaSelected = null;
			productoSelected = null;
			criterioBusqueda = null;
			consultarCategorias();
			
			switch (callModule) {
			case "ADQUISICION":
				if(accion.equals("NUEVO")) {
					adquisicionFrmCtrl.nuevoDetalle();
					tipoRegistro = 1;					
				}else {
					if(adquisicionFrmCtrl.getAdquisiciondetalleSelected().getProducto()!=null) {
						tipoRegistro = 1;
						productoSelected = adquisicionFrmCtrl.getAdquisiciondetalleSelected().getProducto();
					}else {
						tipoRegistro = 2;
					}
				}
				adquisicionFrmCtrl.getAdquisiciondetalleSelected().setAccion(accion);
				break;
			case "FACTURA":
				if(accion.equals("NUEVO")) {
					compFacCtrl.agregarItem();
					tipoRegistro = 1;					
				}else {
					if(compFacCtrl.getDetalleSelected().getProducto()!=null) {
						tipoRegistro = 1;
						productoSelected = compFacCtrl.getDetalleSelected().getProducto();
						// precio venta
						compFacCtrl.getDetalleSelected().setPrecioVenta(0);
						if(compFacCtrl.getDetalleSelected().getProducto().getPreciouno().doubleValue()==compFacCtrl.getDetalleSelected().getPreciounitario().doubleValue()) {
							compFacCtrl.getDetalleSelected().setPrecioVenta(1);
						}else if(compFacCtrl.getDetalleSelected().getProducto().getPreciodos().doubleValue()==compFacCtrl.getDetalleSelected().getPreciounitario().doubleValue()) {
							compFacCtrl.getDetalleSelected().setPrecioVenta(2);
						}else if(compFacCtrl.getDetalleSelected().getProducto().getPreciotres().doubleValue()==compFacCtrl.getDetalleSelected().getPreciounitario().doubleValue()) {
							compFacCtrl.getDetalleSelected().setPrecioVenta(3);
						}
					}else {
						tipoRegistro = 2;						
					}
				}
				break;
			case "NOTA_CREDITO":
				if(accion.equals("NUEVO")) {
					notaCreditoCtrl.agregarItem();
					tipoRegistro = 1;					
				}else {
					if(notaCreditoCtrl.getDetalleSelected().getProducto()!=null) {
						tipoRegistro = 1;
						productoSelected = notaCreditoCtrl.getDetalleSelected().getProducto();
						// precio venta
						notaCreditoCtrl.getDetalleSelected().setPrecioVenta(0);
						if(notaCreditoCtrl.getDetalleSelected().getProducto().getPreciouno().doubleValue()==notaCreditoCtrl.getDetalleSelected().getPreciounitario().doubleValue()) {
							notaCreditoCtrl.getDetalleSelected().setPrecioVenta(1);
						}else if(notaCreditoCtrl.getDetalleSelected().getProducto().getPreciodos().doubleValue()==notaCreditoCtrl.getDetalleSelected().getPreciounitario().doubleValue()) {
							notaCreditoCtrl.getDetalleSelected().setPrecioVenta(2);
						}else if(notaCreditoCtrl.getDetalleSelected().getProducto().getPreciotres().doubleValue()==notaCreditoCtrl.getDetalleSelected().getPreciounitario().doubleValue()) {
							notaCreditoCtrl.getDetalleSelected().setPrecioVenta(3);
						}
					}else {
						tipoRegistro = 2;						
					}
				}
				break;
			default:
				break;
			}
			
			AppJsfUtil.showModalRender("dlgListaProducto", "frmListProducto");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage(formModule, "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void refrescarCategoria() {
		try {
			productoList = null;
			if(categoriaSelected==null) {
				AppJsfUtil.addErrorMessage("frmListProducto:tvPLmain:somPLProductoCategoria", "", "REQUERIDO");
				return;
			}
			productoList = productoServicio.getProductoDao().getByCategoriaEstado(establecimientoMain.getIdestablecimiento(), categoriaSelected.getIdcategoria());
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmListProducto", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void seleccionarProducto() {
		try {
			
			accion = "NUEVO";
			switch (callModule) {
			case "ADQUISICION":
				adquisicionFrmCtrl.seleccionarProducto(productoSelected);
				adquisicionFrmCtrl.getAdquisiciondetalleSelected().setAccion(accion);
				tabIndex = 0;				
				// foco
				AppJsfUtil.executeJavaScript("PrimeFaces.focus('frmListProducto:tvPLmain:fsvAdquisicion:innCantDt')");
				break;
			case "FACTURA":
				compFacCtrl.agregarProducto(productoSelected);
				compFacCtrl.getDetalleSelected().setAccion(accion);
				tabIndex = 0;
				if(productoSelected.getTipoventa()==2) {
					AppJsfUtil.executeJavaScript("PrimeFaces.focus('frmListProducto:tvPLmain:fsvFactura:innCantFrm')");						
				}else {
					AppJsfUtil.executeJavaScript("PrimeFaces.focus('frmListProducto:tvPLmain:fsvFactura:innCantFrm2')");
				}
				break;
			case "NOTA_CREDITO":
				notaCreditoCtrl.agregarProducto(productoSelected);
				notaCreditoCtrl.getDetalleSelected().setAccion(accion);
				tabIndex = 0;
				if(productoSelected.getTipoventa()==2) {
					AppJsfUtil.executeJavaScript("PrimeFaces.focus('frmListProducto:tvPLmain:fsvNCredito:innCantFrm')");						
				}else {
					AppJsfUtil.executeJavaScript("PrimeFaces.focus('frmListProducto:tvPLmain:fsvNCredito:innCantFrm2')");
				}
				break;
			default:
				break;
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmListProducto", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void limpiar() {
		try {
			
			criterioBusqueda = null;
			switch (callModule) {
			case "ADQUISICION":
				adquisicionFrmCtrl.nuevoDetalle();
				adquisicionFrmCtrl.getAdquisiciondetalleSelected().setCodProducto("COD" + (adquisicionFrmCtrl.getAdquisiciondetalleList()==null?1:adquisicionFrmCtrl.getAdquisiciondetalleList().size()+1));
				adquisicionFrmCtrl.getAdquisiciondetalleSelected().setAccion("NUEVO");
				if(tipoRegistro==1) {
					AppJsfUtil.executeJavaScript("PrimeFaces.focus('frmListProducto:intListProBusqueda')");
				}else {
					AppJsfUtil.executeJavaScript("PrimeFaces.focus('frmListProducto:tvPLmain:fsvAdquisicion:intAdqDescripcion')");
				}
				break;
			case "FACTURA":
				compFacCtrl.agregarItem();
				compFacCtrl.getDetalleSelected().setAccion("NUEVO");
				if(tipoRegistro==1) {
					AppJsfUtil.executeJavaScript("PrimeFaces.focus('frmListProducto:intListProBusqueda')");
				}else {
					compFacCtrl.getDetalleSelected().setCodproducto("COD" + (compFacCtrl.getDetalleFacList()==null?1:compFacCtrl.getDetalleFacList().size()+1));
					AppJsfUtil.executeJavaScript("PrimeFaces.focus('frmListProducto:tvPLmain:fsvFactura:intFacDescripcion')");
				}
				break;
			case "NOTA_CREDITO":
				notaCreditoCtrl.agregarItem();
				notaCreditoCtrl.getDetalleSelected().setAccion("NUEVO");
				if(tipoRegistro==1) {
					AppJsfUtil.executeJavaScript("PrimeFaces.focus('frmListProducto:intListProBusqueda')");
				}else {
					notaCreditoCtrl.getDetalleSelected().setCodproducto("COD" + (notaCreditoCtrl.getDetalleNcList()==null?1:notaCreditoCtrl.getDetalleNcList().size()+1));
					AppJsfUtil.executeJavaScript("PrimeFaces.focus('frmListProducto:tvPLmain:fsvNCredito:intFacDescripcion')");
				}
				break;
			default:
				break;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmListProducto", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void cambioTipoRegistro() {
		try {
			productoSelected = null;
			productoList = null;
			categoriaSelected = null;
			criterioBusqueda = null;
			switch (callModule) {
			case "ADQUISICION":
				accion = "NUEVO";
				adquisicionFrmCtrl.nuevoDetalle();
				adquisicionFrmCtrl.getAdquisiciondetalleSelected().setAccion(accion);
				if(tipoRegistro==2) {
					adquisicionFrmCtrl.getAdquisiciondetalleSelected().setCodProducto("COD" + (adquisicionFrmCtrl.getAdquisiciondetalleList()==null?1:adquisicionFrmCtrl.getAdquisiciondetalleList().size()+1));					
					AppJsfUtil.executeJavaScript("PrimeFaces.focus('frmListProducto:tvPLmain:fsvAdquisicion:intAdqDescripcion')");
				}else {
					AppJsfUtil.executeJavaScript("PrimeFaces.focus('frmListProducto:intListProBusqueda')");
				}
				break;
			case "FACTURA":
				accion = "NUEVO";
				compFacCtrl.agregarItem();
				compFacCtrl.getDetalleSelected().setAccion(accion);				
				if(tipoRegistro==2) {
					compFacCtrl.getDetalleSelected().setCodproducto("COD" + (compFacCtrl.getDetalleFacList()==null?1:compFacCtrl.getDetalleFacList().size()+1));
					AppJsfUtil.executeJavaScript("PrimeFaces.focus('frmListProducto:tvPLmain:fsvFactura:intFacDescripcion')");
				}else {
					AppJsfUtil.executeJavaScript("PrimeFaces.focus('frmListProducto:intListProBusqueda')");
				}
				break;
			case "NOTA_CREDITO":
				accion = "NUEVO";
				notaCreditoCtrl.agregarItem();
				notaCreditoCtrl.getDetalleSelected().setAccion(accion);				
				if(tipoRegistro==2) {
					notaCreditoCtrl.getDetalleSelected().setCodproducto("COD" + (notaCreditoCtrl.getDetalleNcList()==null?1:notaCreditoCtrl.getDetalleNcList().size()+1));
					AppJsfUtil.executeJavaScript("PrimeFaces.focus('frmListProducto:tvPLmain:fsvNCredito:intFacDescripcion')");
				}else {
					AppJsfUtil.executeJavaScript("PrimeFaces.focus('frmListProducto:intListProBusqueda')");
				}
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmListProducto", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	//====================================================================================================
	// TODO Eliminar todos estos metodos al terminar la migracion
	
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
			
//			case "FACTURA_FORM2":
//				CompFacCtrl compFacCtrl = (CompFacCtrl)AppJsfUtil.getManagedBean("compFacCtrl");
//				compFacCtrl.setProductoSelected(productoSelected);
//				compFacCtrl.agregarProducto();
//				AppJsfUtil.hideModal("dlgListaProducto");
//				// foco a la lista
//				Ajax.oncomplete("PrimeFaces.focus('formMain:pvDetalleDT:" + (compFacCtrl.getDetalleFacList().size()-1) + ":insDetFacCanbtidad1_input')");
//				
//				//AppJsfUtil.executeJavaScript("PrimeFaces.focus('formMain:pvDetalleDT:" + (compFacCtrl.getDetalleFacList().size()-1) + ":insDetFacCanbtidad1_input')");
//				//AppJsfUtil.addInfoMessage("frmListProducto", "PRODUCTO AGREGADO CORRECTAMENTE");
//				break;
			case "COTIZACION_FORM":
				cotizacionFormCtrl.setProductoSelected(productoSelected);
				cotizacionFormCtrl.agregarProducto();
				AppJsfUtil.hideModal("dlgListaProducto");
				Ajax.oncomplete("PrimeFaces.focus('formMain:formulario:pvDetalleDT:" + (cotizacionFormCtrl.getDetalleFacList().size()-1) + ":insDetFacCanbtidad1_input')");
				break;
//			case "NOTA_CREDITO":
//				
//				notaCreditoCtrl.setProductoSelected(productoSelected);
//				notaCreditoCtrl.agregarProducto();
//				AppJsfUtil.hideModal("dlgListaProducto");
//				Ajax.oncomplete("PrimeFaces.focus('formMain:pvDetalleDT:" + (notaCreditoCtrl.getDetalleNcList().size()-1) + ":insDetFacCanbtidad1_input')");
//				
//				break;
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

	/**
	 * @return the tipoRegistro
	 */
	public Integer getTipoRegistro() {
		return tipoRegistro;
	}

	/**
	 * @param tipoRegistro the tipoRegistro to set
	 */
	public void setTipoRegistro(Integer tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

	/**
	 * @return the tabIndex
	 */
	public Integer getTabIndex() {
		return tabIndex;
	}

	/**
	 * @param tabIndex the tabIndex to set
	 */
	public void setTabIndex(Integer tabIndex) {
		this.tabIndex = tabIndex;
	}

	/**
	 * @return the categoriaList
	 */
	public List<Categoria> getCategoriaList() {
		return categoriaList;
	}

	/**
	 * @param categoriaList the categoriaList to set
	 */
	public void setCategoriaList(List<Categoria> categoriaList) {
		this.categoriaList = categoriaList;
	}

	/**
	 * @return the categoriaSelected
	 */
	public Categoria getCategoriaSelected() {
		return categoriaSelected;
	}

	/**
	 * @param categoriaSelected the categoriaSelected to set
	 */
	public void setCategoriaSelected(Categoria categoriaSelected) {
		this.categoriaSelected = categoriaSelected;
	}

	/**
	 * @return the accion
	 */
	public String getAccion() {
		return accion;
	}

	/**
	 * @param accion the accion to set
	 */
	public void setAccion(String accion) {
		this.accion = accion;
	}

	/**
	 * @return the compFacCtrl
	 */
	public CompFacCtrl getCompFacCtrl() {
		return compFacCtrl;
	}

	/**
	 * @param compFacCtrl the compFacCtrl to set
	 */
	public void setCompFacCtrl(CompFacCtrl compFacCtrl) {
		this.compFacCtrl = compFacCtrl;
	}	

}
