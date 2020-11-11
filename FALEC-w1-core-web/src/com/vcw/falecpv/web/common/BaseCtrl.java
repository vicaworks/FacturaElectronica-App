/**
 * 
 */
package com.vcw.falecpv.web.common;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.xml.bind.JAXBException;

import org.primefaces.event.ToggleSelectEvent;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.TextoUtil;
import com.servitec.common.util.XmlCommonsUtil;
import com.vcw.falecpv.core.constante.ComprobanteEstadoEnum;
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;
import com.vcw.falecpv.core.helper.ComprobanteHelper;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Comprobanterecibido;
import com.vcw.falecpv.core.modelo.persistencia.Detalle;
import com.vcw.falecpv.core.modelo.persistencia.Infoadicional;
import com.vcw.falecpv.core.modelo.persistencia.Tipopago;
import com.vcw.falecpv.core.modelo.xml.XmlComprobanteRetencion;
import com.vcw.falecpv.core.modelo.xml.XmlFactura;
import com.vcw.falecpv.core.modelo.xml.XmlGuiaRemision;
import com.vcw.falecpv.core.modelo.xml.XmlNotaCredito;
import com.vcw.falecpv.core.modelo.xml.XmlNotaDebito;
import com.vcw.falecpv.core.servicio.ComprobanterecibidoServicio;
import com.vcw.falecpv.core.servicio.TipopagoServicio;
import com.vcw.falecpv.web.ctrl.common.MessageCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;
import com.vcw.falecpv.web.util.MessageWebUtil;

/**
 * @author cristianvillarreal
 *
 */
