/**
 * 
 */
package com.vcw.falecpv.web.ctrl.herramientas;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.modelo.persistencia.Cliente;
import com.vcw.falecpv.core.servicio.ClienteServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.ctrl.adquisicion.AdquisicionFrmCtrl;
import com.vcw.falecpv.web.ctrl.adquisicion.RetencionFrmCtrl;
import com.vcw.falecpv.web.ctrl.comprobantes.fac.CompFacCtrl;
import com.vcw.falecpv.web.ctrl.comprobantes.guiarem.GuiaRemFormCtrl;
import com.vcw.falecpv.web.ctrl.comprobantes.liqcompra.LiqCompraFormCtrl;
import com.vcw.falecpv.web.ctrl.comprobantes.nc.NotaCreditoCtrl;
import com.vcw.falecpv.web.ctrl.comprobantes.nd.NotaDebitoFrmCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@ViewScoped
public class ListaClienteCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9193657970276015404L;
	
	@EJB
	private ClienteServicio clienteServicio;
	
	private List<Cliente> clienteList;
	private Cliente clienteSelected;
	private CompFacCtrl compFacCtrl;
	private RetencionFrmCtrl retencionFrmCtrl;
	private NotaCreditoCtrl notaCreditoCtrl;
	private NotaDebitoFrmCtrl notaDebitoFrmCtrl;
	private GuiaRemFormCtrl guiaRemFormCtrl;
	private LiqCompraFormCtrl liqCompraFormCtrl;
	private AdquisicionFrmCtrl adquisicionFrmCtrl;
	private String callModule;
	private String formModule;
	private String viewUpdate;
	private String criterioBusqueda;
	
	public ListaClienteCtrl() {
	}
	
	@PostConstruct
	private void init() {
		try {
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmListCliente", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void consultarClientes()throws DaoException{
		AppJsfUtil.limpiarFiltrosDataTable("frmListCliente:listaClienteDT");
		clienteList = null;
		clienteList = clienteServicio.getClienteDao().consultarClientes(AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa(), criterioBusqueda);
	}
	
	@Override
	public void refrescar() {
		try {
			consultarClientes();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmListCliente", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void cargarPantalla() {
		try {
			clienteSelected = null;
			criterioBusqueda = null;
			consultarClientes();
			AppJsfUtil.showModalRender("dlgListaCliente", "frmListCliente");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage(formModule, "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	@Override
	public void guardar() {
		try {
			
			if(clienteSelected==null) {
				AppJsfUtil.addErrorMessage("frmListCliente", "ERROR", "NO EXISTE CLIENTE SELECCIONADO");
				return;
			}
			
			switch (callModule) {
			
			case "FACTURA":
				compFacCtrl = (CompFacCtrl)AppJsfUtil.getManagedBean("compFacCtrl");
				compFacCtrl.getCabecerSelected().setCliente(clienteSelected);
				break;
			
			case "RETENCION":
				retencionFrmCtrl = (RetencionFrmCtrl)AppJsfUtil.getManagedBean("retencionFrmCtrl");
				retencionFrmCtrl.getRetencionSeleccion().setCliente(clienteSelected);
				break;
				
			case "NotaCreditoCtrl":
				notaCreditoCtrl = (NotaCreditoCtrl)AppJsfUtil.getManagedBean("notaCreditoCtrl");
				notaCreditoCtrl.getNotaCreditoSeleccion().setCliente(clienteSelected);
				break;
			
			case "NotaDebitoFrmCtrl":
				notaDebitoFrmCtrl = (NotaDebitoFrmCtrl)AppJsfUtil.getManagedBean("notaDebitoFrmCtrl");
				notaDebitoFrmCtrl.getNotDebitoSelected().setCliente(clienteSelected);
				break;
			
			case "guiaRemFormCtrl":
				guiaRemFormCtrl = (GuiaRemFormCtrl) AppJsfUtil.getManagedBean("guiaRemFormCtrl");
				guiaRemFormCtrl.getDestinatarioSelected().setCliente(clienteSelected);
				guiaRemFormCtrl.getDestinatarioSelected().setIdentificaciondestinatario(clienteSelected.getIdentificacion());
				guiaRemFormCtrl.getDestinatarioSelected().setRazonsocialdestinatario(clienteSelected.getRazonsocial());
				break;
				
			case "LIQCOMPRA":
				liqCompraFormCtrl = (LiqCompraFormCtrl) AppJsfUtil.getManagedBean("liqCompraFormCtrl");
				liqCompraFormCtrl.getLiqCompraSelected().setCliente(clienteSelected);
				break;
				
			case "ADQUISICION":
				adquisicionFrmCtrl = (AdquisicionFrmCtrl) AppJsfUtil.getManagedBean("adquisicionFrmCtrl");
				adquisicionFrmCtrl.getAdquisicionSelected().setCliente(clienteSelected);
				break;
				
			default:
				break;
			}
			AppJsfUtil.hideModal("dlgListaCliente");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmListCliente", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
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
	 * @return the clienteList
	 */
	public List<Cliente> getClienteList() {
		return clienteList;
	}

	/**
	 * @param clienteList the clienteList to set
	 */
	public void setClienteList(List<Cliente> clienteList) {
		this.clienteList = clienteList;
	}

	/**
	 * @return the clienteSelected
	 */
	public Cliente getClienteSelected() {
		return clienteSelected;
	}

	/**
	 * @param clienteSelected the clienteSelected to set
	 */
	public void setClienteSelected(Cliente clienteSelected) {
		this.clienteSelected = clienteSelected;
	}

	/**
	 * @return the clienteServicio
	 */
	public ClienteServicio getClienteServicio() {
		return clienteServicio;
	}

	/**
	 * @param clienteServicio the clienteServicio to set
	 */
	public void setClienteServicio(ClienteServicio clienteServicio) {
		this.clienteServicio = clienteServicio;
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

	/**
	 * @return the retencionFrmCtrl
	 */
	public RetencionFrmCtrl getRetencionFrmCtrl() {
		return retencionFrmCtrl;
	}

	/**
	 * @param retencionFrmCtrl the retencionFrmCtrl to set
	 */
	public void setRetencionFrmCtrl(RetencionFrmCtrl retencionFrmCtrl) {
		this.retencionFrmCtrl = retencionFrmCtrl;
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
	 * @return the notaDebitoFrmCtrl
	 */
	public NotaDebitoFrmCtrl getNotaDebitoFrmCtrl() {
		return notaDebitoFrmCtrl;
	}

	/**
	 * @param notaDebitoFrmCtrl the notaDebitoFrmCtrl to set
	 */
	public void setNotaDebitoFrmCtrl(NotaDebitoFrmCtrl notaDebitoFrmCtrl) {
		this.notaDebitoFrmCtrl = notaDebitoFrmCtrl;
	}

	/**
	 * @return the guiaRemFormCtrl
	 */
	public GuiaRemFormCtrl getGuiaRemFormCtrl() {
		return guiaRemFormCtrl;
	}

	/**
	 * @param guiaRemFormCtrl the guiaRemFormCtrl to set
	 */
	public void setGuiaRemFormCtrl(GuiaRemFormCtrl guiaRemFormCtrl) {
		this.guiaRemFormCtrl = guiaRemFormCtrl;
	}

	/**
	 * @return the liqCompraFormCtrl
	 */
	public LiqCompraFormCtrl getLiqCompraFormCtrl() {
		return liqCompraFormCtrl;
	}

	/**
	 * @param liqCompraFormCtrl the liqCompraFormCtrl to set
	 */
	public void setLiqCompraFormCtrl(LiqCompraFormCtrl liqCompraFormCtrl) {
		this.liqCompraFormCtrl = liqCompraFormCtrl;
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
	
	

}
