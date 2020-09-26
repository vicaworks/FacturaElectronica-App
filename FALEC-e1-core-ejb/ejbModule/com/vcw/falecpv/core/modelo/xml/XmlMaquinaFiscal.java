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
@XmlRootElement(name = "maquinaFiscal")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlMaquinaFiscal implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6871723527574478359L;

	@XmlElement
	private String marca;
	@XmlElement
    private String modelo;
	@XmlElement
    private String serie;
	
	
	/**
	 * 
	 */
	public XmlMaquinaFiscal() {
	}


	/**
	 * @return the marca
	 */
	public String getMarca() {
		return marca;
	}


	/**
	 * @param marca the marca to set
	 */
	public void setMarca(String marca) {
		this.marca = marca;
	}


	/**
	 * @return the modelo
	 */
	public String getModelo() {
		return modelo;
	}


	/**
	 * @param modelo the modelo to set
	 */
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}


	/**
	 * @return the serie
	 */
	public String getSerie() {
		return serie;
	}


	/**
	 * @param serie the serie to set
	 */
	public void setSerie(String serie) {
		this.serie = serie;
	}

}
