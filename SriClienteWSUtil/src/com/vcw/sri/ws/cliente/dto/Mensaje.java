/**
 * 
 */
package com.vcw.sri.ws.cliente.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author cristianvillarreal
 *
 */
@XmlRootElement(name = "mensaje")
@XmlAccessorType(XmlAccessType.FIELD)
public class Mensaje implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6912527172791263673L;
	
	@XmlElement(name = "identificador")
	private String identificador;
	@XmlElement(name = "mensaje")
    private String mensaje;
	@XmlElement(name = "informacionAdicional")
    private String informacionAdicional;
	@XmlElement(name = "tipo")
    private String tipo;

	/**
	 * 
	 */
	public Mensaje() {
	}

	/**
	 * @return the identificador
	 */
	public String getIdentificador() {
		return identificador;
	}

	/**
	 * @param identificador the identificador to set
	 */
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
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
	 * @return the informacionAdicional
	 */
	public String getInformacionAdicional() {
		return informacionAdicional;
	}

	/**
	 * @param informacionAdicional the informacionAdicional to set
	 */
	public void setInformacionAdicional(String informacionAdicional) {
		this.informacionAdicional = informacionAdicional;
	}

	/**
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}