public abstract class BaseCtrl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6523368854199739151L;
	@EJB
	private TipopagoServicio tipopagoServicio;
	
	@EJB
	private ComprobanterecibidoServicio comprobanterecibidoServicio;
	
	protected MessageWebUtil msg = new MessageWebUtil();
	protected String estado;
	protected List<Infoadicional> infoadicionalList;
	protected Infoadicional infoadicionalSelected;
	private List<Tipopago> tipopagoList;
	private Tipopago tipopagoSelected;
	protected MessageCtrl messageCtrl = (MessageCtrl) AppJsfUtil.getManagedBean("messageCtrl");
	protected boolean enableAccion = false;
	
	// comprobantes importados
	protected List<Comprobanterecibido> comprobanteRecibidoList;
	protected List<Comprobanterecibido> comprobanteRecibidoSeleccionList;
	protected Comprobanterecibido comprobanteRecibidoSelected;
	protected Comprobanterecibido comprobanteRecibidoTotal;
	protected String xml;
	protected RideCtrl rideCtrl;
	protected String descripcionIva;
	
	/**
	 * 
	 */
	public BaseCtrl() {
		
	}
	
	public void limpiar() {
	}
	
	public void buscar() {
	}
	
	public void refrescar() {
	}
	
	public void eliminar() {
	}
	
	public void guardar() {
	}
	
	public void editar() {
	}
	
	public void nuevo() {
	}

	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEmpresa
	 * @param genTipoDocumentoEnum
	 * @param desde
	 * @param hasta
	 * @param criterio
	 * @throws DaoException
	 */
	public void consultarComprobanteRecibido(String idEmpresa,GenTipoDocumentoEnum genTipoDocumentoEnum, Date desde,Date hasta,String criterio,boolean aplicarFechas) throws DaoException {
		comprobanteRecibidoList = null;
		comprobanteRecibidoSeleccionList = null;
		comprobanteRecibidoList = comprobanterecibidoServicio.getComprobanterecibidoDao().getByComprobanteEmpresa(idEmpresa, genTipoDocumentoEnum, desde, hasta, criterio,aplicarFechas);
	}
	
	public void totalizarComprobantesRecibidos() {
		comprobanteRecibidoTotal = new Comprobanterecibido();
		comprobanteRecibidoTotal.setTotalbaseimponible(BigDecimal.ZERO);
		comprobanteRecibidoTotal.setTotalconimpuestos(BigDecimal.ZERO);
		comprobanteRecibidoTotal.setTotaldescuento(BigDecimal.ZERO);
		comprobanteRecibidoTotal.setTotalice(BigDecimal.ZERO);
		comprobanteRecibidoTotal.setTotaliva(BigDecimal.ZERO);
		comprobanteRecibidoTotal.setTotalrenta(BigDecimal.ZERO);
		comprobanteRecibidoTotal.setTotalretencion(BigDecimal.ZERO);
		comprobanteRecibidoTotal.setTotalsinimpuestos(BigDecimal.ZERO);
		
		if(comprobanteRecibidoList!=null) {
			comprobanteRecibidoTotal.setTotalbaseimponible(BigDecimal.valueOf(comprobanteRecibidoList.stream().mapToDouble(x-> x.getTotalbaseimponible()==null?0d:x.getTotalbaseimponible().doubleValue()).sum()));
			comprobanteRecibidoTotal.setTotalconimpuestos(BigDecimal.valueOf(comprobanteRecibidoList.stream().mapToDouble(x-> x.getTotalconimpuestos()==null?0d:x.getTotalconimpuestos().doubleValue()).sum()));
			comprobanteRecibidoTotal.setTotaldescuento(BigDecimal.valueOf(comprobanteRecibidoList.stream().mapToDouble(x-> x.getTotaldescuento()==null?0d:x.getTotaldescuento().doubleValue()).sum()));
			comprobanteRecibidoTotal.setTotalice(BigDecimal.valueOf(comprobanteRecibidoList.stream().mapToDouble(x-> x.getTotalice()==null?0d:x.getTotalice().doubleValue()).sum()));
			comprobanteRecibidoTotal.setTotaliva(BigDecimal.valueOf(comprobanteRecibidoList.stream().mapToDouble(x-> x.getTotaliva()==null?0d:x.getTotaliva().doubleValue()).sum()));
			comprobanteRecibidoTotal.setTotalrenta(BigDecimal.valueOf(comprobanteRecibidoList.stream().mapToDouble(x-> x.getTotalrenta()==null?0d:x.getTotalrenta().doubleValue()).sum()));
			comprobanteRecibidoTotal.setTotalretencion(BigDecimal.valueOf(comprobanteRecibidoList.stream().mapToDouble(x-> x.getTotalretencion()==null?0d:x.getTotalretencion().doubleValue()).sum()));
			comprobanteRecibidoTotal.setTotalsinimpuestos(BigDecimal.valueOf(comprobanteRecibidoList.stream().mapToDouble(x-> x.getTotalsinimpuestos()==null?0d:x.getTotalsinimpuestos().doubleValue()).sum()));
		}
	}
	
	public void formatoValorXml() throws UnsupportedEncodingException, JAXBException {
		if(xml==null) {
			return;
		}
		switch (GenTipoDocumentoEnum.getEnumByIdentificador(comprobanteRecibidoSelected.getTipocomprobante().getIdentificador())) {
		case FACTURA:
			XmlFactura f = XmlCommonsUtil.jaxbunmarshall(xml, new XmlFactura());
			xml = XmlCommonsUtil.jaxbMarshall(f, true, false);
			break;
		case RETENCION:
			XmlComprobanteRetencion rt = XmlCommonsUtil.jaxbunmarshall(xml, new XmlComprobanteRetencion());
			xml = XmlCommonsUtil.jaxbMarshall(rt, true, false);
			break;	
		case NOTA_CREDITO:
			XmlNotaCredito nc = XmlCommonsUtil.jaxbunmarshall(xml, new XmlNotaCredito());
			xml = XmlCommonsUtil.jaxbMarshall(nc, true, false);
			break;
		case NOTA_DEBITO:
			XmlNotaDebito nd = XmlCommonsUtil.jaxbunmarshall(xml, new XmlNotaDebito());
			xml = XmlCommonsUtil.jaxbMarshall(nd, true, false);
			break;
		case GUIA_REMISION:
			XmlGuiaRemision gr = XmlCommonsUtil.jaxbunmarshall(xml, new XmlGuiaRemision());
			xml = XmlCommonsUtil.jaxbMarshall(gr, true, false);
			break;
		default:
			break;
		}
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
	
	public void habilitarCrud(String estado) {
		enableAccion = false;
		ComprobanteEstadoEnum estadoEnum = ComprobanteEstadoEnum.getByEstado(estado);
		List<ComprobanteEstadoEnum> lista = Arrays.asList(new ComprobanteEstadoEnum[] {ComprobanteEstadoEnum.BORRADOR,ComprobanteEstadoEnum.ERROR,ComprobanteEstadoEnum.ERROR_SRI,ComprobanteEstadoEnum.REGISTRADO});
		enableAccion = !lista.contains(estadoEnum);
	}
	
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param cabecera
	 */
	protected void inicializarSecuencia(Cabecera cabecera) {
		cabecera.setEditarSecuencial(false);
		cabecera.setSecuencialEstablecimiento(TextoUtil.leftPadTexto(AppJsfUtil.getEstablecimiento().getCodigoestablecimiento(),3,"0"));
		cabecera.setSecuencialCaja(TextoUtil.leftPadTexto(AppJsfUtil.getEstablecimiento().getPuntoemision(),3,"0"));
		cabecera.setSecuencialNumero(TextoUtil.leftPadTexto("0",9,"0"));
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param cabecera
	 */
	protected void editarSecuencial(Cabecera cabecera,String focus) {
			
	   cabecera.setEditarSecuencial(true);
	   messageCtrl.cargarMenssage("IMPORTANTE", msg.getString("mensaje.editarsecuencial"), "WARNING");     
	        
			
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param cabecera
	 */
	protected void noEditarSecuencial(Cabecera cabecera) {
			
		cabecera.setEditarSecuencial(false);
		cabecera.setSecuencialNumero(TextoUtil.leftPadTexto("0", 9, "0"));
			
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param numDoc
	 * @return
	 */
	public String getFormatoNumDocumento(String numDoc) {
		return ComprobanteHelper.formatNumDocumento(numDoc);
	}
	
	public void agregarInfoAdicional(){
		try {
			
			if(infoadicionalList==null) {
				infoadicionalList = new ArrayList<>();
			}
			
			Infoadicional info = new Infoadicional();
			infoadicionalList.add(info);
			
			int cont = 0;
			for (Infoadicional infoadicional : infoadicionalList) {
				infoadicional.setIdinfoadicional("M"+cont++);
			}
			
			AppJsfUtil.executeJavaScript("PrimeFaces.focus('formMain:j_idt181:"  + (infoadicionalList.size()-1) + ":inpInfoAdicionalNombre')");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void eliminarInfoAdicional() {
		try {
			if(infoadicionalList!=null && infoadicionalSelected!=null) {
				infoadicionalList.remove(infoadicionalSelected);
			}
			
			infoadicionalSelected = null;
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void populateTipoPago() throws DaoException {
		tipopagoList = null;
		tipopagoList = tipopagoServicio.getALL();
		tipopagoSelected = null;
		if(tipopagoList.size()>0) {
			tipopagoSelected = tipopagoList.get(0);
		}
	}
	
	public void cambioEstadoBorrador(Cabecera cabecera){
		if(cabecera.isBorrador()) {
			
		}else {
			inicializarSecuencia(cabecera);
			cabecera.setSecuencial(null);
		}
	}
	
	public void selectAllComprobantesRecibidos(ToggleSelectEvent event) {
		if (comprobanteRecibidoSeleccionList != null) {
			comprobanteRecibidoSeleccionList.clear();
			if (event.isSelected()) {
				comprobanteRecibidoSeleccionList.addAll(comprobanteRecibidoList); // Add all the elements from getSomeList()
			}
		}
	}
	
	public void determinarDescripcionIVA(List<Detalle> detalleList) {
		descripcionIva = "(0%)";
		
		if(detalleList==null || detalleList.isEmpty()) return;
		
		Detalle d = detalleList.stream().filter(x->x.getValoriva().doubleValue()>0).findFirst().orElse(null);
		if(d==null) {
			descripcionIva = "(0%)";
		}else {
			descripcionIva = "(" + d.getIva().getPorcentaje() + ")";
		}
	}
	
	/**
	 * @return the infoadicionalSelected
	 */
	public Infoadicional getInfoadicionalSelected() {
		return infoadicionalSelected;
	}

	/**
	 * @param infoadicionalSelected the infoadicionalSelected to set
	 */
	public void setInfoadicionalSelected(Infoadicional infoadicionalSelected) {
		this.infoadicionalSelected = infoadicionalSelected;
	}

	/**
	 * @return the infoadicionalList
	 */
	public List<Infoadicional> getInfoadicionalList() {
		return infoadicionalList;
	}

	/**
	 * @param infoadicionalList the infoadicionalList to set
	 */
	public void setInfoadicionalList(List<Infoadicional> infoadicionalList) {
		this.infoadicionalList = infoadicionalList;
	}

	/**
	 * @return the tipopagoList
	 */
	public List<Tipopago> getTipopagoList() {
		return tipopagoList;
	}

	/**
	 * @param tipopagoList the tipopagoList to set
	 */
	public void setTipopagoList(List<Tipopago> tipopagoList) {
		this.tipopagoList = tipopagoList;
	}

	/**
	 * @return the tipopagoSelected
	 */
	public Tipopago getTipopagoSelected() {
		return tipopagoSelected;
	}

	/**
	 * @param tipopagoSelected the tipopagoSelected to set
	 */
	public void setTipopagoSelected(Tipopago tipopagoSelected) {
		this.tipopagoSelected = tipopagoSelected;
	}

	/**
	 * @return the tipopagoServicio
	 */
	public TipopagoServicio getTipopagoServicio() {
		return tipopagoServicio;
	}

	/**
	 * @param tipopagoServicio the tipopagoServicio to set
	 */
	public void setTipopagoServicio(TipopagoServicio tipopagoServicio) {
		this.tipopagoServicio = tipopagoServicio;
	}

	/**
	 * @return the msg
	 */
	public MessageWebUtil getMsg() {
		return msg;
	}

	/**
	 * @param msg the msg to set
	 */
	public void setMsg(MessageWebUtil msg) {
		this.msg = msg;
	}

	/**
	 * @return the messageCtrl
	 */
	public MessageCtrl getMessageCtrl() {
		return messageCtrl;
	}

	/**
	 * @param messageCtrl the messageCtrl to set
	 */
	public void setMessageCtrl(MessageCtrl messageCtrl) {
		this.messageCtrl = messageCtrl;
	}

	/**
	 * @return the enableAccion
	 */
	public boolean isEnableAccion() {
		return enableAccion;
	}

	/**
	 * @param enableAccion the enableAccion to set
	 */
	public void setEnableAccion(boolean enableAccion) {
		this.enableAccion = enableAccion;
	}

	/**
	 * @return the comprobanterecibidoServicio
	 */
	public ComprobanterecibidoServicio getComprobanterecibidoServicio() {
		return comprobanterecibidoServicio;
	}

	/**
	 * @param comprobanterecibidoServicio the comprobanterecibidoServicio to set
	 */
	public void setComprobanterecibidoServicio(ComprobanterecibidoServicio comprobanterecibidoServicio) {
		this.comprobanterecibidoServicio = comprobanterecibidoServicio;
	}

	/**
	 * @return the comprobanteRecibidoList
	 */
	public List<Comprobanterecibido> getComprobanteRecibidoList() {
		return comprobanteRecibidoList;
	}

	/**
	 * @param comprobanteRecibidoList the comprobanteRecibidoList to set
	 */
	public void setComprobanteRecibidoList(List<Comprobanterecibido> comprobanteRecibidoList) {
		this.comprobanteRecibidoList = comprobanteRecibidoList;
	}

	/**
	 * @return the comprobanteRecibidoSeleccionList
	 */
	public List<Comprobanterecibido> getComprobanteRecibidoSeleccionList() {
		return comprobanteRecibidoSeleccionList;
	}

	/**
	 * @param comprobanteRecibidoSeleccionList the comprobanteRecibidoSeleccionList to set
	 */
	public void setComprobanteRecibidoSeleccionList(List<Comprobanterecibido> comprobanteRecibidoSeleccionList) {
		this.comprobanteRecibidoSeleccionList = comprobanteRecibidoSeleccionList;
	}

	/**
	 * @return the comprobanteRecibidoSelected
	 */
	public Comprobanterecibido getComprobanteRecibidoSelected() {
		return comprobanteRecibidoSelected;
	}

	/**
	 * @param comprobanteRecibidoSelected the comprobanteRecibidoSelected to set
	 */
	public void setComprobanteRecibidoSelected(Comprobanterecibido comprobanteRecibidoSelected) {
		this.comprobanteRecibidoSelected = comprobanteRecibidoSelected;
	}

	/**
	 * @return the comprobanteRecibidoTotal
	 */
	public Comprobanterecibido getComprobanteRecibidoTotal() {
		return comprobanteRecibidoTotal;
	}

	/**
	 * @param comprobanteRecibidoTotal the comprobanteRecibidoTotal to set
	 */
	public void setComprobanteRecibidoTotal(Comprobanterecibido comprobanteRecibidoTotal) {
		this.comprobanteRecibidoTotal = comprobanteRecibidoTotal;
	}

	/**
	 * @return the xml
	 */
	public String getXml() {
		return xml;
	}

	/**
	 * @param xml the xml to set
	 */
	public void setXml(String xml) {
		this.xml = xml;
	}

	/**
	 * @return the rideCtrl
	 */
	public RideCtrl getRideCtrl() {
		return rideCtrl;
	}

	/**
	 * @param rideCtrl the rideCtrl to set
	 */
	public void setRideCtrl(RideCtrl rideCtrl) {
		this.rideCtrl = rideCtrl;
	}

	/**
	 * @return the descripcionIva
	 */
	public String getDescripcionIva() {
		return descripcionIva;
	}

	/**
	 * @param descripcionIva the descripcionIva to set
	 */
	public void setDescripcionIva(String descripcionIva) {
		this.descripcionIva = descripcionIva;
	}
	
	
}
