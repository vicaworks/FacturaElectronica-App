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
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.vcw.falecpv.core.modelo.xml.adapter.XmlAdapterSriNumero;
import com.vcw.falecpv.core.modelo.xml.adapter.XmlAdapterSriNumero6;

/**
 * @author cristianvillarreal
 *
 */
@XmlRootElement(name = "detalle")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlDetalle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8408647799057751642L;
	
	@XmlElement
	private String codigoPrincipal;
	@XmlElement
    private String codigoAuxiliar;
	@XmlElement
    private String descripcion;
	@XmlElement
    private String unidadMedida;
	@XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero6.class)
    //private Double cantidad = 0.000000d;
	private Double cantidad;
	@XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero6.class)
//    private Double precioUnitario = 0.000000d;
	private Double precioUnitario;
	@XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero6.class)
//    private Double precioSinSubsidio = 0.000000d;
	private Double precioSinSubsidio;
    @XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero.class)
    private Double descuento;
    @XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero.class)
    private Double precioTotalSinImpuesto;
    @XmlElementRef
    @XmlElementWrapper(name = "detallesAdicionales")
    private List<XmlDetAdicional> detAdicionalList;
    @XmlElementRef
    @XmlElementWrapper(name = "impuestos")
    private List<XmlImpuesto> impuestoList;

	/**
	 * 
	 */
	public XmlDetalle() {
		
	}

	/**
	 * @return the codigoPrincipal
	 */
	public String getCodigoPrincipal() {
		return codigoPrincipal;
	}

	/**
	 * @param codigoPrincipal the codigoPrincipal to set
	 */
	public void setCodigoPrincipal(String codigoPrincipal) {
		this.codigoPrincipal = codigoPrincipal;
	}

	/**
	 * @return the codigoAuxiliar
	 */
	public String getCodigoAuxiliar() {
		return codigoAuxiliar;
	}

	/**
	 * @param codigoAuxiliar the codigoAuxiliar to set
	 */
	public void setCodigoAuxiliar(String codigoAuxiliar) {
		this.codigoAuxiliar = codigoAuxiliar;
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
	 * @return the unidadMedida
	 */
	public String getUnidadMedida() {
		return unidadMedida;
	}

	/**
	 * @param unidadMedida the unidadMedida to set
	 */
	public void setUnidadMedida(String unidadMedida) {
		this.unidadMedida = unidadMedida;
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
	 * @return the precioSinSubsidio
	 */
	public Double getPrecioSinSubsidio() {
		return precioSinSubsidio;
	}

	/**
	 * @param precioSinSubsidio the precioSinSubsidio to set
	 */
	public void setPrecioSinSubsidio(Double precioSinSubsidio) {
		this.precioSinSubsidio = precioSinSubsidio;
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
