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
public class XmlDetAdicional implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3963703022164213966L;
	
	
	@XmlAttribute
	private String nombre;
	@XmlAttribute
	private String valor;
	
	/**
	 * 
	 */
	public XmlDetAdicional() {
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
