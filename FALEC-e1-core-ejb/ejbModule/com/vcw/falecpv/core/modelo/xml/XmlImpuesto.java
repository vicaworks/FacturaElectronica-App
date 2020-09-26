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
@XmlRootElement(name = "impuesto")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlImpuesto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1716146859075169004L;

	@XmlElement
	private String codigo;
	@XmlElement
    private String codigoPorcentaje;
    @XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero.class)
    private Double tarifa;
    @XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero.class)
    private Double baseImponible;
    @XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero.class)
    private Double valor;
	
	/**
	 * 
	 */
	public XmlImpuesto() {
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
