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
@XmlRootElement(name = "totalImpuesto")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlTotalImpuesto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -214961181226193903L;
	
	@XmlElement
	private String codigo;
	@XmlElement
    private String codigoPorcentaje;
    @XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero.class)
    private Double descuentoAdicional;
    @XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero.class)
    private Double baseImponible = 0.00d;
    @XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero.class)
    private Double tarifa;
    @XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero.class)
    private Double valor = 0.00d;
    @XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero.class)
    private Double valorDevolucionIva;
	
	/**
	 * 
	 */
	public XmlTotalImpuesto() {
		
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
	 * @return the descuentoAdicional
	 */
	public Double getDescuentoAdicional() {
		return descuentoAdicional;
	}


	/**
	 * @param descuentoAdicional the descuentoAdicional to set
	 */
	public void setDescuentoAdicional(Double descuentoAdicional) {
		this.descuentoAdicional = descuentoAdicional;
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


	/**
	 * @return the valorDevolucionIva
	 */
	public Double getValorDevolucionIva() {
		return valorDevolucionIva;
	}


	/**
	 * @param valorDevolucionIva the valorDevolucionIva to set
	 */
	public void setValorDevolucionIva(Double valorDevolucionIva) {
		this.valorDevolucionIva = valorDevolucionIva;
	}

}
