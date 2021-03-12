/**
 * 
 */
package com.vcw.falecpv.web.ctrl.proforma;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;
import com.vcw.falecpv.core.modelo.persistencia.Cliente;
import com.vcw.falecpv.core.modelo.persistencia.Tareacabecera;
import com.vcw.falecpv.core.modelo.persistencia.Usuario;
import com.vcw.falecpv.core.servicio.CabeceraServicio;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.falecpv.core.servicio.TareacabeceraServicio;
import com.vcw.falecpv.core.servicio.UsuarioServicio;
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
	
	@EJB
	private CabeceraServicio cabeceraServicio;
	
	@EJB
	private TareacabeceraServicio tareacabeceraServicio;
	
	@EJB
	private UsuarioServicio usuarioServicio;
	
	private List<Tareacabecera> tareacabeceraList;
	private Tareacabecera tareacabeceraSelected;
	private String estadoSeleccion = "PENDIENTE-VENCIDO";
	private String prioridadSeleccion;
	private List<Usuario> usuarioList;
	private Usuario usuarioSeleccion;
	private List<Cliente> clienteList;
	private Cliente clienteSeleccion;
	private String numCotizacion;

	/**
	 * 
	 */
	public TareaCotMainCtrl() {
	}
	
	@PostConstruct
	private void init() {
		try {
			establecimientoFacade(establecimientoServicio, false);
			consultarUsuario();
			consultarCliente();
			consultar();
			tareacabeceraServicio.getTareacabeceraDao().actualizarEstadoVencido(AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa());
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	public void consultarUsuario() throws DaoException{
		usuarioList = usuarioServicio.getUsuarioDao().getByEmpresaEstado(EstadoRegistroEnum.ACTIVO, establecimientoMain.getEmpresa().getIdempresa());
	}
	
	public void consultarCliente() throws DaoException{
		clienteList = cabeceraServicio.getClienteByTipoComprobante(establecimientoMain.getEmpresa().getIdempresa(), GenTipoDocumentoEnum.COTIZACION);
	}
	
	@Override
	public void buscar() {
		try {
			
			consultar();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void consultar() throws DaoException {
		
		AppJsfUtil.limpiarFiltrosDataTable("fomrMain:tarea:pvDosDT");
		
		List<String> estadoList = null;
		
		if(estadoSeleccion!=null) {
			estadoList = new ArrayList<>();
			switch (estadoSeleccion) {
			case "PENDIENTE-VENCIDO":
				estadoList.add("PENDIENTE");
				estadoList.add("VENCIDO");
				break;

			default:
				estadoList.add(estadoSeleccion);
				break;
			}
		}
		
		List<Integer> prioridadList = null;
		
		if(prioridadSeleccion!=null) {
			prioridadList = new ArrayList<>();
			switch (prioridadSeleccion) {
			case "IMPORTANTE":
				prioridadList.add(1);
				break;
			case "IMPORTANTE-ALTA":
				prioridadList.add(1);
				prioridadList.add(2);
				break;
			case "ALTA":
				prioridadList.add(2);
				break;
			case "MEDIA":
				prioridadList.add(3);
				break;
			case "BAJA":
				prioridadList.add(4);
				break;
			default:
				break;
			}
		}
		
		
		List<String> idClienteList = null;
		if(clienteSeleccion!=null) {
			idClienteList = new ArrayList<>();
			idClienteList.add(clienteSeleccion.getIdcliente());
		}else {
			idClienteList = cabeceraServicio
					.getClienteByTipoComprobante(establecimientoMain.getEmpresa().getIdempresa(),
							GenTipoDocumentoEnum.COTIZACION)
					.stream().map(x -> x.getIdcliente()).collect(Collectors.toList());
		}
		
		
		tareacabeceraList =	tareacabeceraServicio.getTareacabeceraDao().consultarByOpciones(AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa(),
								establecimientoMain==null?null:establecimientoMain.getIdestablecimiento(), estadoList, prioridadList,
								usuarioSeleccion == null ? null : usuarioSeleccion.getIdusuario(), idClienteList, numCotizacion,
								GenTipoDocumentoEnum.COTIZACION);
		
		
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

	/**
	 * @return the estadoSeleccion
	 */
	public String getEstadoSeleccion() {
		return estadoSeleccion;
	}

	/**
	 * @param estadoSeleccion the estadoSeleccion to set
	 */
	public void setEstadoSeleccion(String estadoSeleccion) {
		this.estadoSeleccion = estadoSeleccion;
	}

	/**
	 * @return the prioridadSeleccion
	 */
	public String getPrioridadSeleccion() {
		return prioridadSeleccion;
	}

	/**
	 * @param prioridadSeleccion the prioridadSeleccion to set
	 */
	public void setPrioridadSeleccion(String prioridadSeleccion) {
		this.prioridadSeleccion = prioridadSeleccion;
	}

	/**
	 * @return the usuarioList
	 */
	public List<Usuario> getUsuarioList() {
		return usuarioList;
	}

	/**
	 * @param usuarioList the usuarioList to set
	 */
	public void setUsuarioList(List<Usuario> usuarioList) {
		this.usuarioList = usuarioList;
	}

	/**
	 * @return the usuarioSeleccion
	 */
	public Usuario getUsuarioSeleccion() {
		return usuarioSeleccion;
	}

	/**
	 * @param usuarioSeleccion the usuarioSeleccion to set
	 */
	public void setUsuarioSeleccion(Usuario usuarioSeleccion) {
		this.usuarioSeleccion = usuarioSeleccion;
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
	 * @return the clienteSeleccion
	 */
	public Cliente getClienteSeleccion() {
		return clienteSeleccion;
	}

	/**
	 * @param clienteSeleccion the clienteSeleccion to set
	 */
	public void setClienteSeleccion(Cliente clienteSeleccion) {
		this.clienteSeleccion = clienteSeleccion;
	}

	/**
	 * @return the numCotizacion
	 */
	public String getNumCotizacion() {
		return numCotizacion;
	}

	/**
	 * @param numCotizacion the numCotizacion to set
	 */
	public void setNumCotizacion(String numCotizacion) {
		this.numCotizacion = numCotizacion;
	}

}
