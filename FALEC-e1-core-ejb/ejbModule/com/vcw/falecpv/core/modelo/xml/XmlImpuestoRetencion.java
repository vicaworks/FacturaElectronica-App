package com.vcw.falecpv.core.modelo.xml;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.vcw.falecpv.core.modelo.xml.adapter.XmlAdapterSriDate;
import com.vcw.falecpv.core.modelo.xml.adapter.XmlAdapterSriNumero;

@XmlRootElement(name = "impuesto")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlImpuestoRetencion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4620913996963738457L;

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
	@XmlElement
	private String codDocSustento;
	@XmlElement
	private String numDocSustento;
	@XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriDate.class)
	private Date fechaEmisionDocSustento;
	
	
	@XmlTransient
	private String desCodigo;
	@XmlTransient
	private String desCodDocSustento;
	@XmlTransient
	private String desCodigoRetencion;
	
	public XmlImpuestoRetencion() {
		
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


	/**
	 * @return the codDocSustento
	 */
	public String getCodDocSustento() {
		return codDocSustento;
	}


	/**
	 * @param codDocSustento the codDocSustento to set
	 */
	public void setCodDocSustento(String codDocSustento) {
		this.codDocSustento = codDocSustento;
	}


	/**
	 * @return the numDocSustento
	 */
	public String getNumDocSustento() {
		return numDocSustento;
	}


	/**
	 * @param numDocSustento the numDocSustento to set
	 */
	public void setNumDocSustento(String numDocSustento) {
		this.numDocSustento = numDocSustento;
	}


	/**
	 * @return the fechaEmisionDocSustento
	 */
	public Date getFechaEmisionDocSustento() {
		return fechaEmisionDocSustento;
	}


	/**
	 * @param fechaEmisionDocSustento the fechaEmisionDocSustento to set
	 */
	public void setFechaEmisionDocSustento(Date fechaEmisionDocSustento) {
		this.fechaEmisionDocSustento = fechaEmisionDocSustento;
	}

	/**
	 * @return the desCodigo
	 */
	public String getDesCodigo() {
		return desCodigo;
	}

	/**
	 * @param desCodigo the desCodigo to set
	 */
	public void setDesCodigo(String desCodigo) {
		this.desCodigo = desCodigo;
	}

	/**
	 * @return the desCodDocSustento
	 */
	public String getDesCodDocSustento() {
		return desCodDocSustento;
	}

	/**
	 * @param desCodDocSustento the desCodDocSustento to set
	 */
	public void setDesCodDocSustento(String desCodDocSustento) {
		this.desCodDocSustento = desCodDocSustento;
	}

	/**
	 * @return the desCodigoRetencion
	 */
	public String getDesCodigoRetencion() {
		return desCodigoRetencion;
	}

	/**
	 * @param desCodigoRetencion the desCodigoRetencion to set
	 */
	public void setDesCodigoRetencion(String desCodigoRetencion) {
		this.desCodigoRetencion = desCodigoRetencion;
	}

}
