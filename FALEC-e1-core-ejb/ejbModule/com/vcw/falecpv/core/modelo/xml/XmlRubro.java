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
@XmlRootElement(name = "rubro")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlRubro implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3244593792755818264L;

	@XmlElement
	private String concepto;
	@XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero.class)
    private Double total;
	
	/**
	 * 
	 */
	public XmlRubro() {
	}

	/**
	 * @return the concepto
	 */
	public String getConcepto() {
		return concepto;
	}

	/**
	 * @param concepto the concepto to set
	 */
	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	/**
	 * @return the total
	 */
	public Double getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(Double total) {
		this.total = total;
	}

}
