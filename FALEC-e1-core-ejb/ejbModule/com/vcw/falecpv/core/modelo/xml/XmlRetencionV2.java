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
public class XmlRetencionV2 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5364787442417202003L;

	@XmlElement
	private String codigo;
	
	@XmlElement
	private String codigoRetencion;
	
	@XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero.class)
    private Double baseImponible;
	
	@XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero.class)
    private Double porcentajeRetener;
	
	
	@XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero.class)
    private Double valorRetenido;
	
		
	/**
	 * 
	 */
	public XmlRetencionV2() {
		
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
	 * @return the codigoRetencion
	 */
	public String getCodigoRetencion() {
		return codigoRetencion;
	}


	/**
	 * @param codigoRetencion the codigoRetencion to set
	 */
	public void setCodigoRetencion(String codigoRetencion) {
		this.codigoRetencion = codigoRetencion;
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
	 * @return the porcentajeRetener
	 */
	public Double getPorcentajeRetener() {
		return porcentajeRetener;
	}


	/**
	 * @param porcentajeRetener the porcentajeRetener to set
	 */
	public void setPorcentajeRetener(Double porcentajeRetener) {
		this.porcentajeRetener = porcentajeRetener;
	}


	/**
	 * @return the valorRetenido
	 */
	public Double getValorRetenido() {
		return valorRetenido;
	}


	/**
	 * @param valorRetenido the valorRetenido to set
	 */
	public void setValorRetenido(Double valorRetenido) {
		this.valorRetenido = valorRetenido;
	}
	
}
