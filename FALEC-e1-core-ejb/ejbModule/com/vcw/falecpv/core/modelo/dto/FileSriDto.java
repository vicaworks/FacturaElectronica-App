/**
 * 
 */
package com.vcw.falecpv.core.modelo.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.servitec.common.util.PojoUtil;
import com.vcw.falecpv.core.constante.ImportComprobanteEnum;

/**
 * @author cristianvillarreal
 *
 */
public class FileSriDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4239358758695217316L;
	
	private Integer id;
	private String comprobante;
	private String serieComprobante;
	private String rucEmisor;
	private String emisor;
	private Date fechaEmision;
	private Date fechaAutorizacion;
	private String tipoEmision;
	private String identificacionReceptor;
	private String claveAcceso;
	private String numeroAutorizacion;
	private BigDecimal importeTotal;
	private ImportComprobanteEnum estado;
	private String mensaje;
	private boolean registrado;
	private boolean validacion = false;
	private byte[] xml;
	private String xmlNombre;

	/**
	 * 
	 */
	public FileSriDto() {
	}
	
	public String getStyleEstado() {
		String style = "";
    	switch (estado) {
		case ACTUALIZADO:
			style = "bg-green-200";
			break;
		case PENDIENTE:
			style = "bg-yellow-200";
			break;	
		case REGISTRADO:
			style = "bg-green-200";
			break;
		default:
			style = "bg-pink-200";
			break;
		}
    	
    	return style;
    }
	
	public boolean getDesplegarInfo() {
		boolean info = false;
    	switch (estado) {
		case ACTUALIZADO:
			info = false;
			break;
		case PENDIENTE:
			info = false;
			break;	
		case REGISTRADO:
			info = false;
			break;
		default:
			info = true;
			break;
		}
    	
    	return info;
    }

	
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the comprobante
	 */
	public String getComprobante() {
		return comprobante;
	}

	/**
	 * @param comprobante the comprobante to set
	 */
	public void setComprobante(String comprobante) {
		this.comprobante = comprobante;
	}

	/**
	 * @return the serieComprobante
	 */
	public String getSerieComprobante() {
		return serieComprobante;
	}

	/**
	 * @param serieComprobante the serieComprobante to set
	 */
	public void setSerieComprobante(String serieComprobante) {
		this.serieComprobante = serieComprobante;
	}

	/**
	 * @return the rucEmisor
	 */
	public String getRucEmisor() {
		return rucEmisor;
	}

	/**
	 * @param rucEmisor the rucEmisor to set
	 */
	public void setRucEmisor(String rucEmisor) {
		this.rucEmisor = rucEmisor;
	}

	/**
	 * @return the emisor
	 */
	public String getEmisor() {
		return emisor;
	}

	/**
	 * @param emisor the emisor to set
	 */
	public void setEmisor(String emisor) {
		this.emisor = emisor;
	}

	/**
	 * @return the fechaEmision
	 */
	public Date getFechaEmision() {
		return fechaEmision;
	}

	/**
	 * @param fechaEmision the fechaEmision to set
	 */
	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	/**
	 * @return the fechaAutorizacion
	 */
	public Date getFechaAutorizacion() {
		return fechaAutorizacion;
	}

	/**
	 * @param fechaAutorizacion the fechaAutorizacion to set
	 */
	public void setFechaAutorizacion(Date fechaAutorizacion) {
		this.fechaAutorizacion = fechaAutorizacion;
	}

	/**
	 * @return the tipoEmision
	 */
	public String getTipoEmision() {
		return tipoEmision;
	}

	/**
	 * @param tipoEmision the tipoEmision to set
	 */
	public void setTipoEmision(String tipoEmision) {
		this.tipoEmision = tipoEmision;
	}

	/**
	 * @return the identificacionReceptor
	 */
	public String getIdentificacionReceptor() {
		return identificacionReceptor;
	}

	/**
	 * @param identificacionReceptor the identificacionReceptor to set
	 */
	public void setIdentificacionReceptor(String identificacionReceptor) {
		this.identificacionReceptor = identificacionReceptor;
	}

	/**
	 * @return the claveAcceso
	 */
	public String getClaveAcceso() {
		return claveAcceso;
	}

	/**
	 * @param claveAcceso the claveAcceso to set
	 */
	public void setClaveAcceso(String claveAcceso) {
		this.claveAcceso = claveAcceso;
	}

	/**
	 * @return the numeroAutorizacion
	 */
	public String getNumeroAutorizacion() {
		return numeroAutorizacion;
	}

	/**
	 * @param numeroAutorizacion the numeroAutorizacion to set
	 */
	public void setNumeroAutorizacion(String numeroAutorizacion) {
		this.numeroAutorizacion = numeroAutorizacion;
	}

	/**
	 * @return the importeTotal
	 */
	public BigDecimal getImporteTotal() {
		return importeTotal;
	}

	/**
	 * @param importeTotal the importeTotal to set
	 */
	public void setImporteTotal(BigDecimal importeTotal) {
		this.importeTotal = importeTotal;
	}

	@Override
	public String toString() {
		return PojoUtil.toString(this);
	}

	/**
	 * @return the mensaje
	 */
	public String getMensaje() {
		return mensaje;
	}

	/**
	 * @param mensaje the mensaje to set
	 */
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * @return the registrado
	 */
	public boolean isRegistrado() {
		return registrado;
	}

	/**
	 * @param registrado the registrado to set
	 */
	public void setRegistrado(boolean registrado) {
		this.registrado = registrado;
	}

	/**
	 * @return the estado
	 */
	public ImportComprobanteEnum getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(ImportComprobanteEnum estado) {
		this.estado = estado;
	}

	/**
	 * @return the validacion
	 */
	public boolean isValidacion() {
		return validacion;
	}

	/**
	 * @param validacion the validacion to set
	 */
	public void setValidacion(boolean validacion) {
		this.validacion = validacion;
	}

	/**
	 * @return the xml
	 */
	public byte[] getXml() {
		return xml;
	}

	/**
	 * @param xml the xml to set
	 */
	public void setXml(byte[] xml) {
		this.xml = xml;
	}

	/**
	 * @return the xmlNombre
	 */
	public String getXmlNombre() {
		return xmlNombre;
	}

	/**
	 * @param xmlNombre the xmlNombre to set
	 */
	public void setXmlNombre(String xmlNombre) {
		this.xmlNombre = xmlNombre;
	}
	
	


}
