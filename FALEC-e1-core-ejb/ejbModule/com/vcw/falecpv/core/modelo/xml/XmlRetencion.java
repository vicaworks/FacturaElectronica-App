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
@XmlRootElement(name = "retencion")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlRetencion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5364787442417202003L;

	@XmlElement
	private String codigo;
	@XmlElement
    private String codigoPorcentaje;
	@XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero.class)
    private Double tarifa;
	@XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero.class)
    private Double valor;
	
	/**
	 * 
	 */
	public XmlRetencion() {
		
	}

	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the codigoPorcentaje
	 */
	public String getCodigoPorcentaje() {
		return codigoPorcentaje;
	}

	/**
	 * @param codigoPorcentaje the codigoPorcentaje to set
	 */
	public void setCodigoPorcentaje(String codigoPorcentaje) {
		this.codigoPorcentaje = codigoPorcentaje;
	}

	/**
	 * @return the tarifa
	 */
	public Double getTarifa() {
		return tarifa;
	}

	/**
	 * @param tarifa the tarifa to set
	 */
	public void setTarifa(Double tarifa) {
		this.tarifa = tarifa;
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
