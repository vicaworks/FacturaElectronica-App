/**
 * 
 */
package com.vcw.sri.ws.cliente.dto;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * @author cristianvillarreal
 *
 */
@XmlRootElement(name = "autorizacion")
@XmlAccessorType(XmlAccessType.FIELD)
public class Autorizacion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6425911283957668949L;
	
	
	@XmlElement(name = "estado")
	private String estado;
	@XmlElement(name = "numeroAutorizacion")
    private String numeroAutorizacion;
	
	@XmlElement(name = "fechaAutorizacion")
    @XmlSchemaType(name = "dateTime")
    private XMLGregorianCalendar fechaAutorizacion;
	
	@XmlElement(name = "ambiente")
    private String ambiente;
	@XmlElement(name = "comprobante")
    private String comprobante;
	@XmlElementRef
	@XmlElementWrapper(name = "mensajes")
    private List<Mensaje> mensajeList;

	/**
	 * 
	 */
	public Autorizacion() {
		
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
	 * @return the fechaAutorizacion
	 */
	public XMLGregorianCalendar getFechaAutorizacion() {
		return fechaAutorizacion;
	}

	/**
	 * @param fechaAutorizacion the fechaAutorizacion to set
	 */
	public void setFechaAutorizacion(XMLGregorianCalendar fechaAutorizacion) {
		this.fechaAutorizacion = fechaAutorizacion;
	}

	/**
	 * @return the ambiente
	 */
	public String getAmbiente() {
		return ambiente;
	}

	/**
	 * @param ambiente the ambiente to set
	 */
	public void setAmbiente(String ambiente) {
		this.ambiente = ambiente;
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
	 * @return the mensajeList
	 */
	public List<Mensaje> getMensajeList() {
		return mensajeList;
	}

	/**
	 * @param mensajeList the mensajeList to set
	 */
	public void setMensajeList(List<Mensaje> mensajeList) {
		this.mensajeList = mensajeList;
	}

}
