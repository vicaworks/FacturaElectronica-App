/**
 * 
 */
package com.vcw.falecpv.core.modelo.xml;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * @author cristianvillarreal
 *
 */
@XmlRootElement(name = "campoAdicional")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlCampoAdicional implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7831980738169704819L;
	
	@XmlValue
	private String value;
	
	@XmlAttribute(name = "nombre")
	private String nombre;
	
	/**
	 * 
	 */
	public XmlCampoAdicional() {
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
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

}
