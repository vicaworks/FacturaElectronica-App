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

/**
 * @author cristianvillarreal
 *
 */
@XmlRootElement(name = "detalle")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlDestinatarioDetalle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -179487164384701332L;
	
	@XmlElement
	private String codigoInterno;
	@XmlElement
	private String codigoAdicional;
	@XmlElement
	private String descripcion;
	@XmlElement
	private Integer cantidad;
	
	@XmlElementRef
	@XmlElementWrapper(name = "detallesAdicionales")
	private List<XmlDetallesAdicionales> detallesAdicionalesList;
	
	/**
	 * 
	 */
	public XmlDestinatarioDetalle() {
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
	public Integer getCantidad() {
		return cantidad;
	}

	/**
	 * @param cantidad the cantidad to set
	 */
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	/**
	 * @return the detallesAdicionalesList
	 */
	public List<XmlDetallesAdicionales> getDetallesAdicionalesList() {
		return detallesAdicionalesList;
	}

	/**
	 * @param detallesAdicionalesList the detallesAdicionalesList to set
	 */
	public void setDetallesAdicionalesList(List<XmlDetallesAdicionales> detallesAdicionalesList) {
		this.detallesAdicionalesList = detallesAdicionalesList;
	}

}
