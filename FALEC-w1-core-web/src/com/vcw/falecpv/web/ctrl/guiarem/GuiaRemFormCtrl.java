/**
 * 
 */
package com.vcw.falecpv.web.ctrl.guiarem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Cliente;
import com.vcw.falecpv.core.modelo.persistencia.Destinatario;
import com.vcw.falecpv.core.modelo.persistencia.Detalledestinatario;
import com.vcw.falecpv.core.modelo.persistencia.Transportista;
import com.vcw.falecpv.core.servicio.ClienteServicio;
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
	
	private String callModule;
	private Cabecera guiaRemisionSelected;
	private List<Destinatario> destinatarioList;
	private Destinatario destinatarioSelected;
	private List<Detalledestinatario> detalledestinatarioList;
	private Detalledestinatario detalledestinatarioSeleted;
	private List<Transportista> transportistaList;
	
	/**
	 * 
	 */
	public GuiaRemFormCtrl() {
	}
	
	@PostConstruct
	private void init() {
		try {
			
			nuevaGuiaRemision();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
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

	@Override
	public void nuevo() {
		try {
			nuevaGuiaRemision();
			destinatarioList = new ArrayList<>();
			
			destinatarioSelected = new Destinatario();
			destinatarioSelected.setIddestinatario("M1");
			Cliente cl = clienteServicio.consultarByPk("7");
			destinatarioSelected.setCliente(cl);
			destinatarioSelected.setIdentificaciondestinatario(cl.getIdentificacion());
			destinatarioSelected.setRazonsocialdestinatario(cl.getRazonsocial());
			destinatarioSelected.setDirdestinatario("AV LA PRENSA N42-95 EDF LUBLUADA DEOP 5");
			destinatarioSelected.setMotivotraslado("TRANSFERENCIA MERCADERIA");
			destinatarioSelected.setDocaduanerounico("0041324846887");
			destinatarioSelected.setCodestabdestino("012");
			destinatarioSelected.setRuta("Quito – Cayambe - Otavalo");
			destinatarioSelected.setCoddocsustento("01");
			destinatarioSelected.setNumdocsustento("002-001-000000001");
			destinatarioSelected.setNumautdocsustento("2103202001302517921467390011234567891541236987412");
			destinatarioSelected.setFechaemisiondocsustento(new Date());
			
			
			detalledestinatarioList = new ArrayList<>();
			Detalledestinatario dd = new Detalledestinatario();
			dd.setIddetalledestinatario("M1");
			dd.setCantidad(BigDecimal.valueOf(10));
			dd.setCodigointerno("XXXX100");
			dd.setCodigoadicional("A1");
			dd.setDescripcion("KERATINA COLOR BLANCO 24x30");
			detalledestinatarioList.add(dd);
			dd = new Detalledestinatario();
			dd.setIddetalledestinatario("M2");
			dd.setCantidad(BigDecimal.valueOf(5));
			dd.setCodigointerno("YYYX100");
			dd.setCodigoadicional("A2");
			dd.setDescripcion("KERATINA COLOR ROJO 16x16");
			
			detalledestinatarioList.add(dd);
			dd = new Detalledestinatario();
			dd.setIddetalledestinatario("M3");
			dd.setCantidad(BigDecimal.valueOf(5));
			dd.setCodigointerno("YYYX100");
			dd.setCodigoadicional("A2");
			dd.setDescripcion("KERATINA COLOR ROJO 16x16");
			
			detalledestinatarioList.add(dd);
			dd = new Detalledestinatario();
			dd.setIddetalledestinatario("M4");
			dd.setCantidad(BigDecimal.valueOf(5));
			dd.setCodigointerno("YYYX100");
			dd.setCodigoadicional("A2");
			dd.setDescripcion("KERATINA COLOR ROJO 16x16");
			
			detalledestinatarioList.add(dd);
			dd = new Detalledestinatario();
			dd.setIddetalledestinatario("M5");
			dd.setCantidad(BigDecimal.valueOf(5));
			dd.setCodigointerno("YYYX100");
			dd.setCodigoadicional("A2");
			dd.setDescripcion("KERATINA COLOR ROJO 16x16");
			
			detalledestinatarioList.add(dd);
			dd = new Detalledestinatario();
			dd.setIddetalledestinatario("M6");
			dd.setCantidad(BigDecimal.valueOf(5));
			dd.setCodigointerno("YYYX100");
			dd.setCodigoadicional("A2");
			dd.setDescripcion("KERATINA COLOR ROJO 16x16");
			
			detalledestinatarioList.add(dd);
			dd = new Detalledestinatario();
			dd.setIddetalledestinatario("M7");
			dd.setCantidad(BigDecimal.valueOf(5));
			dd.setCodigointerno("YYYX100");
			dd.setCodigoadicional("A2");
			dd.setDescripcion("KERATINA COLOR ROJO 16x16");
			detalledestinatarioList.add(dd);
			
			dd = new Detalledestinatario();
			dd.setIddetalledestinatario("M8");
			dd.setCantidad(BigDecimal.valueOf(5));
			dd.setCodigointerno("YYYX100");
			dd.setCodigoadicional("A2");
			dd.setDescripcion("KERATINA COLOR ROJO 16x16");
			detalledestinatarioList.add(dd);
			
			
			
			destinatarioSelected.setDetalledestinatarioList(detalledestinatarioList);
			destinatarioList.add(destinatarioSelected);
			guiaRemisionSelected.setDestinatarioList(destinatarioList);
			
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
		des.setTotal(BigDecimal.valueOf(des.getDetalledestinatarioList().stream().mapToDouble(x->x.getCantidad().doubleValue()).sum()));
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
	 * @return the destinatarioList
	 */
	public List<Destinatario> getDestinatarioList() {
		return destinatarioList;
	}

	/**
	 * @param destinatarioList the destinatarioList to set
	 */
	public void setDestinatarioList(List<Destinatario> destinatarioList) {
		this.destinatarioList = destinatarioList;
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
	 * @return the detalledestinatarioList
	 */
	public List<Detalledestinatario> getDetalledestinatarioList() {
		return detalledestinatarioList;
	}

	/**
	 * @param detalledestinatarioList the detalledestinatarioList to set
	 */
	public void setDetalledestinatarioList(List<Detalledestinatario> detalledestinatarioList) {
		this.detalledestinatarioList = detalledestinatarioList;
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

}
