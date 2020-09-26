/**
 * 
 */
package com.vcw.falecpv.core.modelo.xml;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.vcw.falecpv.core.modelo.xml.adapter.XmlAdapterSriNumero;

/**
 * @author cristianvillarreal
 *
 */
@XmlRootElement(name = "motivo")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlMotivo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3555846678791282039L;

	@XmlElement
	private String razon;
	@XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero.class)
	private Double valor;
	
	/**
	 * 
	 */
	public XmlMotivo() {
	}

	/**
	 * @return the razon
	 */
	public String getRazon() {
		return razon;
	}

	/**
	 * @param razon the razon to set
	 */
	public void setRazon(String razon) {
		this.razon = razon;
	}

	/**
	 * @return the valor
	 */
	public Double getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(Double valor) {
		this.valor = valor;
	}

}
