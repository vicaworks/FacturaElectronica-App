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
@XmlRootElement(name = "detalleImpuesto")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlDetalleImpuesto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2779217300639250997L;
	
	@XmlElement
	private String codigo;
	@XmlElement
    private String codigoPorcentaje;
	@XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero.class)
    private Double tarifa;
    @XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero.class)
    private Double baseImponibleReembolso;
    @XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero.class)
    private Double impuestoReembolso;
	
	/**
	 * 
	 */
	public XmlDetalleImpuesto() {
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
	 * @return the baseImponibleReembolso
	 */
	public Double getBaseImponibleReembolso() {
		return baseImponibleReembolso;
	}

	/**
	 * @param baseImponibleReembolso the baseImponibleReembolso to set
	 */
	public void setBaseImponibleReembolso(Double baseImponibleReembolso) {
		this.baseImponibleReembolso = baseImponibleReembolso;
	}

	/**
	 * @return the impuestoReembolso
	 */
	public Double getImpuestoReembolso() {
		return impuestoReembolso;
	}

	/**
	 * @param impuestoReembolso the impuestoReembolso to set
	 */
	public void setImpuestoReembolso(Double impuestoReembolso) {
		this.impuestoReembolso = impuestoReembolso;
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

}
