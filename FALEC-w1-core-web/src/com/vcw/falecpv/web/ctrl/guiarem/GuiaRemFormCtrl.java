/**
 * 
 */
package com.vcw.falecpv.web.ctrl.guiarem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
import com.vcw.falecpv.core.constante.contadores.TipoComprobanteEnum;
import com.vcw.falecpv.core.helper.ComprobanteHelper;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Destinatario;
import com.vcw.falecpv.core.modelo.persistencia.Detalle;
import com.vcw.falecpv.core.modelo.persistencia.Detalledestinatario;
import com.vcw.falecpv.core.modelo.persistencia.Producto;
import com.vcw.falecpv.core.modelo.persistencia.Tipocomprobante;
import com.vcw.falecpv.core.modelo.persistencia.Transportista;
import com.vcw.falecpv.core.modelo.query.ResumenCabeceraQuery;
import com.vcw.falecpv.core.servicio.CabeceraServicio;
import com.vcw.falecpv.core.servicio.ClienteServicio;
import com.vcw.falecpv.core.servicio.DetalleServicio;
import com.vcw.falecpv.core.servicio.TipocomprobanteServicio;
import com.vcw.falecpv.core.servicio.TransportistaServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@SessionScoped
public class GuiaRemFormCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5257443755564085346L;
	
	@EJB
	private TransportistaServicio transportistaServicio;
	
	@EJB
	private ClienteServicio clienteServicio;
	
	@EJB
	private TipocomprobanteServicio tipocomprobanteServicio;
	
	@EJB
	private CabeceraServicio cabeceraServicio;
	
	@EJB
	private DetalleServicio detalleServicio;
	
	private String callModule;
	private Cabecera guiaRemisionSelected;
	private Destinatario destinatarioSelected;
	private Detalledestinatario detalledestinatarioSeleted;
	private List<Transportista> transportistaList;
	private List<Tipocomprobante> tipocomprobanteList;
	private String criterioCliente;
	
	/**
	 * 
	 */
	public GuiaRemFormCtrl() {
	}
	
	@PostConstruct
	private void init() {
		try {
			
			nuevaGuiaRemision();
			consultarTipoComprobante();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void consultarTipoComprobante()throws DaoException{
		setTipocomprobanteList(tipocomprobanteServicio.getTipocomprobanteDao()
				.getByEmpresaFormulario(AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa(),
						TipoComprobanteEnum.GUIA_REMISION));
	}
	
	public void consultarTransportista()throws DaoException{
		transportistaList = null;
		transportistaList = transportistaServicio.getTransportistaDao().getByEstado(EstadoRegistroEnum.ACTIVO, AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa());
	}
	
	public void nuevaGuiaRemision() throws DaoException {
		consultarTransportista();
		guiaRemisionSelected = new Cabecera();
		guiaRemisionSelected.setDireccionpartida(AppJsfUtil.getEstablecimiento().getDireccionestablecimiento());
		guiaRemisionSelected.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
		guiaRemisionSelected.setFechainiciotransporte(new Date());
		guiaRemisionSelected.setFechafintransporte(new Date());
		guiaRemisionSelected.setDestinatarioList(new ArrayList<>());
	}
	
	public void cambioTransportista() {
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		
	}
	
	@Override
	public void guardar() {
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void guardarDestinatario() {
		try {
			
			if(destinatarioSelected.getDetalledestinatarioList()==null) {
				destinatarioSelected.setDetalledestinatarioList(new ArrayList<>());
			}
			
			if(destinatarioSelected.isConsultarDetalleFactura()) {
				destinatarioSelected.setDetalledestinatarioList(new ArrayList<>());
				List<Detalle> lista = detalleServicio.getDetalleDao().getByIdCabecera(destinatarioSelected.getIdVenta());
				for (Detalle d : lista) {
					if(d.getProducto()!=null) {
						Detalledestinatario dd = new Detalledestinatario();
						dd.setIddetalledestinatario("M");
						dd.setCodigointerno(d.getProducto().getCodigoprincipal());
						dd.setCodigoadicional(d.getProducto().getIdproducto());
						dd.setDescripcion(d.getDescripcion());
						dd.setCantidad(d.getCantidad());
						destinatarioSelected.getDetalledestinatarioList().add(dd);
					}
				}
			}
			destinatarioSelected.setConsultarDetalleFactura(false);
			if(destinatarioSelected.getIddestinatario()==null) {
				destinatarioSelected.setIddestinatario("M");
				guiaRemisionSelected.getDestinatarioList().add(0,destinatarioSelected);
			}
			totalizarGuiaRemision();
			AppJsfUtil.hideModal("dlgDestinatario");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmDestinatario", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	@Override
	public void nuevo() {
		try {
			nuevaGuiaRemision();
			totalizarGuiaRemision();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void totalizarGuiaRemision() {
		if(guiaRemisionSelected.getDestinatarioList()==null || guiaRemisionSelected.getDestinatarioList().isEmpty()) {
			guiaRemisionSelected.setTotal(BigDecimal.ZERO);
			return;
		}
		int cont = 0;
		for (Destinatario destinatario : guiaRemisionSelected.getDestinatarioList()) {
			if(destinatario.getIddestinatario().contains("M")) {
				destinatario.setIddestinatario("M" + (cont++));
			}
			
		}
		guiaRemisionSelected.getDestinatarioList().stream().forEach(x->{
			totalizarGuiaRemisionDestinatario(x);
		});
		guiaRemisionSelected.setTotal(BigDecimal.valueOf(guiaRemisionSelected.getDestinatarioList().stream().mapToDouble(x->x.getTotal().doubleValue()).sum()));
		
	}
	
	private void totalizarGuiaRemisionDestinatario(Destinatario des) {
		if(des.getDetalledestinatarioList()==null || des.getDetalledestinatarioList().isEmpty()) {
			des.setTotal(BigDecimal.ZERO);
			return;
		}
		int cont = 0;
		for (Detalledestinatario d : des.getDetalledestinatarioList()) {
			if(d.getIddetalledestinatario().contains("M")) {
				d.setIddetalledestinatario("M" + (cont++));
			}
		}
		des.setTotal(BigDecimal.valueOf(des.getDetalledestinatarioList().stream().mapToDouble(x->x.getCantidad().doubleValue()).sum()));
	}
	
	public void nuevoDestinatario() {
		try {
			
			destinatarioSelected = new Destinatario();
			criterioCliente = null;
			AppJsfUtil.showModalRender("dlgDestinatario", "frmDestinatario");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void buscarCliente() {
		try {
			
			if(criterioCliente==null || criterioCliente.trim().length()==0) {
				AppJsfUtil.addErrorMessage("frmDestinatario:inpCriterioCliente", "ERROR", "REQUERIDO");
				return;
			}
			
			consultarCliente(criterioCliente);
			if(destinatarioSelected.getCliente()!=null) {
				destinatarioSelected.setIdentificaciondestinatario(destinatarioSelected.getCliente().getIdentificacion());
				destinatarioSelected.setRazonsocialdestinatario(destinatarioSelected.getCliente().getRazonsocial());
			}else {
				destinatarioSelected.setIdentificaciondestinatario(null);
				destinatarioSelected.setRazonsocialdestinatario(null);
				AppJsfUtil.addErrorMessage("frmDestinatario:inpCriterioCliente", "ERROR", "NO EXISTE");
				return;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void consultarCliente(String identificador) throws DaoException {
		destinatarioSelected.setCliente(null);
		destinatarioSelected.setCliente(clienteServicio.getClienteDao().getByIdentificador(identificador,
				AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa()));
	}
	
	public String asignarFactura(ResumenCabeceraQuery resumenCabeceraQuery)throws DaoException {
		
		if(guiaRemisionSelected.getDestinatarioList()==null) {
			guiaRemisionSelected.setDestinatarioList(new ArrayList<>());
		}
		
		Cabecera c = cabeceraServicio.consultarByPk(resumenCabeceraQuery.getIdcabecera());
		// existe ya una factura igual
		if(guiaRemisionSelected.getDestinatarioList().stream().filter(x->x.getNumdocsustento().replace("-", "").equals(c.getNumdocumento())).collect(Collectors.toList()).size()>0) {
			return "LA FACTURA : " + c.getNumdocumento() + " YA EXISTE, DESTINATARIO : " + c.getCliente().getRazonsocial();
		}
		
		destinatarioSelected = new Destinatario();
//		destinatarioSelected.setIddestinatario("M");		
		destinatarioSelected.setCliente(c.getCliente());
		destinatarioSelected.setIdentificaciondestinatario(c.getCliente().getIdentificacion());
		destinatarioSelected.setRazonsocialdestinatario(c.getCliente().getRazonsocial());
		destinatarioSelected.setDirdestinatario(c.getCliente().getDireccion());
		destinatarioSelected.setMotivotraslado(null);
		destinatarioSelected.setDocaduanerounico(null);
		destinatarioSelected.setCodestabdestino(null);
		destinatarioSelected.setRuta(null);
		destinatarioSelected.setCoddocsustento(c.getTipocomprobante().getIdentificador());
		destinatarioSelected.setTipocomprobante(c.getTipocomprobante());
		destinatarioSelected.setNumdocsustento(ComprobanteHelper.formatNumDocumento(c.getNumdocumento()));
		destinatarioSelected.setNumautdocsustento(c.getNumeroautorizacion());
		destinatarioSelected.setFechaemisiondocsustento(c.getFechaemision());
		destinatarioSelected.setDetalledestinatarioList(new ArrayList<>());
		destinatarioSelected.setIdVenta(c.getIdcabecera());
		destinatarioSelected.setConsultarDetalleFactura(true);
		totalizarGuiaRemision();
		
		return null;
	}
	
	public void editarDestinatario() {
		try {
			
			AppJsfUtil.showModalRender("dlgDestinatario", "frmDestinatario");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void eliminarDestinatario() {
		try {
			
			if(destinatarioSelected!=null) {
				guiaRemisionSelected.getDestinatarioList().remove(destinatarioSelected);
			}
			
			destinatarioSelected=null;
			totalizarGuiaRemision();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void eliminarDetalleDestinatario() {
		try {
			
			if(destinatarioSelected==null || detalledestinatarioSeleted==null) {
				return;
			}
			
			destinatarioSelected.getDetalledestinatarioList().remove(detalledestinatarioSeleted);
			detalledestinatarioSeleted = null;
			totalizarGuiaRemision();
			
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public String agregarProducto(Producto producto) {
		if(destinatarioSelected==null)
			return null;
		
		if(destinatarioSelected.getDetalledestinatarioList()==null) {
			destinatarioSelected.setDetalledestinatarioList(new ArrayList<>());
		}
		
		detalledestinatarioSeleted = destinatarioSelected.getDetalledestinatarioList().stream().filter(x->x.getCodigoadicional().equals(producto.getIdproducto())).findFirst().orElse(null);
		if(detalledestinatarioSeleted==null) {
			detalledestinatarioSeleted = new Detalledestinatario();
			detalledestinatarioSeleted.setCodigointerno(producto.getCodigoprincipal());
			detalledestinatarioSeleted.setCodigoadicional(producto.getIdproducto());
			detalledestinatarioSeleted.setDescripcion(producto.getNombregenerico());
			detalledestinatarioSeleted.setIddetalledestinatario("M");
			detalledestinatarioSeleted.setCantidad(BigDecimal.valueOf(producto.getCantidad()));
			destinatarioSelected.getDetalledestinatarioList().add(detalledestinatarioSeleted);
		}else {
			detalledestinatarioSeleted.setCantidad(
			detalledestinatarioSeleted.getCantidad().add(BigDecimal.valueOf(producto.getCantidad())));
		}
		totalizarGuiaRemision();
		return null;
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
	 * @return the guiaRemisionSelected
	 */
	public Cabecera getGuiaRemisionSelected() {
		return guiaRemisionSelected;
	}

	/**
	 * @param guiaRemisionSelected the guiaRemisionSelected to set
	 */
	public void setGuiaRemisionSelected(Cabecera guiaRemisionSelected) {
		this.guiaRemisionSelected = guiaRemisionSelected;
	}

	/**
	 * @return the destinatarioSelected
	 */
	public Destinatario getDestinatarioSelected() {
		return destinatarioSelected;
	}

	/**
	 * @param destinatarioSelected the destinatarioSelected to set
	 */
	public void setDestinatarioSelected(Destinatario destinatarioSelected) {
		this.destinatarioSelected = destinatarioSelected;
	}

	/**
	 * @return the detalledestinatarioSeleted
	 */
	public Detalledestinatario getDetalledestinatarioSeleted() {
		return detalledestinatarioSeleted;
	}

	/**
	 * @param detalledestinatarioSeleted the detalledestinatarioSeleted to set
	 */
	public void setDetalledestinatarioSeleted(Detalledestinatario detalledestinatarioSeleted) {
		this.detalledestinatarioSeleted = detalledestinatarioSeleted;
	}

	/**
	 * @return the transportistaList
	 */
	public List<Transportista> getTransportistaList() {
		return transportistaList;
	}

	/**
	 * @param transportistaList the transportistaList to set
	 */
	public void setTransportistaList(List<Transportista> transportistaList) {
		this.transportistaList = transportistaList;
	}

	/**
	 * @return the tipocomprobanteList
	 */
	public List<Tipocomprobante> getTipocomprobanteList() {
		return tipocomprobanteList;
	}

	/**
	 * @param tipocomprobanteList the tipocomprobanteList to set
	 */
	public void setTipocomprobanteList(List<Tipocomprobante> tipocomprobanteList) {
		this.tipocomprobanteList = tipocomprobanteList;
	}

	/**
	 * @return the criterioCliente
	 */
	public String getCriterioCliente() {
		return criterioCliente;
	}

	/**
	 * @param criterioCliente the criterioCliente to set
	 */
	public void setCriterioCliente(String criterioCliente) {
		this.criterioCliente = criterioCliente;
	}

}
