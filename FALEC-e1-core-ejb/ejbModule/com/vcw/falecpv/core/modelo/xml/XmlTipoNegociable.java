/**
 * 
 */
package com.vcw.falecpv.core.modelo.xml;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author cristianvillarreal
 *
 */
@XmlRootElement(name = "tipoNegociable")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlTipoNegociable implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4191439653003200346L;

	@XmlElement
	private String correo;
	
	/**
	 * 
	 */
	public XmlTipoNegociable() {
	}

	/**
	 * @return the correo
	 */
	public String getCorreo() {
		return correo;
	}

	/**
	 * @param correo the correo to set
	 */
	public void setCorreo(String correo) {
		this.correo = correo;
	}

}
