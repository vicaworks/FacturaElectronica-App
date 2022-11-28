package com.vcw.falecpv.core.modelo.xml;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.vcw.falecpv.core.modelo.xml.adapter.XmlAdapterSriNumero;

@XmlRootElement(name = "impuestoDocSustento")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlImpuestoDocSustento implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4620913996963738457L;

	@XmlElement
	private String codImpuestoDocSustento;
	
	@XmlElement
	private String codigoPorcentaje;
	
	@XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero.class)
	private Double baseImponible;
	
	@XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero.class)
	private Double tarifa;
	
	@XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero.class)
	private Double valorImpuesto;
		
	public XmlImpuestoDocSustento() {
		
	}

	/**
	 * @return the codImpuestoDocSustento
	 */
	public String getCodImpuestoDocSustento() {
		return codImpuestoDocSustento;
	}

	/**
	 * @param codImpuestoDocSustento the codImpuestoDocSustento to set
	 */
	public void setCodImpuestoDocSustento(String codImpuestoDocSustento) {
		this.codImpuestoDocSustento = codImpuestoDocSustento;
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
	 * @return the baseImponible
	 */
	public Double getBaseImponible() {
		return baseImponible;
	}

	/**
	 * @param baseImponible the baseImponible to set
	 */
	public void setBaseImponible(Double baseImponible) {
		this.baseImponible = baseImponible;
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
	 * @return the valorImpuesto
	 */
	public Double getValorImpuesto() {
		return valorImpuesto;
	}

	/**
	 * @param valorImpuesto the valorImpuesto to set
	 */
	public void setValorImpuesto(Double valorImpuesto) {
		this.valorImpuesto = valorImpuesto;
	}
	
}
