/**
 * 
 */
package com.vcw.falecpv.core.modelo.xml;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author cristianvillarreal
 *
 */
@XmlRootElement(name = "detAdicional")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlDetallesAdicionales implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2869182876743359643L;

	@XmlAttribute(name = "nombre")
	private String nombre;
	
	@XmlAttribute(name = "valor")
	private String valor;
	
	
	/**
	 * 
	 */
	public XmlDetallesAdicionales() {
	}


	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}


	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	/**
	 * @return the valor
	 */
	public String getValor() {
		return valor;
	}


	/**
	 * @param valor the valor to set
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}

}
