/**
 * 
 */
package com.vcw.falecpv.core.modelo.xml;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.vcw.falecpv.core.modelo.xml.adapter.XmlAdapterSriNumero;

/**
 * @author cristianvillarreal
 *
 */
@XmlRootElement(name = "detalle")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlNotaCreditoDetalle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2911407179137713626L;

	@XmlElement
	private String codigoInterno;
	@XmlElement
    private String codigoAdicional;
	@XmlElement
    private String descripcion;
	@XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero.class)
    private Double cantidad;
	@XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero.class)
    private Double precioUnitario;
	@XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero.class)
    private Double descuento;
	@XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero.class)
    private Double precioTotalSinImpuesto;
	
	@XmlElementRef(name = "detallesAdicionales")
	private List<XmlDetAdicional> detAdicionalList;
	
	@XmlElementRef(name = "impuestos")
	private List<XmlImpuesto> impuestoList;
	
	/**
	 * 
	 */
	public XmlNotaCreditoDetalle() {
		
	}

	/**
	 * @return the codigoInterno
	 */
	public String getCodigoInterno() {
		return codigoInterno;
	}

	/**
	 * @param codigoInterno the codigoInterno to set
	 */
	public void setCodigoInterno(String codigoInterno) {
		this.codigoInterno = codigoInterno;
	}

	/**
	 * @return the codigoAdicional
	 */
	public String getCodigoAdicional() {
		return codigoAdicional;
	}

	/**
	 * @param codigoAdicional the codigoAdicional to set
	 */
	public void setCodigoAdicional(String codigoAdicional) {
		this.codigoAdicional = codigoAdicional;
	}

	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return the cantidad
	 */
	public Double getCantidad() {
		return cantidad;
	}

	/**
	 * @param cantidad the cantidad to set
	 */
	public void setCantidad(Double cantidad) {
		this.cantidad = cantidad;
	}

	/**
	 * @return the precioUnitario
	 */
	public Double getPrecioUnitario() {
		return precioUnitario;
	}

	/**
	 * @param precioUnitario the precioUnitario to set
	 */
	public void setPrecioUnitario(Double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	/**
	 * @return the descuento
	 */
	public Double getDescuento() {
		return descuento;
	}

	/**
	 * @param descuento the descuento to set
	 */
	public void setDescuento(Double descuento) {
		this.descuento = descuento;
	}

	/**
	 * @return the precioTotalSinImpuesto
	 */
	public Double getPrecioTotalSinImpuesto() {
		return precioTotalSinImpuesto;
	}

	/**
	 * @param precioTotalSinImpuesto the precioTotalSinImpuesto to set
	 */
	public void setPrecioTotalSinImpuesto(Double precioTotalSinImpuesto) {
		this.precioTotalSinImpuesto = precioTotalSinImpuesto;
	}

	/**
	 * @return the detAdicionalList
	 */
	public List<XmlDetAdicional> getDetAdicionalList() {
		return detAdicionalList;
	}

	/**
	 * @param detAdicionalList the detAdicionalList to set
	 */
	public void setDetAdicionalList(List<XmlDetAdicional> detAdicionalList) {
		this.detAdicionalList = detAdicionalList;
	}

	/**
	 * @return the impuestoList
	 */
	public List<XmlImpuesto> getImpuestoList() {
		return impuestoList;
	}

	/**
	 * @param impuestoList the impuestoList to set
	 */
	public void setImpuestoList(List<XmlImpuesto> impuestoList) {
		this.impuestoList = impuestoList;
	}

}
