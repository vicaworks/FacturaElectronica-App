package com.vcw.falecpv.web.ctrl.herramientas;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.modelo.persistencia.Cliente;
import com.vcw.falecpv.core.modelo.persistencia.TipoIdentificacion;
import com.vcw.falecpv.core.servicio.ClienteServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author Jorge Toaza
 *
 */
@Named
@ViewScoped
public class ClienteCtrl extends BaseCtrl {
	private static final long serialVersionUID = -8788719067123516137L;
	
	@EJB
	private ClienteServicio clienteServicio;
	
	private String criterioBusqueda;
	private File fileClientes;
	private String nombreFileCliente;
	private boolean existenNovedades;
	private boolean renderResultadoImportCliente;
	private List<Cliente> clienteList;
	private Cliente clienteSelected;
	
	public ClienteCtrl() {
	}
	
	@PostConstruct
	private void init() {
		clienteList = new ArrayList<>();
	}
	
	@Override
	public void refrescar() {
		try {
			clienteSelected = null;
			consultarClientes();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	@Override
	public void guardar() {
		
		try {
			// validaci�n 
			
			// 1. Identificacion
			if(clienteServicio.getClienteDao().existeIdentificacion(clienteSelected.getIdentificacion(), AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa())) {
				AppJsfUtil.addErrorMessage("frmCliente", "ERROR","LA IDENTIFICACI�N YA EXISTE.");
				AppJsfUtil.addErrorMessage("frmCliente:intIdentificacion","YA EXISTE.");
				return;
			}
			clienteSelected.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
			clienteSelected.setUpdated(new Date());
			clienteServicio.guardar(clienteSelected);
			
			// lista principal
			consultarClientes();
			
			AppJsfUtil.addInfoMessage("frmCliente","OK", "REGISTRO ALMACENADO CORRECTAMENTE.");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmCliente", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		
	}
	
	@Override
	public void nuevo() {
		try {
			clienteSelected = new Cliente();
			clienteSelected.setEmpresa(AppJsfUtil.getEstablecimiento().getEmpresa());
			AppJsfUtil.showModalRender("dlgCliente", "frmCliente");
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	@Override
	public void eliminar() {
		try {
			
			if(clienteSelected==null) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTE REGISTRO SELECCIONADO.");
				return;
			}
			
			clienteServicio.eliminar(clienteSelected);
			
			clienteSelected = null;
			consultarClientes();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void consultarClientes()throws DaoException{
		AppJsfUtil.limpiarFiltrosDataTable("formMain:clienteDT");
		clienteList = null;
		clienteList = clienteServicio.getClienteDao().consultarClientes(AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa(), criterioBusqueda);
	}
	
	public void verCliente(String id) {
		try {
			if(id==null) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTE REGISTRO SELECCIONADO.");
				return;
			}
			
			editarFacade(id);
			AppJsfUtil.showModalRender("dlgVerCliente", "frmVerCliente");
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void editarCliente(String id) {
		try {
			if(id==null) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTE REGISTRO SELECCIONADO.");
				return;
			}
			
			editarFacade(id);
			AppJsfUtil.showModalRender("dlgCliente", "frmCliente");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	private void editarFacade(String id) throws DaoException, IOException {
		
		clienteSelected = clienteServicio.getClienteDao().cargar(id);

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
	 * @return the fileClientes
	 */
	public File getFileClientes() {
		return fileClientes;
	}

	/**
	 * @param fileClientes the fileClientes to set
	 */
	public void setFileClientes(File fileClientes) {
		this.fileClientes = fileClientes;
	}

	/**
	 * @return the nombreFileCliente
	 */
	public String getNombreFileCliente() {
		return nombreFileCliente;
	}

	/**
	 * @param nombreFileCliente the nombreFileCliente to set
	 */
	public void setNombreFileCliente(String nombreFileCliente) {
		this.nombreFileCliente = nombreFileCliente;
	}

	/**
	 * @return the existenNovedades
	 */
	public boolean isExistenNovedades() {
		return existenNovedades;
	}

	/**
	 * @param existenNovedades the existenNovedades to set
	 */
	public void setExistenNovedades(boolean existenNovedades) {
		this.existenNovedades = existenNovedades;
	}

	/**
	 * @return the renderResultadoImportCliente
	 */
	public boolean isRenderResultadoImportCliente() {
		return renderResultadoImportCliente;
	}

	/**
	 * @param renderResultadoImportCliente the renderResultadoImportCliente to set
	 */
	public void setRenderResultadoImportCliente(boolean renderResultadoImportCliente) {
		this.renderResultadoImportCliente = renderResultadoImportCliente;
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
}